package org.example.data.mappers;

import org.example.model.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxMapperTest {

    @Test
    void mapRow_mapsAllColumns_withEndDate() throws Exception {
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("TaxID")).thenReturn(1);
        when(rs.getBigDecimal("TaxPercentage")).thenReturn(new BigDecimal("6.25"));
        when(rs.getDate("StartDate")).thenReturn(java.sql.Date.valueOf("2022-01-01"));
        when(rs.getDate("EndDate")).thenReturn(java.sql.Date.valueOf("2022-12-31"));

        Tax t = new TaxMapper().mapRow(rs, 1);

        // adjust getters to your model naming (getTaxID vs getTaxId)
        assertEquals(1, t.getTaxID());
        assertEquals(0, new BigDecimal("6.25").compareTo(t.getTaxPercentage()));
        assertEquals(java.time.LocalDate.of(2022, 1, 1), t.getStartDate());
        assertEquals(java.time.LocalDate.of(2022, 12, 31), t.getEndDate());
    }

    @Test
    void mapRow_handlesNullEndDate() throws Exception {
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("TaxID")).thenReturn(2);
        when(rs.getBigDecimal("TaxPercentage")).thenReturn(new BigDecimal("5.75"));
        // IMPORTANT: still stub StartDate in this test
        when(rs.getDate("StartDate")).thenReturn(java.sql.Date.valueOf("2021-06-01"));
        when(rs.getDate("EndDate")).thenReturn(null); // nullable

        Tax t = new TaxMapper().mapRow(rs, 1);

        assertEquals(2, t.getTaxID());
        assertEquals(0, new BigDecimal("5.75").compareTo(t.getTaxPercentage()));
        assertEquals(java.time.LocalDate.of(2021, 6, 1), t.getStartDate());
        assertNull(t.getEndDate());
    }
}
