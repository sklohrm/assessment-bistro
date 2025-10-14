package org.example.data.mappers;

import org.example.model.PaymentType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentTypeMapper implements RowMapper<PaymentType> {
    @Override
    public PaymentType mapRow(ResultSet rs, int rowNum) throws SQLException {
        PaymentType paymentType = new PaymentType();
        paymentType.setPaymentTypeID(rs.getInt("PaymentTypeID"));
        paymentType.setPaymentTypeName(rs.getString("PaymentTypeName"));
        return paymentType;
    }
}
