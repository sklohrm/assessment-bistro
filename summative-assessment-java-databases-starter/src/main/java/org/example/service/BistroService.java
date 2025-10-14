package org.example.service;

import org.example.data.ItemRepo;
import org.example.data.OrderRepo;
import org.example.data.ServerRepo;
import org.example.data.TaxRepo;
import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.data.PaymentTypeRepo;
import org.example.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BistroService {
    private OrderRepo orders;
    private ServerRepo servers;
    private TaxRepo taxes;
    private PaymentTypeRepo paymentTypes;
    private ItemRepo items;

    public BistroService(OrderRepo orders, ServerRepo servers, TaxRepo taxes, PaymentTypeRepo paymentTypes, ItemRepo items) {
        this.orders = orders;
        this.servers = servers;
        this.taxes = taxes;
        this.paymentTypes = paymentTypes;
        this.items = items;
    }

    public List<Order> getAllOrders() throws InternalErrorException, RecordNotFoundException {
        return orders.getAllOrders();
    }

    public List<Server> getAllAvailableServers() throws InternalErrorException {
        return servers.getAllAvailableServers(LocalDate.now());
    }

    public Order getOrder(int id) throws RecordNotFoundException, InternalErrorException {
        return orders.getOrderById(id);
    }

    public Order addOrder(Order order) throws InternalErrorException, RecordNotFoundException {
        calculateOrderTotals(order);
        order = orders.addOrder(order);
        return order;
    }

    public Order deleteOrder(int id) throws InternalErrorException {
        return orders.deleteOrder(id);
    }

    public void calculateOrderTotals(Order order) throws RecordNotFoundException, InternalErrorException {
        BigDecimal subTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (OrderItem oi : order.getItems()) {
            subTotal = subTotal.add(oi.getPrice().multiply(new BigDecimal(oi.getQuantity())));
        }
        Tax currentTax = taxes.getCurrentTax(order.getOrderDate().toLocalDate());

        BigDecimal taxAmount = subTotal.multiply(currentTax.getTaxPercentage().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal total = subTotal.add(taxAmount).add(order.getTip()).setScale(2, RoundingMode.HALF_EVEN);

        order.setSubTotal(subTotal);
        order.setTax(taxAmount);
        order.setTotal(total);
    }

    public Order updateOrder(Order order) throws RecordNotFoundException, InternalErrorException {
        calculateOrderTotals(order);
        orders.updateOrder(order);
        return order;
    }

    public List<PaymentType> getPaymentTypes() throws InternalErrorException {
        return paymentTypes.getAll();
    }

    public List<Item> getAllItems() throws InternalErrorException {
        return items.getAllAvailableItems(LocalDate.now());
    }

    public List<Item> getAllItemsByCategory(int itemCategoryID) throws InternalErrorException {
        return items.getItemsByCategory(LocalDate.now(), itemCategoryID);
    }

    public List<ItemCategory> getAllItemCategories() throws InternalErrorException {
        return items.getAllItemCategories();
    }

    public Item getItem(int id) throws RecordNotFoundException, InternalErrorException {
        return items.getItemById(id);
    }

    public Server getServerByID (int id) throws RecordNotFoundException, InternalErrorException {
        return servers.getServerById(id);
    }

    public List<PaymentType> getAllPaymentTypes() throws InternalErrorException {
        return paymentTypes.getAll();
    }

    public Order getNewOrder() {
        Order result = new Order();
        result.setItems(new ArrayList<>());
        result.setPayments(new ArrayList<>());
        result.setOrderDate(LocalDateTime.now());
        result.setSubTotal(new BigDecimal(0));
        result.setTax(new BigDecimal(0));
        result.setTip(new BigDecimal(0));
        result.setTotal(new BigDecimal(0));
        return result;
    }
}
