package org.example.data.impl;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.ItemCategoryMapper;
import org.example.data.mappers.ItemMapper;
import org.example.model.Item;
import org.example.model.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemJdbcRepoTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ItemJdbcRepo repository;

    private Item expectedItem;
    private List<Item> expectedItems;
    private LocalDate testDate;
    private Item testItem;
    private int testCategoryId;
    private List<ItemCategory> expectedCategories;

    /**
     * Tests use a mock database repository as an alternative to using a duplicate Test table
     */

    @BeforeEach
    void setUp() {
        // Setup expected Item by Id object
        expectedItem = new Item();
        expectedItem.setItemID(1);
        expectedItem.setItemName("Test Item");
        expectedItem.setItemDescription("Test Description");
        expectedItem.setUnitPrice(new BigDecimal("10.99"));

        // Setup expected available Item objects
        testDate = LocalDate.of(2024, 6, 15);

        Item item1 = new Item();
        item1.setItemID(1);
        item1.setItemName("Available Item 1");
        item1.setItemDescription("Description 1");
        item1.setUnitPrice(new BigDecimal("15.99"));
        item1.setStartDate(LocalDate.of(2024, 1, 1));
        item1.setEndDate(null); // No end date, available item

        Item item2 = new Item();
        item2.setItemID(2);
        item2.setItemName("Available Item 2");
        item2.setItemDescription("Description 2");
        item2.setUnitPrice(new BigDecimal("25.99"));
        item2.setStartDate(LocalDate.of(2024, 3, 1));
        item2.setEndDate(LocalDate.of(2024, 12, 31));

        expectedItems = Arrays.asList(item1, item2);

        // Setup Item for CategoryID test
        testCategoryId = 5;
        testItem = new Item();
        testItem.setItemID(1);
        testItem.setItemCategoryID(testCategoryId);
        testItem.setItemName("Test Item");
        testItem.setItemDescription("Test Description");
        testItem.setUnitPrice(new BigDecimal("19.99"));
        testItem.setStartDate(LocalDate.of(2024, 1, 1));
        testItem.setEndDate(null);

        // Setup for ItemCategory tests
        ItemCategory category1 = new ItemCategory(1, "Appetizers");
        ItemCategory category2 = new ItemCategory(2, "Entrees");
        ItemCategory category3 = new ItemCategory(3, "Desserts");
        ItemCategory category4 = new ItemCategory(4, "Beverages");

        expectedCategories = Arrays.asList(category1, category2, category3, category4);
    }

    @Test
    void getItemById_ShouldReturnItem() throws RecordNotFoundException, InternalErrorException {
        int itemId = 1;
        when(jdbcTemplate.queryForObject(anyString(), any(ItemMapper.class), eq(itemId)))
                .thenReturn(expectedItem);

        Item result = repository.getItemById(itemId);

        assertNotNull(result);
        assertEquals(expectedItem.getItemID(), result.getItemID());
        assertEquals(expectedItem.getItemName(), result.getItemName());
        assertEquals(expectedItem.getItemDescription(), result.getItemDescription());
        assertEquals(expectedItem.getUnitPrice(), result.getUnitPrice());

        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), any(ItemMapper.class), eq(itemId));
    }

    @Test
    void getItemById_ShouldThrowRecordNotFoundException() {
        int itemId = 999;
        when(jdbcTemplate.queryForObject(anyString(), any(ItemMapper.class), eq(itemId)))
                .thenThrow(new EmptyResultDataAccessException(1));

        assertThrows(RecordNotFoundException.class, () -> {
            repository.getItemById(itemId);
        });

        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), any(ItemMapper.class), eq(itemId));
    }

    @Test
    void getAllAvailableItems_ShouldReturnListOfItems() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemMapper.class)))
                .thenReturn(expectedItems);

        List<Item> result = repository.getAllAvailableItems(testDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedItems.get(0).getItemID(), result.get(0).getItemID());
        assertEquals(expectedItems.get(1).getItemID(), result.get(1).getItemID());

        verify(jdbcTemplate, times(1)).query(anyString(), any(ItemMapper.class));
    }

    @Test
    void getAllAvailableItems_ShouldReturnEmptyList() throws InternalErrorException {
        List<Item> emptyList = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(ItemMapper.class)))
                .thenReturn(emptyList);

        List<Item> result = repository.getAllAvailableItems(testDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(jdbcTemplate, times(1)).query(anyString(), any(ItemMapper.class));
    }

    @Test
    void getItemsByCategory_ShouldReturnListWithOneItem() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemMapper.class), eq(testCategoryId)))
                .thenReturn(Collections.singletonList(testItem));

        List<Item> result = repository.getItemsByCategory(testDate, testCategoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testItem.getItemID(), result.get(0).getItemID());
        assertEquals(testItem.getItemCategoryID(), result.get(0).getItemCategoryID());
        assertEquals(testItem.getItemName(), result.get(0).getItemName());

        verify(jdbcTemplate, times(1))
                .query(anyString(), any(ItemMapper.class), eq(testCategoryId));
    }

    @Test
    void getItemsByCategory_ShouldReturnNull() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemMapper.class), eq(testCategoryId)))
                .thenThrow(new EmptyResultDataAccessException(1));

        List<Item> result = repository.getItemsByCategory(testDate, testCategoryId);

        assertNull(result);

        verify(jdbcTemplate, times(1))
                .query(anyString(), any(ItemMapper.class), eq(testCategoryId));
    }

    @Test
    void getAllItemCategories_ShouldReturnAllCategories() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemCategoryMapper.class)))
                .thenReturn(expectedCategories);

        List<ItemCategory> result = repository.getAllItemCategories();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Appetizers", result.get(0).getItemCategoryName());
        assertEquals("Entrees", result.get(1).getItemCategoryName());
        assertEquals("Desserts", result.get(2).getItemCategoryName());
        assertEquals("Beverages", result.get(3).getItemCategoryName());

        verify(jdbcTemplate, times(1)).query(anyString(), any(ItemCategoryMapper.class));
    }

    @Test
    void getAllItemCategories_ShouldReturnEmptyList() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemCategoryMapper.class)))
                .thenReturn(new ArrayList<>());

        List<ItemCategory> result = repository.getAllItemCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(jdbcTemplate, times(1)).query(anyString(), any(ItemCategoryMapper.class));
    }

    @Test
    void testGetAllItemCategories_CatchesExceptionAndReturnsNull() throws InternalErrorException {
        when(jdbcTemplate.query(anyString(), any(ItemCategoryMapper.class)))
                .thenThrow(new DataAccessException("Database connection failed") {});

        List<ItemCategory> result = repository.getAllItemCategories();

        assertNull(result, "Expected null when exception is caught");
        verify(jdbcTemplate).query(anyString(), any(ItemCategoryMapper.class));
    }
}