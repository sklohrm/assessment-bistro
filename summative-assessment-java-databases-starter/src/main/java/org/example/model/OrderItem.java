package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private int orderItemID;
    private int orderID;
    private int itemID;
    private int quantity;
    private BigDecimal price;
    private Item item;

    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return getOrderItemID() == orderItem.getOrderItemID() && getOrderID() == orderItem.getOrderID() && getItemID() == orderItem.getItemID() && getQuantity() == orderItem.getQuantity() && Objects.equals(getPrice(), orderItem.getPrice()) && Objects.equals(getItem(), orderItem.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemID(), getOrderID(), getItemID(), getQuantity(), getPrice());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemID=" + orderItemID +
                ", orderID=" + orderID +
                ", itemID=" + itemID +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
