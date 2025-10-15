package org.example.data.impl;

import org.example.data.ItemRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.mappers.ItemCategoryMapper;
import org.example.data.mappers.ItemMapper;
import org.example.model.Item;
import org.example.model.ItemCategory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class ItemJdbcRepo implements ItemRepo {
    private final JdbcTemplate jdbcTemplate;

    public ItemJdbcRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Item getItemById(int id) throws RecordNotFoundException, InternalErrorException {
        final String sql = getSelectQuery() + " WHERE Item.ItemID = ?;";

        ItemMapper mapper = new ItemMapper();

        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Item> getAllAvailableItems(LocalDate today) throws InternalErrorException {
        final String sql = getSelectQuery() +
        " WHERE StartDate >= " + today +
        " AND (EndDate < " + today +
                " OR EndDate IS NULL);"; // assumes Null EndDate means items available

        ItemMapper mapper = new ItemMapper();

        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Item> getItemsByCategory(LocalDate today, int itemCategoryID) throws InternalErrorException {
        final String sql = getSelectQuery() + " WHERE ItemCategoryID = ?;";
        ItemMapper mapper = new ItemMapper();
        List<Item> items = new ArrayList<>();
        try {
            items.add(jdbcTemplate.queryForObject(sql, mapper, itemCategoryID));
            return items;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<ItemCategory> getAllItemCategories() throws InternalErrorException {
        final String sql = getSelectDistinctQuery();

        ItemCategoryMapper mapper = new ItemCategoryMapper();

        try {
            List<ItemCategory> categories = jdbcTemplate.query(sql, mapper);

            // Use LinkedHashSet to preserve order and eliminate duplicates
            Set<ItemCategory> uniqueCategories = new LinkedHashSet<>(categories);

            return new ArrayList<>(uniqueCategories);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getSelectQuery() {
        return "SELECT Item.ItemID, " +
                "Item.ItemCategoryID, " +
                "ItemCategory.ItemCategoryName, " +
                "Item.ItemName, " +
                "Item.ItemDescription, " +
                "Item.StartDate, " +
                "Item.EndDate, " +
                "Item.UnitPrice " +
                "FROM SimpleBistro.Item " +
                "INNER JOIN SimpleBistro.ItemCategory " +
                "ON Item.ItemCategoryID = ItemCategory.ItemCategoryID";
    }

    private String getSelectDistinctQuery() {
        return "SELECT DISTINCT " +
                "ItemCategory.ItemCategoryID, " +
                "ItemCategory.ItemCategoryName " +
                "FROM SimpleBistro.ItemCategory " +
                "ORDER BY ItemCategory.ItemCategoryName;";
    }
}
