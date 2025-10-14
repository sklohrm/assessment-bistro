package org.example.data;

import org.example.data.exceptions.InternalErrorException;
import org.example.model.PaymentType;

import java.util.List;

public interface PaymentTypeRepo {
    List<PaymentType> getAll() throws InternalErrorException;
}
