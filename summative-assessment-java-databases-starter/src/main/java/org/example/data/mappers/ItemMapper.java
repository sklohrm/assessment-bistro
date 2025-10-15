package org.example.data.mappers;

import org.example.model.Item;
import org.example.model.ItemCategory;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ItemMapper implements RowMapper<Item> {

    private final ItemCategoryMapper categoryMapper = new ItemCategoryMapper();

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setItemID(rs.getInt("ItemID"));
        item.setItemCategoryID(rs.getInt("ItemCategoryID"));
        item.setItemName(rs.getString("ItemName"));
        item.setItemDescription(rs.getString("ItemDescription"));

        if (rs.getDate("ItemStartDate") != null) {
            item.setStartDate(rs.getDate("ItemStartDate").toLocalDate());
        }
        if (rs.getDate("ItemEndDate") != null) {
            item.setEndDate(rs.getDate("ItemEndDate").toLocalDate());
        }

        item.setUnitPrice(rs.getBigDecimal("UnitPrice"));

        item.setItemCategory(categoryMapper.mapRow(rs, rowNum));

        return item;
    }
}
