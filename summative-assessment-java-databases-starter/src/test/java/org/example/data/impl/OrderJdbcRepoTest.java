package org.example.data.impl;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {OrderJdbcRepo.class})
class OrderJdbcRepoTest {

    @Autowired
    OrderJdbcRepo repo;

    @Test
    void testThing() throws InternalErrorException, RecordNotFoundException {
        List<Order> orders = repo.getAllOrders();
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        System.out.println(orders.size());
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        System.out.println("TEST TEST TEST");
        int uniqueOrder = 0;
        ArrayList<Integer> seen = new ArrayList<>();
        for (Order o : orders) {

            if (!seen.contains(o.getOrderID())) {
                seen.add(o.getOrderID());
                uniqueOrder++;
            }
        }
        System.out.println(uniqueOrder);
        for (Order order : orders) {
            if (order.getOrderDate() == null) {
                System.out.println("Order " + order.getOrderID() + ": orderDate is null");
            }
            if (order.getSubTotal() == null) {
                System.out.println("Order " + order.getOrderID() + ": subTotal is null");
            }
            if (order.getTax() == null) {
                System.out.println("Order " + order.getOrderID() + ": tax is null");
            }
            if (order.getTip() == null) {
                System.out.println("Order " + order.getOrderID() + ": tip is null");
            }
            if (order.getTotal() == null) {
                System.out.println("Order " + order.getOrderID() + ": total is null");
            }
            if (order.getServer() == null) {
                System.out.println("Order " + order.getOrderID() + ": server is null");
            }
            if (order.getItems() == null) {
                System.out.println("Order " + order.getOrderID() + ": items list is null");
            } else if (order.getItems().isEmpty()) {
                System.out.println("Order " + order.getOrderID() + ": items list is empty");
            }
            if (order.getPayments() == null) {
                System.out.println("Order " + order.getOrderID() + ": payments list is null");
            } else if (order.getPayments().isEmpty()) {
                System.out.println("Order " + order.getOrderID() + ": payments list is empty");
            }
        }


    }

}