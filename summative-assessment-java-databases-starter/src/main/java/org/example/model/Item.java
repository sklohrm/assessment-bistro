package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Item {
    private int itemID;
    private int itemCategoryID;
    private String itemName;
    private String itemDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal unitPrice;

    ItemCategory itemCategory;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemCategoryID() {
        return itemCategoryID;
    }

    public void setItemCategoryID(int itemCategoryID) {
        this.itemCategoryID = itemCategoryID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return getItemID() == item.getItemID() && getItemCategoryID() == item.getItemCategoryID() && Objects.equals(getItemName(), item.getItemName()) && Objects.equals(getItemDescription(), item.getItemDescription()) && Objects.equals(getStartDate(), item.getStartDate()) && Objects.equals(getEndDate(), item.getEndDate()) && Objects.equals(getUnitPrice(), item.getUnitPrice()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemID(), getItemCategoryID(), getItemName(), getItemDescription(), getStartDate(), getEndDate(), getUnitPrice());
    }

    @Override
    public String toString() {
        return "Item{" +
                "ItemID=" + itemID +
                ", ItemCategoryID=" + itemCategoryID +
                ", ItemName='" + itemName + '\'' +
                ", ItemDescription='" + itemDescription + '\'' +
                ", StartDate=" + startDate +
                ", EndDate=" + endDate +
                ", UnitPrice=" + unitPrice +
                '}';
    }
}
