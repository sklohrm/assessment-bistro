package org.example.data.impl;

import org.example.data.PaymentTypeRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.model.PaymentType;

import java.util.List;

public class PaymentTypeJdbcRepo implements PaymentTypeRepo {
    @Override
    public List<PaymentType> getAll() throws InternalErrorException {
        return List.of();
    }
}
