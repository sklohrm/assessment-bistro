package org.example.data.mappers;

import org.example.model.PaymentType;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentTypeMapperTest {
    @Test
    void mapRow_mapsAllColumns() throws Exception {
        // arrnage
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