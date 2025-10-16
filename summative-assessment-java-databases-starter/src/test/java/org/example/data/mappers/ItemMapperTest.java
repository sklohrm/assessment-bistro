package org.example.data.mappers;

import org.example.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private ResultSet resultSet;

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper();
    }

    @Test
    void testMapRow_WithAllFields() throws SQLException {
        // Arrange
        int itemId = 1;
        int categoryId = 10;
        String itemName = "Test Item";
        String itemDescription = "Test Description";
        Date startDate = Date.valueOf("2024-01-01");
        Date endDate = Date.valueOf("2024-12-31");
        BigDecimal unitPrice = new BigDecimal("99.99");

        when(resultSet.getInt("ItemID")).thenReturn(itemId);
        when(resultSet.getInt("ItemCategoryID")).thenReturn(categoryId);
        when(resultSet.getString("ItemName")).thenReturn(itemName);
        when(resultSet.getString("ItemDescription")).thenReturn(itemDescription);
        when(resultSet.getDate("StartDate")).thenReturn(startDate);
        when(resultSet.getDate("EndDate")).thenReturn(endDate);
        when(resultSet.getBigDecimal("UnitPrice")).thenReturn(unitPrice);

        when(resultSet.getInt("ItemCategoryID")).thenReturn(categoryId);
        when(resultSet.getString("ItemCategoryName")).thenReturn("Test Category");

        // Act
        Item result = itemMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getItemID());
        assertEquals(categoryId, result.getItemCategoryID());
        assertEquals(itemName, result.getItemName());
        assertEquals(itemDescription, result.getItemDescription());
        assertEquals(LocalDate.of(2024, 1, 1), result.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), result.getEndDate());
        assertEquals(unitPrice, result.getUnitPrice());
        assertNotNull(result.getItemCategory());
    }

    @Test
    void testMapRow_WithNullDates() throws SQLException {
        // Arrange
        when(resultSet.getInt("ItemID")).thenReturn(1);
        when(resultSet.getInt("ItemCategoryID")).thenReturn(10);
        when(resultSet.getString("ItemName")).thenReturn("Test Item");
        when(resultSet.getString("ItemDescription")).thenReturn("Description");
        when(resultSet.getDate("StartDate")).thenReturn(null);
        when(resultSet.getDate("EndDate")).thenReturn(null);
        when(resultSet.getBigDecimal("UnitPrice")).thenReturn(BigDecimal.TEN);

        when(resultSet.getString("ItemCategoryName")).thenReturn("Category");

        // Act
        Item result = itemMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(result);
        assertNull(result.getStartDate());
        assertNull(result.getEndDate());
    }
}