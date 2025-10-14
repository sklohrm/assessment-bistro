package org.example.data.mappers;

import org.example.model.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMapper implements RowMapper<Payment> {
    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentID(rs.getInt("PaymentID"));
        payment.setPaymentTypeID(rs.getInt("PaymentTypeID"));
        payment.setOrderID(rs.getInt("OrderID"));
        payment.setAmount(rs.getBigDecimal("Amount"));
        return payment;
    }
}
