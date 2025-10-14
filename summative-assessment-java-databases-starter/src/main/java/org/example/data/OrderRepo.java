package org.example.data;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.Order;

import java.util.List;

public interface OrderRepo {
    public Order getOrderById(int id) throws RecordNotFoundException, InternalErrorException;
    public List<Order> getAllOrders() throws InternalErrorException, RecordNotFoundException;
    public Order addOrder(Order order) throws InternalErrorException;
    public void updateOrder(Order order) throws InternalErrorException;
    public Order deleteOrder(int id) throws InternalErrorException;
}
