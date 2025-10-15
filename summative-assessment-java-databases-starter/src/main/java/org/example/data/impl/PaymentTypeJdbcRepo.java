package org.example.data.impl;

import org.example.data.PaymentTypeRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.mappers.PaymentTypeMapper;
import org.example.model.PaymentType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentTypeJdbcRepo implements PaymentTypeRepo {

    private final JdbcTemplate jdbc;

    public PaymentTypeJdbcRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<PaymentType> getAll() throws InternalErrorException {
        final String sql = """
            SELECT PaymentTypeID, PaymentTypeName
            FROM PaymentType
            ORDER BY PaymentTypeID
        """;
        try {
            return jdbc.query(sql, new PaymentTypeMapper());
        } catch (DataAccessException ex) {
            throw new InternalErrorException("Failed to load payment types.", ex);
        }
    }

}
