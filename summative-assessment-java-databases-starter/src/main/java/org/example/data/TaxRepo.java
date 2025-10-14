package org.example.data;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Tax;

import java.time.LocalDate;
import java.util.List;

public interface TaxRepo {
    // Tax records have a StartDate and an EndDate.
    // Return only the records where the given date falls in between the StartDate and EndDate inclusive.
    public Tax getCurrentTax(LocalDate dateOf) throws InternalErrorException, RecordNotFoundException;
}
