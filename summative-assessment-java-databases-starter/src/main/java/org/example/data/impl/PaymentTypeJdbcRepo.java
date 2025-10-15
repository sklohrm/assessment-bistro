package org.example.data.impl;

import org.example.data.PaymentTypeRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.mappers.PaymentTypeMapper;
import org.example.model.PaymentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentTypeJdbcRepo implements PaymentTypeRepo {

    private final JdbcTemplate jdbc;

    public PaymentTypeJdbcRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<PaymentType> getAll() throws InternalErrorException {
        return List.of();
    }

    // Implement later:
    // public List<PaymentType> findAll() { ... }
    // public Optional<PaymentType> findById(int id) { ... }
}
