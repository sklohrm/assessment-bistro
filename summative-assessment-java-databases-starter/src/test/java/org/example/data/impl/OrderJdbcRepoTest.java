package org.example.data.impl;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {OrderJdbcRepo.class})
class OrderJdbcRepoTest {

    @Autowired
    OrderJdbcRepo repo;

    Order order1;

    final int ORDERS_COUNT = 365;

    @BeforeEach
    void setup() {
        order1 = new Order();
        order1.setOrderID(1);
        order1.setOrderDate(LocalDateTime.of(2022, 5, 3, 0, 0, 0));
        order1.setSubTotal(new BigDecimal("32.00"));
        order1.setTax(new BigDecimal("2.00"));
        order1.setTip(new BigDecimal("4.80"));
        order1.setTotal(new BigDecimal("38.80"));
        order1.setServerID(8);
    }

    // findAll() tests
    @Test
    void findAll_shouldFindAll() throws Exception {
        assertEquals(ORDERS_COUNT, repo.getAllOrders().size());
    }

    // findByID() tests
    @Test
    void findByID_shouldFindByID() throws Exception {
        Order actual = repo.getOrderById(order1.getOrderID());
        assertNotNull(actual);
        assertEquals(order1.getOrderID(), actual.getOrderID());
    }

    @Test
    void findByID_shouldThrowRecordNotFoundWhenListEmpty() throws Exception {
        assertThrows(RecordNotFoundException.class, () -> repo.getOrderById(-999));
    }

    // addOrder() tests
    @Test
    void addOrder_shouldAddOrder() throws Exception {
        Order actual = repo.addOrder(order1);
        order1.setOrderID(actual.getOrderID());
        assertEquals(order1, actual);
    }

    // updateOrder() tests
    @Test
    void updateOrder_shouldUpdate() throws Exception {
        order1.setTotal(new BigDecimal("999.99"));
        repo.updateOrder(order1);
        assertEquals(new BigDecimal("999.99"), repo.getOrderById(1).getTotal());
    }

    // deleteOrder() tests
    @Test
    void deleteOrder_shouldDelete() throws InternalErrorException, RecordNotFoundException {
        Order order;
        order = repo.deleteOrder(1);
        assertThrows(RecordNotFoundException.class, () -> repo.getOrderById(1));
    }

    @Test
    void deleteOrder_shouldThrow() throws Exception {
        assertThrows(InternalErrorException.class, () -> repo.deleteOrder(-1));
    }

}