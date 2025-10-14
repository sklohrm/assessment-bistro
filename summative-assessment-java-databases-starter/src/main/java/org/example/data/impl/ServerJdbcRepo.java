package org.example.data.impl;

import org.example.data.ServerRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Server;

import java.time.LocalDate;
import java.util.List;

public class ServerJdbcRepo implements ServerRepo {
    @Override
    public Server getServerById(int id) throws InternalErrorException, RecordNotFoundException {
        return null;
    }

    @Override
    public List<Server> getAllAvailableServers(LocalDate date) throws InternalErrorException {
        return List.of();
    }
}
