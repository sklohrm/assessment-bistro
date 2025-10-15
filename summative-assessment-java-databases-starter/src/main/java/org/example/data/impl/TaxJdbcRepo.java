package org.example.data.impl;

import org.example.data.TaxRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Tax;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public class TaxJdbcRepo implements TaxRepo {
    @Override
    public Tax getCurrentTax(LocalDate dateOf) throws InternalErrorException, RecordNotFoundException {
        return null;
    }
}
