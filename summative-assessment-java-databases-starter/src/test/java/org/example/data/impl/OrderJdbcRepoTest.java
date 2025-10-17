package org.example.data.impl;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;



import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {OrderJdbcRepo.class})
class OrderJdbcRepoTest {

    @Autowired
    OrderJdbcRepo repo;

    Order order1;
    Item item1;
    Item item2;
    OrderItem oi1;
    OrderItem oi2;
    PaymentType pt;
    Payment p1;
    Payment p2;
    Server server;
    Order newOrder;

    final int ORDERS_COUNT = 366;

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

        item1 = new Item();
        item1.setItemID(19);
        item1.setItemCategoryID(5);
        item1.setItemName("Chicken Sandwich");
        item1.setItemDescription("Fried chicken breast with mayo, tomato, cheddar, and lettuce");
        item1.setStartDate(LocalDate.of(2020, 1, 1));
        item1.setEndDate(null);
        item1.setUnitPrice(new BigDecimal("14.00"));

        item2 = new Item();
        item2.setItemID(28);
        item2.setItemCategoryID(6);
        item2.setItemDescription("Fresh, seasonal fruit from the local farmer's market");
        item2.setItemName("Fruit Bowl");
        item2.setStartDate(LocalDate.of(2022, 1, 1));
        item2.setEndDate(null);
        item2.setUnitPrice(new BigDecimal("6.00"));

        oi1 = new OrderItem();
        oi1.setItemID(item1.getItemID());
        oi1.setQuantity(2);
        oi1.setPrice(item1.getUnitPrice().multiply(new BigDecimal("2.0")));
        oi1.setItem(item1);

        oi2 = new OrderItem();
        oi2.setItemID(item2.getItemID());
        oi2.setQuantity(3);
        oi2.setPrice(item2.getUnitPrice().multiply(new BigDecimal("3.0")));
        oi2.setItem(item2);

        pt = new PaymentType();
        pt.setPaymentTypeID(3);
        pt.setPaymentTypeName("Mastercard");

        p1 = new Payment();
        p1.setPaymentTypeID(pt.getPaymentTypeID());
        p1.setAmount(new BigDecimal("0.0"));
        p1.setPaymentType(pt);

        p2 = new Payment();
        p2.setPaymentTypeID(pt.getPaymentTypeID());
        p2.setAmount(new BigDecimal("0.0"));
        p2.setPaymentType(pt);

        server = new Server();
        server.setServerID(1);
        server.setFirstName("Mersey");
        server.setLastName("Giacometti");
        server.setHireDate(LocalDate.of(2020, 2, 27));
        server.setTermDate(null);

        newOrder = new Order();
        newOrder.setServerID(server.getServerID());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setSubTotal(new BigDecimal("32.00"));
        newOrder.setTax(new BigDecimal("2.00"));
        newOrder.setTip(new BigDecimal("4.80"));
        newOrder.setTotal(new BigDecimal("38.80"));

        newOrder.setServer(server);

        newOrder.setItems(new ArrayList<>());
        newOrder.getItems().add(oi1);
        newOrder.getItems().add(oi2);

        newOrder.setPayments(new ArrayList<>());
        newOrder.getPayments().add(p1);
        newOrder.getPayments().add(p2);

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
        newOrder = repo.addOrder(newOrder);
        newOrder.getItems().clear();
        newOrder.getItems().add(oi1);
        repo.updateOrder(newOrder);
        Order result = repo.getOrderById(newOrder.getOrderID());
        assertEquals(1, result.getItems().size());
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

    @Test
    void addOrder_shouldAddToAllRelevantTables() throws Exception{



        repo.addOrder(newOrder);

        Order result = repo.getOrderById(newOrder.getOrderID());

        assertNotNull(result);
        assertTrue(result.getOrderID() > 0);

        assertEquals(server.getServerID(), result.getServerID());
        assertEquals(newOrder.getSubTotal(), result.getSubTotal());
        assertEquals(newOrder.getTax(), result.getTax());
        assertEquals(newOrder.getTip(), result.getTip());
        assertEquals(newOrder.getTotal(), result.getTotal());

        assertNotNull(result.getServer());
        assertEquals(server.getServerID(), result.getServer().getServerID());
        assertEquals(server.getFirstName(), result.getServer().getFirstName());
        assertEquals(server.getLastName(), result.getServer().getLastName());
        assertEquals(server.getHireDate(), result.getServer().getHireDate());
        assertEquals(server.getTermDate(), result.getServer().getTermDate());

        assertNotNull(result.getItems());
        assertEquals(2, result.getItems().size());

        OrderItem r1 = result.getItems().get(0);
        OrderItem r2 = result.getItems().get(1);

        assertNotNull(result.getPayments());
        assertEquals(2, result.getPayments().size());
        for (Payment pay : result.getPayments()) {
            assertTrue(pay.getPaymentID() > 0);
            assertEquals(pt.getPaymentTypeID(), pay.getPaymentTypeID());
            assertEquals(new BigDecimal("0.00"), pay.getAmount());
            assertNotNull(pay.getPaymentType());
            assertEquals(pt.getPaymentTypeName(), pay.getPaymentType().getPaymentTypeName());
        }
    }

}