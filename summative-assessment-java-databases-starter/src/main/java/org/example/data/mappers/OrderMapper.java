package org.example.data.mappers;

import org.example.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderMapper implements RowMapper<Order> {

    private final ServerMapper serverMapper = new ServerMapper();
    private final OrderItemMapper orderItemMapper = new OrderItemMapper();
    private final PaymentMapper paymentMapper = new PaymentMapper();

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setOrderID(rs.getInt("OrderID"));
        order.setServerID(rs.getInt("ServerID"));

        Timestamp ts = rs.getTimestamp("OrderDate");
        if (ts != null) {
            order.setOrderDate(ts.toLocalDateTime());
        }

        order.setSubTotal(rs.getBigDecimal("SubTotal"));
        order.setTax(rs.getBigDecimal("Tax"));
        order.setTip(rs.getBigDecimal("Tip"));
        order.setTotal(rs.getBigDecimal("Total"));

        if (order.getItems() == null) order.setItems(new ArrayList<>());
        if (order.getPayments() == null) order.setPayments(new ArrayList<>());

        if (rs.getInt("OrderItemID") != 0) {
            order.getItems().add(orderItemMapper.mapRow(rs, rowNum));
        }
        if (rs.getInt("PaymentID") != 0) {
            order.getPayments().add(paymentMapper.mapRow(rs, rowNum));
        }

        return order;

    }
}
