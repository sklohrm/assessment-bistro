package org.example.data.mappers;

import org.example.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderMapperTest {

    private final OrderMapper mapper = new OrderMapper();

    @Test
    void mapsRowCorrectlyWhenAllFieldsPresent() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        LocalDateTime time = LocalDateTime.of(2022, 5, 3, 0, 0);

        when(rs.getInt("OrderID")).thenReturn(1);
        when(rs.getInt("ServerID")).thenReturn(5);
        when(rs.getTimestamp("OrderDate")).thenReturn(Timestamp.valueOf(time));
        when(rs.getBigDecimal("SubTotal")).thenReturn(new BigDecimal("10.00"));
        when(rs.getBigDecimal("Tax")).thenReturn(new BigDecimal("1.00"));
        when(rs.getBigDecimal("Tip")).thenReturn(new BigDecimal("2.00"));
        when(rs.getBigDecimal("Total")).thenReturn(new BigDecimal("13.00"));

        Order result = mapper.mapRow(rs, 1);

        assertEquals(1, result.getOrderID());
        assertEquals(5, result.getServerID());
        assertEquals(time, result.getOrderDate());
        assertEquals(new BigDecimal("10.00"), result.getSubTotal());
        assertEquals(new BigDecimal("1.00"), result.getTax());
        assertEquals(new BigDecimal("2.00"), result.getTip());
        assertEquals(new BigDecimal("13.00"), result.getTotal());
        assertNotNull(result.getServer());
        assertNotNull(result.getItems());
        assertNotNull(result.getPayments());
    }

    @Test
    void throwsNullPointerWhenOrderDateIsNull() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getTimestamp("OrderDate")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> mapper.mapRow(rs, 1));
    }

    @Test
    void throwsSQLExceptionWhenResultSetFails() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("OrderID")).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> mapper.mapRow(rs, 1));
    }
}
