package org.example.data.mappers;

import org.example.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();

        orderItem.setOrderItemID(rs.getInt("OrderItemID"));
        orderItem.setOrderID(rs.getInt("OrderID"));
        orderItem.setItemID(rs.getInt("ItemID"));
        orderItem.setQuantity(rs.getInt("Quantity"));
        orderItem.setPrice(rs.getBigDecimal("Price"));

        return orderItem;
    }
}
