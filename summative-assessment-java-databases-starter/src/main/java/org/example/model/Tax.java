package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Tax {
    private int taxID;
    private BigDecimal taxPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tax tax)) return false;
        return getTaxID() == tax.getTaxID() && Objects.equals(getTaxPercentage(), tax.getTaxPercentage()) && Objects.equals(getStartDate(), tax.getStartDate()) && Objects.equals(getEndDate(), tax.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTaxID(), getTaxPercentage(), getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return "Tax{" +
                "taxID=" + taxID +
                ", taxPercentage=" + taxPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
