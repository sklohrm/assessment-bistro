package org.example.data.mappers;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerMapperTest {

    @Test
    void mapRow_mapsAllColumns_withNullTermDate() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("ServerID")).thenReturn(5);
        when(rs.getString("FirstName")).thenReturn("Ava");
        when(rs.getString("LastName")).thenReturn("Nguyen");
        when(rs.getDate("HireDate")).thenReturn(java.sql.Date.valueOf("2020-01-15"));
        when(rs.getDate("TermDate")).thenReturn(null);

        var server = new ServerMapper().mapRow(rs, 1);

        assertEquals(5, server.getServerID());
        assertEquals("Ava", server.getFirstName());
        assertEquals("Nguyen", server.getLastName());
        assertEquals(java.time.LocalDate.of(2020, 1, 15), server.getHireDate());
        assertNull(server.getTermDate());
    }

    @Test
    void mapRow_mapsTermDate_whenPresent() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("ServerID")).thenReturn(7);
        when(rs.getString("FirstName")).thenReturn("Leo");
        when(rs.getString("LastName")).thenReturn("Kim");
        when(rs.getDate("HireDate")).thenReturn(java.sql.Date.valueOf("2019-03-10"));
        when(rs.getDate("TermDate")).thenReturn(java.sql.Date.valueOf("2023-04-30"));

        var server = new ServerMapper().mapRow(rs, 1);

        assertEquals(7, server.getServerID());
        assertEquals("Leo", server.getFirstName());
        assertEquals("Kim", server.getLastName());
        assertEquals(java.time.LocalDate.of(2019,3, 10), server.getHireDate());
        assertEquals(java.time.LocalDate.of(2023, 4, 30), server.getTermDate());
    }
}