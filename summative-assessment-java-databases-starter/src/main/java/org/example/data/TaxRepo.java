package org.example.data;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Tax;

import java.time.LocalDate;

/**
 * Provides access to Tax records.
 * Returns the tax row whose [StartDate, EndDate] range includes the given date (inclusive).
 * If EndDate is NULL in the DB, treat it as open-ended.
 */
public interface TaxRepo {

    Tax getCurrentTax(LocalDate dateOf)
            throws InternalErrorException, RecordNotFoundException;
}
