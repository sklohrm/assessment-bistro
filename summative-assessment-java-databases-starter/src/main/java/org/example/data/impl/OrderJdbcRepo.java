package org.example.data.impl;

import org.example.data.OrderRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.OrderMapper;
import org.example.model.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderJdbcRepo implements OrderRepo {

    private final JdbcTemplate jdbcTemplate;

    public OrderJdbcRepo (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order getOrderById(int id) throws RecordNotFoundException, InternalErrorException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() throws InternalErrorException, RecordNotFoundException {
        final String sql = getSelectQuery() + ";";
        return jdbcTemplate.query(sql, new OrderMapper());
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
                + "    s.FirstName AS ServerFirstName, "
                + "    s.LastName AS ServerLastName, "
                + "    s.HireDate AS ServerHireDate, "
                + "    s.TermDate AS ServerTermDate, "
                + "    oi.OrderItemID, "
                + "    oi.Quantity, "
                + "    oi.Price AS OrderItemPrice, "
                + "    i.ItemID, "
                + "    i.ItemName, "
                + "    i.ItemDescription, "
                + "    i.UnitPrice, "
                + "    i.StartDate AS ItemStartDate, "
                + "    i.EndDate AS ItemEndDate, "
                + "    ic.ItemCategoryID, "
                + "    ic.ItemCategoryName, "
                + "    p.PaymentID, "
                + "    p.Amount AS PaymentAmount, "
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
}
