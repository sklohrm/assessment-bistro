package org.example.view;

import org.example.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface IO {
    void prompt(String prompt);
    void displayMessage(String message);
    void displayTitle(String title);
    String getString(String prompt);
    int getInt(String prompt);
    int getIntRequiredRange(String prompt, int min, int max);
    BigDecimal getMoney(String prompt);
    public String asCurrency(double money);
    void displayOrders(List<Order> orders);
    void displayOrder(Order order);
    void displayMenuChoice(int choiceId, String label);
}
