package org.example.data.mappers;

import org.example.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderMapper implements RowMapper<Order> {

    private final ServerMapper serverMapper = new ServerMapper();

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();

        order.setOrderID(rs.getInt("OrderID"));
        order.setServerID(rs.getInt("ServerID"));
        order.setOrderDate(rs.getTimestamp("OrderDate").toLocalDateTime());
        order.setSubTotal(rs.getBigDecimal("SubTotal"));
        order.setTax(rs.getBigDecimal("Tax"));
        order.setTip(rs.getBigDecimal("Tip"));
        order.setTotal(rs.getBigDecimal("Total"));
        order.setServer(serverMapper.mapRow(rs, rowNum));
        order.setItems(new ArrayList<>());
        order.setPayments(new ArrayList<>());

        return order;

    }
}
