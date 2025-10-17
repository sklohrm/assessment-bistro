package org.example.data.impl;

import org.example.data.OrderRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.OrderItemMapper;
import org.example.data.mappers.OrderMapper;
import org.example.data.mappers.PaymentMapper;
import org.example.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * {@code OrderJdbcRepo} is a JDBC-based implementation of the {@link OrderRepo} interface.
 * <p>
 * This class provides CRUD operations for the {@link Order} entity and its associated
 * {@link OrderItem}, {@link Payment}, and {@link Server} objects.
 * </p>
 *
 * <p>
 * Each method wraps SQL or data mapping errors into custom exceptions:
 * <ul>
 *     <li>{@link RecordNotFoundException} – thrown when an expected record is not found.</li>
 *     <li>{@link InternalErrorException} – thrown when a lower-level exception occurs
 * </ul>
 * </p>
 *
 * <p>
 * This repository joins multiple related tables to construct a fully populated {@link Order}
 * including its items, payments, and associated server data.
 * </p>
 */
@Repository
public class OrderJdbcRepo implements OrderRepo {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code OrderJdbcRepo} using the given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the JDBC template used for executing SQL queries and updates
     */
    public OrderJdbcRepo (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves an {@link Order} by its unique identifier.
     *
     * @param id the unique order ID
     * @return the {@link Order} associated with the given ID
     * @throws RecordNotFoundException if no order exists with the provided ID
     * @throws InternalErrorException  if an unexpected SQL or mapping error occurs
     */
    @Override
    public Order getOrderById(int id) throws RecordNotFoundException, InternalErrorException {
        final String sql = getSelectQuery() + " WHERE o.OrderID = ?;";

        try {
            List<Order> orders = fetchOrders(sql, id);
            if (orders.isEmpty()) {
                throw new RecordNotFoundException();
            }
            return orders.get(0);
        } catch (InternalErrorException e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * Retrieves all {@link Order} records in the system.
     *
     * @return a list of all orders
     * @throws RecordNotFoundException if no orders exist in the database
     * @throws InternalErrorException  if an unexpected SQL or mapping error occurs
     */
    @Override
    public List<Order> getAllOrders() throws InternalErrorException, RecordNotFoundException {
        final String sql = getSelectQuery() + ";";

        try {
            List<Order> orders = fetchOrders(sql);
            if (orders.isEmpty()) {
                throw new RecordNotFoundException();
            }
            return orders;
        } catch (RecordNotFoundException e) {
            throw new RecordNotFoundException(e);
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * Inserts a new {@link Order} record into the database.
     * <p>
     * The {@code OrderDate} field is automatically set to the current timestamp.
     * </p>
     *
     * @param order the {@link Order} object to insert
     * @return the inserted {@link Order} with its generated {@code OrderID}, or {@code null} if the insert failed
     * @throws InternalErrorException if an SQL error or constraint violation occurs
     */
    @Override
    @Transactional
    public Order addOrder(Order order) throws InternalErrorException {
        try {
            // Insert Order
            String sqlOrder = """
            INSERT INTO `Order` (ServerID, OrderDate, SubTotal, Tax, Tip, Total)
            VALUES (?, NOW(), ?, ?, ?, ?)
        """;

            int orderID = insertAndReturnKey(sqlOrder, ps -> {
                ps.setInt(1, order.getServerID());
                ps.setBigDecimal(2, order.getSubTotal());
                ps.setBigDecimal(3, order.getTax());
                ps.setBigDecimal(4, order.getTip());
                ps.setBigDecimal(5, order.getTotal());
            });

            order.setOrderID(orderID);

            // Insert OrderItems
            deleteAndInsertOrderItems(orderID, order.getItems());

            // Insert Payments
            deleteAndInsertPayments(orderID, order.getPayments());

            return order;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * Updates an existing {@link Order} record with new field values.
     *
     * @param order the {@link Order} containing updated information
     */
    @Override
    @Transactional
    public void updateOrder(Order order) throws InternalErrorException {
        try {
            String sqlOrder = """
            UPDATE `Order`
            SET ServerID = ?, OrderDate = ?, SubTotal = ?, Tax = ?, Tip = ?, Total = ?
            WHERE OrderID = ?
        """;

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlOrder);
                ps.setInt(1, order.getServerID());
                ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                ps.setBigDecimal(3, order.getSubTotal());
                ps.setBigDecimal(4, order.getTax());
                ps.setBigDecimal(5, order.getTip());
                ps.setBigDecimal(6, order.getTotal());
                ps.setInt(7, order.getOrderID());
                return ps;
            });

            int orderID = order.getOrderID();

            // Replace all OrderItems
            deleteAndInsertOrderItems(orderID, order.getItems());

            // Replace all Payments
            deleteAndInsertPayments(orderID, order.getPayments());

        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }


    /**
     * Deletes an {@link Order} and all related {@link Payment} and {@link OrderItem} records.
     * <p>
     * If the specified order does not exist, or no rows are deleted, a {@link RecordNotFoundException}
     * is wrapped and rethrown as an {@link InternalErrorException}.
     * </p>
     *
     * @param id the ID of the order to delete
     * @return the deleted {@link Order}, if the operation succeeded
     * @throws InternalErrorException if the order cannot be found or a SQL error occurs
     */
    @Override
    public Order deleteOrder(int id) throws InternalErrorException {
        try {

            Order order = getOrderById(id);

            jdbcTemplate.update("DELETE FROM Payment WHERE OrderID = ?", id);
            jdbcTemplate.update("DELETE FROM OrderItem WHERE OrderID = ?", id);

            int rowsAffected = jdbcTemplate.update("DELETE FROM `Order` WHERE OrderID = ?", id);

            if (rowsAffected > 0) {
                return order;
            } else {
                throw new RecordNotFoundException();
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    // Private helper methods

    /**
     * Constructs the base SQL query used for fetching complete {@link Order} records with
     * related {@link Server}, {@link OrderItem}, {@link Item}, {@link ItemCategory},
     * {@link Payment}, and {@link PaymentType} information.
     *
     * @return the base SQL SELECT statement
     */
    private String getSelectQuery() {
        return "SELECT "
                + "    o.OrderID, "
                + "    o.OrderDate, "
                + "    o.SubTotal, "
                + "    o.Tax, "
                + "    o.Tip, "
                + "    o.Total, "
                + "    s.ServerID, "
                + "    s.FirstName, "
                + "    s.LastName, "
                + "    s.HireDate, "
                + "    s.TermDate, "
                + "    oi.OrderItemID, "
                + "    oi.Quantity, "
                + "    oi.Price, "
                + "    i.ItemID, "
                + "    i.ItemName, "
                + "    i.ItemDescription, "
                + "    i.UnitPrice, "
                + "    i.StartDate, "
                + "    i.EndDate, "
                + "    ic.ItemCategoryID, "
                + "    ic.ItemCategoryName, "
                + "    p.PaymentID, "
                + "    p.Amount, "
                + "    pt.PaymentTypeID, "
                + "    pt.PaymentTypeName "
                + "FROM `Order` o "
                + "JOIN Server s ON o.ServerID = s.ServerID "
                + "LEFT JOIN OrderItem oi ON o.OrderID = oi.OrderID "
                + "LEFT JOIN Item i ON oi.ItemID = i.ItemID "
                + "LEFT JOIN ItemCategory ic ON i.ItemCategoryID = ic.ItemCategoryID "
                + "LEFT JOIN Payment p ON o.OrderID = p.OrderID "
                + "LEFT JOIN PaymentType pt ON p.PaymentTypeID = pt.PaymentTypeID ";
    }

    /**
     * Executes the given SQL query and maps the result set into a list of {@link Order} objects.
     * <p>
     * This method merges rows referring to the same order ID into a single {@link Order} instance,
     * attaching all related {@link OrderItem} and {@link Payment} objects.
     * </p>
     *
     * @param sql  the SQL query string
     * @param args query parameters
     * @return a list of {@link Order} objects
     * @throws RecordNotFoundException if no results are returned
     * @throws InternalErrorException  if an SQL or mapping error occurs
     */
    private List<Order> fetchOrders(String sql, Object... args) throws RecordNotFoundException, InternalErrorException {
        try {
            return jdbcTemplate.query(sql, rs -> {

                Map<Integer, Order> orderMap = new HashMap<>();

                OrderMapper orderMapper = new OrderMapper();
                OrderItemMapper orderItemMapper = new OrderItemMapper();
                PaymentMapper paymentMapper = new PaymentMapper();

                while (rs.next()) {
                    int orderID = rs.getInt("OrderID");

                    Order order = orderMap.computeIfAbsent(orderID, id -> {
                        try {
                            return orderMapper.mapRow(rs, rs.getRow());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    // Track added OrderItems
                    if (rs.getObject("OrderItemID") != null) {
                        int orderItemID = rs.getInt("OrderItemID");
                        boolean alreadyAdded = order.getItems().stream()
                                .anyMatch(oi -> oi.getOrderItemID() == orderItemID);
                        if (!alreadyAdded) {
                            order.getItems().add(orderItemMapper.mapRow(rs, rs.getRow()));
                        }
                    }

                    // Track added payments
                    if (rs.getObject("PaymentID") != null) {
                        int paymentID = rs.getInt("PaymentID");
                        boolean alreadyAdded = order.getPayments().stream()
                                .anyMatch(p -> p.getPaymentID() == paymentID);
                        if (!alreadyAdded) {
                            order.getPayments().add(paymentMapper.mapRow(rs, rs.getRow()));
                        }
                    }
                }

                return new ArrayList<>(orderMap.values());
            }, args);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * Executes the given SQL and returns the generated key
     * <p>
     * This method wraps pattern of preparing a statement, setting parameters,
     * executing the update, and retrieving the generated key.
     * </p>
     *
     * @param sql    the SQL insert statement with placeholders for parameters
     * @param setter a {@link PreparedStatementSetter} that sets the parameter values on the {@link PreparedStatement}
     * @return the generated key as an {@code int}
     * @throws InternalErrorException if a SQL error occurs or the key cannot be retrieved
     */
    private int insertAndReturnKey(String sql, PreparedStatementSetter setter) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            setter.setValues(ps);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * Functional interface used to set parameters on a {@link PreparedStatement}.
     * <p>
     * Implementations define how the SQL parameters should be populated before execution.
     * </p>
     */
    @FunctionalInterface
    private interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
     * Deletes all {@link OrderItem} records for the specified order and inserts the provided list of items.
     * <p>
     * If the {@code items} list is null or empty, no insertion occurs after the deletion.
     * </p>
     *
     * @param orderID the ID of the order whose items should be replaced
     * @param items   the list of {@link OrderItem} objects to insert
     */
    private void deleteAndInsertOrderItems(int orderID, List<OrderItem> items) {
        jdbcTemplate.update("DELETE FROM OrderItem WHERE OrderID = ?", orderID);

        if (items != null && !items.isEmpty()) {
            String sql = "INSERT INTO OrderItem (OrderID, ItemID, Quantity, Price) VALUES (?, ?, ?, ?)";
            jdbcTemplate.batchUpdate(sql, items, items.size(), (ps, item) -> {
                ps.setInt(1, orderID);
                ps.setInt(2, item.getItemID());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
            });
        }
    }

    /**
     * Deletes all {@link Payment} records for the specified order and inserts the provided list of payments.
     * <p>
     * If the {@code payments} list is null or empty, no insertion occurs after the deletion.
     * </p>
     *
     * @param orderID  the ID of the order whose payments should be replaced
     * @param payments the list of {@link Payment} objects to insert
     */
    private void deleteAndInsertPayments(int orderID, List<Payment> payments) {
        jdbcTemplate.update("DELETE FROM Payment WHERE OrderID = ?", orderID);

        if (payments != null && !payments.isEmpty()) {
            String sql = "INSERT INTO Payment (PaymentTypeID, OrderID, Amount) VALUES (?, ?, ?)";
            jdbcTemplate.batchUpdate(sql, payments, payments.size(), (ps, payment) -> {
                ps.setInt(1, payment.getPaymentTypeID());
                ps.setInt(2, orderID);
                ps.setBigDecimal(3, payment.getAmount());
            });
        }
    }
}
