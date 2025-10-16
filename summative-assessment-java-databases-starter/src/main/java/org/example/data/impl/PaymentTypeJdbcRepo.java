package org.example.data.impl;

import org.example.data.PaymentTypeRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.mappers.PaymentTypeMapper;
import org.example.model.PaymentType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC implementation of {@link PaymentTypeRepo}
 * Read-only access to the {@code PaymentType} lookup table
 */

@Repository
public class PaymentTypeJdbcRepo implements PaymentTypeRepo {

    private final JdbcTemplate jdbc;

    /** Constructor injection of the configured {@link JdbcTemplate} */
    public PaymentTypeJdbcRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Returns all payment types order by ID
     * @throws InternalErrorException wraps any SQL error from the driver
     */

    @Override
    public List<PaymentType> getAll() throws InternalErrorException {
        // Use a plain string to avoid text-block (""") compiler level issues
        final String sql =
                "SELECT PaymentTypeID, PaymentTypeName " +
                        "FROM PaymentType " +
                        "ORDER BY PaymentTypeID";

        try {
            return jdbc.query(sql, new PaymentTypeMapper());
        } catch (DataAccessException ex) {
            throw new InternalErrorException(ex);
        }
    }
}
