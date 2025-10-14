package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Payment {
    private int paymentID;
    private int paymentTypeID;
    private int orderID;
    private BigDecimal amount;
    private PaymentType paymentType;

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(int paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return getPaymentID() == payment.getPaymentID() && getPaymentTypeID() == payment.getPaymentTypeID() && getOrderID() == payment.getOrderID() && Objects.equals(getAmount(), payment.getAmount()) && Objects.equals(getPaymentType(), payment.getPaymentType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaymentID(), getPaymentTypeID(), getOrderID(), getAmount(), getPaymentType());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", paymentTypeID=" + paymentTypeID +
                ", orderID=" + orderID +
                ", amount=" + amount +
                ", paymentType=" + paymentType +
                '}';
    }
}
