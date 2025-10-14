package org.example.model;

import java.util.Objects;

public class PaymentType {
    private int paymentTypeID;
    private String paymentTypeName;

    public int getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(int paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentType that)) return false;
        return getPaymentTypeID() == that.getPaymentTypeID() && Objects.equals(getPaymentTypeName(), that.getPaymentTypeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaymentTypeID(), getPaymentTypeName());
    }

    @Override
    public String toString() {
        return "PaymentType{" +
                "paymentTypeID=" + paymentTypeID +
                ", paymentTypeName='" + paymentTypeName + '\'' +
                '}';
    }
}
