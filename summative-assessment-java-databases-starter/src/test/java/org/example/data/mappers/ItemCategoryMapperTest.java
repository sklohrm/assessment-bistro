package org.example.data.mappers;

import org.example.model.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemCategoryMapperTest {

    @Mock
    private ResultSet resultSet;

    private ItemCategoryMapper itemCategoryMapper;

    @BeforeEach
    void setUp() {
        itemCategoryMapper = new ItemCategoryMapper();
    }

    @Test
    void testMapRow_WithValidData() throws SQLException {
        // Arrange
        int categoryId = 5;
        String categoryName = "Lunch";

        when(resultSet.getInt("ItemCategoryID")).thenReturn(categoryId);
        when(resultSet.getString("ItemCategoryName")).thenReturn(categoryName);

        // Act
        ItemCategory result = itemCategoryMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getItemCategoryID());
        assertEquals(categoryName, result.getItemCategoryName());

        verify(resultSet).getInt("ItemCategoryID");
        verify(resultSet).getString("ItemCategoryName");
    }

    @Test
    void testMapRow_ThrowsSQLException() throws SQLException {
        // Arrange
        when(resultSet.getInt("ItemCategoryID")).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            itemCategoryMapper.mapRow(resultSet, 0);
        });
    }
}