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
        server.setFirstName(rs.getString("ServerFirstName"));
        server.setLastName(rs.getString("ServerLastName"));
        if (rs.getDate("ServerHireDate") != null) {
            server.setHireDate(rs.getDate("ServerHireDate").toLocalDate());
        }
        if (rs.getDate("ServerTermDate") != null) {
            server.setTermDate(rs.getDate("ServerTermDate").toLocalDate());
        }
        return server;
    }
}
