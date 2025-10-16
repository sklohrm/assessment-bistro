package org.example.data.mappers;

import org.example.model.Tax;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@code Tax} row to a {@link Tax} object
 * Handles nullable {@code EndDate} safely
 */

public class TaxMapper implements RowMapper<Tax> {

    @Override
    public Tax mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tax tax = new Tax();

        // IDs / Numbers
        tax.setTaxID(rs.getInt("TaxID"));
        tax.setTaxPercentage(rs.getBigDecimal("TaxPercentage"));

        // Dates
        java.sql.Date start = rs.getDate("StartDate");
        tax.setStartDate(start == null ? null : start.toLocalDate());

        java.sql.Date end = rs.getDate("EndDate");
        tax.setEndDate(end == null ? null : end.toLocalDate());

        return tax;
    }
}
