package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private int orderID;
    private int serverID;
    private LocalDateTime orderDate;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal tip;
    private BigDecimal total;
    private Server server;
    private List<OrderItem> items;
    private List<Payment> payments;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTip() {
        return tip;
    }

    public void setTip(BigDecimal tip) {
        this.tip = tip;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return getOrderID() == order.getOrderID() && getServerID() == order.getServerID() && Objects.equals(getOrderDate(), order.getOrderDate()) && Objects.equals(getSubTotal(), order.getSubTotal()) && Objects.equals(getTax(), order.getTax()) && Objects.equals(getTip(), order.getTip()) && Objects.equals(getTotal(), order.getTotal()) && Objects.equals(getServer(), order.getServer()) && Objects.equals(getItems(), order.getItems()) && Objects.equals(getPayments(), order.getPayments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderID(), getServerID(), getOrderDate(), getSubTotal(), getTax(), getTip(), getTotal(), getServer(), getItems(), getPayments());
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", serverID=" + serverID +
                ", orderDate=" + orderDate +
                ", subTotal=" + subTotal +
                ", tax=" + tax +
                ", tip=" + tip +
                ", total=" + total +
                ", server=" + server +
                ", items=" + items +
                ", payments=" + payments +
                '}';
    }
}
