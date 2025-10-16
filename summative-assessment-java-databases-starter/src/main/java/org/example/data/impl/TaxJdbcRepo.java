package org.example.data.impl;

import org.example.data.TaxRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.TaxMapper;
import org.example.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TaxJdbcRepo implements TaxRepo {

    private final JdbcTemplate jdbc;

    @Autowired
    public TaxJdbcRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Tax getCurrentTax(LocalDate dateOf)
            throws InternalErrorException, RecordNotFoundException {

        final String sql =
                "SELECT TaxID, TaxPercentage, StartDate, EndDate " +
                        "FROM Tax " +
                        "WHERE ? BETWEEN StartDate AND IFNULL(EndDate, '9999-12-31') " +
                        "ORDER BY StartDate DESC " +
                        "LIMIT 1";

        try {
            List<Tax> rows = jdbc.query(sql, new TaxMapper(), Date.valueOf(dateOf));
            if (rows.isEmpty()) {
                throw new RecordNotFoundException();
            }
            return rows.get(0);
        } catch (DataAccessException ex) {
            throw new InternalErrorException(ex);
        }
    }
}





