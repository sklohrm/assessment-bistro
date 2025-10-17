package org.example.data.mappers;

import org.example.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {

    private final ItemCategoryMapper categoryMapper = new ItemCategoryMapper();

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setItemID(rs.getInt("ItemID"));
        item.setItemCategoryID(rs.getInt("ItemCategoryID"));
        item.setItemName(rs.getString("ItemName"));
        item.setItemDescription(rs.getString("ItemDescription"));

        if (rs.getDate("StartDate") != null) {
            item.setStartDate(rs.getDate("StartDate").toLocalDate());
        }
        if (rs.getDate("EndDate") != null) {
            item.setEndDate(rs.getDate("EndDate").toLocalDate());
        }

        item.setUnitPrice(rs.getBigDecimal("UnitPrice"));

        item.setItemCategory(categoryMapper.mapRow(rs, rowNum));

        return item;
    }
}
