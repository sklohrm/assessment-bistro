package org.example.data.impl;

import org.example.data.ServerRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.ServerMapper;
import org.example.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@Repository
public class ServerJdbcRepo implements ServerRepo {

    private final JdbcTemplate jdbc;

    @Autowired
    public ServerJdbcRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Server getServerById(int id) throws InternalErrorException, RecordNotFoundException {
        final String sql=
                "SELECT ServerID, FirstName, LastName, HireDate, TermDate " +
                "FROM `Server` " +
                "WHERE ServerID = ?";

        try {
            List<Server> result = jdbc.query(sql, new ServerMapper(), id);
            if (result.isEmpty()) {
                throw new RecordNotFoundException();
            }
            return result.get(0);
        } catch (DataAccessException ex) {
            throw new InternalErrorException(ex);
        }
    }

    @Override
    public List<Server> getAllAvailableServers(LocalDate date) throws InternalErrorException {
        final String sql=
                "SELECT ServerID, FirstName, LastName, HireDate, TermDate " +
                "FROM `Server` " +
                "WHERE HireDate <= ? " +
                " AND (TermDate IS NULL OR TermDate >= ?) " +
                "ORDER BY ServerID";
        try {
            Date d = Date.valueOf(date);
            return jdbc.query(sql, new ServerMapper(), d, d);
        } catch (DataAccessException ex) {
            throw new InternalErrorException(ex);
        }
    }
}