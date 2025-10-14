package org.example.data.mappers;

import org.example.model.Tax;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaxMapper implements RowMapper<Tax> {
    @Override
    public Tax mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tax tax = new Tax();
        tax.setTaxID(rs.getInt("TaxID"));
        tax.setTaxPercentage(rs.getBigDecimal("TaxPercentage"));
        // TODO: Is this cast ok?
        tax.setStartDate(rs.getDate("StartDate").toLocalDate());
        tax.setEndDate(rs.getDate("EndDate").toLocalDate());
        return tax;
    }
}
