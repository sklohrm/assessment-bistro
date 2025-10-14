package org.example.service;

import org.example.data.OrderRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BistroServiceTest {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BistroService svc;

    @Test
    void testCalculateOrderTotals() throws RecordNotFoundException, InternalErrorException {
        Order expected = orderRepo.getOrderById(1);
        Order actual = orderRepo.getOrderById(1);

        svc.calculateOrderTotals(actual);
        assertEquals(expected, actual);
    }
}