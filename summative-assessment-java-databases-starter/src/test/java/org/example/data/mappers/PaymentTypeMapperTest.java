package org.example.data.mappers;

import org.example.model.PaymentType;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link org.example.data.mappers.PaymentTypeMapper}
 * These test mock a {@link java.sql.ResultSet} and verify that a single row
 * from the {@code PaymentType} table is mapped to a {@link org.example.model.PaymentType}
 * correctly. Because this is pure mapping test, no database is involved
 */

/**
 * Verifies that all expected columns (ID + Name) map to the model
 */

class PaymentTypeMapperTest {
    @Test
    void mapRow_mapsAllColumns() throws Exception {
        // arrange
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("PaymentTypeID")).thenReturn(1);
        when(rs.getString("PaymentTypeName")).thenReturn("Cash");

        PaymentTypeMapper mapper = new PaymentTypeMapper();

        // act
        PaymentType pt = mapper.mapRow(rs, 1);

        // assert
        assertNotNull(pt);
        assertEquals(1, pt.getPaymentTypeID());
        assertEquals("Cash", pt.getPaymentTypeName());
    }
}