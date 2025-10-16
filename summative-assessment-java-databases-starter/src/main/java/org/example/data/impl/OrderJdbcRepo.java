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
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class OrderJdbcRepo implements OrderRepo {

    private final JdbcTemplate jdbcTemplate;

    public OrderJdbcRepo (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order getOrderById(int id) throws RecordNotFoundException, InternalErrorException {
        final String sql = getSelectQuery() + " WHERE o.OrderID = ?;";
        try {
            return null;
        } catch (Exception e) {
            System.out.println();
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() throws InternalErrorException, RecordNotFoundException {
        final String sql = getSelectQuery() + ";";

        try {
            List<Order> orders = jdbcTemplate.query(sql, (ResultSetExtractor<List<Order>>) rs -> mapOrders(rs));
            if (orders.isEmpty()) {
                throw new RecordNotFoundException();
            }
            return orders;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    @Override
    public Order addOrder(Order order) throws InternalErrorException {
        return null;
    }

    @Override
    public void updateOrder(Order order) throws InternalErrorException {

    }

    @Override
    public Order deleteOrder(int id) throws InternalErrorException {
        return null;
    }

    // Private helper method(s)
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
                + "LEFT JOIN PaymentType pt ON p.PaymentTypeID = pt.PaymentTypeID "
                + "ORDER BY o.OrderID, oi.OrderItemID, p.PaymentID";
    }

    private List<Order> mapOrders(ResultSet rs) throws SQLException {
        Map<Integer, Order> orderMap = new HashMap<>();

        OrderMapper orderMapper = new OrderMapper();
        OrderItemMapper orderItemMapper = new OrderItemMapper();
        PaymentMapper paymentMapper = new PaymentMapper();

        while (rs.next()) {
            int orderID = rs.getInt("OrderID");

            Order order = orderMap.get(orderID);

            if (order == null) {
                order = orderMapper.mapRow(rs, rs.getRow());
                orderMap.put(orderID, order);
            }

            order.getItems().add(orderItemMapper.mapRow(rs, rs.getRow()));
            order.getPayments().add(paymentMapper.mapRow(rs, rs.getRow()));
        }

        return new ArrayList<>(orderMap.values());
    }


}
