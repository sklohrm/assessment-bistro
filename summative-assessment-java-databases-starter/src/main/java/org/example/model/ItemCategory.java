package org.example.model;

import java.util.Objects;

public class ItemCategory {
    private int itemCategoryID;
    private String itemCategoryName;

    public ItemCategory(){}

    public ItemCategory(int itemCategoryID, String itemCategoryName) {
        this.itemCategoryID = itemCategoryID;
        this.itemCategoryName = itemCategoryName;
    }

    public int getItemCategoryID() {
        return itemCategoryID;
    }

    public void setItemCategoryID(int itemCategoryID) {
        this.itemCategoryID = itemCategoryID;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCategory that)) return false;
        return getItemCategoryID() == that.getItemCategoryID() && Objects.equals(getItemCategoryName(), that.getItemCategoryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemCategoryID(), getItemCategoryName());
    }

    @Override
    public String toString() {
        return "ItemCategory{" +
                "ItemCategoryID=" + itemCategoryID +
                ", ItemCategoryName='" + itemCategoryName + '\'' +
                '}';
    }
}
