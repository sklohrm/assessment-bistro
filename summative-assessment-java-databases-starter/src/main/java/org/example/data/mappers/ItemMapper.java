package org.example.data.mappers;

import org.example.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();

        item.setItemID(rs.getInt("ItemID"));
        item.setItemCategoryID(rs.getInt("ItemCategoryID"));
        item.setItemName(rs.getString("ItemName"));
        item.setItemDescription(rs.getString("ItemDescription"));
        // TODO: Might need to change this from .toLocalDate to something else
        item.setStartDate(rs.getDate("StartDate").toLocalDate());
        item.setEndDate(rs.getDate("EndDate").toLocalDate());

        return item;
    }
}
