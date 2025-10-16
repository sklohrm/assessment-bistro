package org.example.data.mappers;

import org.example.model.Server;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerMapper implements RowMapper<Server> {
    @Override
    public Server mapRow(ResultSet rs, int rowNum) throws SQLException {
        Server s = new Server();

        s.setServerID(rs.getInt("ServerID"));
        s.setFirstName(rs.getString("FirstName"));
        s.setLastName(rs.getString("LastName"));

        var hire = rs.getDate("HireDate");
        s.setHireDate(hire == null ? null : hire.toLocalDate());

        var term = rs.getDate("TermDate");
        s.setTermDate(term == null ? null : term.toLocalDate());

        return s;
    }
}
