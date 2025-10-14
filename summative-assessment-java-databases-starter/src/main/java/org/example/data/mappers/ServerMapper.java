package org.example.data.mappers;

import org.example.model.Server;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerMapper implements RowMapper<Server> {
    @Override
    public Server mapRow(ResultSet rs, int rowNum) throws SQLException {
        Server server = new Server();
        server.setServerID(rs.getInt("ServerID"));
        server.setFirstName(rs.getString("FirstName"));
        server.setLastName(rs.getString("LastName"));
        // TODO: Is this cast correct
        server.setHireDate(rs.getDate("HireDate").toLocalDate());
        // TODO: Do we need to handle null values here?
        server.setTermDate(rs.getDate("TermDate").toLocalDate());
        return server;
    }
}
