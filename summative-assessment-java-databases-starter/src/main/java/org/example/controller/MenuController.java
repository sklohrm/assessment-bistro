package org.example.controller;

import org.example.data.exceptions.InternalErrorException;
import org.example.data.exceptions.RecordNotFoundException;
import org.example.model.*;
import org.example.service.BistroService;
import org.example.view.IO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuController {
    private static final int CHOICE_DISPLAY_ORDERS = 1;
    private static final int CHOICE_REMOVE_ORDER = 2;
    private static final int CHOICE_ADD_ORDER = 3;
    private static final int CHOICE_EDIT_ORDER = 4;
    private static final int CHOICE_EXIT = 5;
    private static final int CHOICE_MIN = 1;
    private static final int CHOICE_MAX = 5;

    private static final int CHOICE_SELECT_SERVER = 1;
    private static final int CHOICE_ADD_ITEM = 2;
    private static final int CHOICE_EDIT_ITEM = 3;
    private static final int CHOICE_ADD_TIP = 4;
    private static final int CHOICE_ADD_PAYMENT = 5;
    private static final int CHOICE_EDIT_PAYMENT = 6;
    private static final int CHOICE_SAVE = 7;
    private static final int CHOICE_CANCEL = 8;


    private IO io;
    private BistroService svc;

    @Autowired
    public MenuController(IO io, BistroService svc) {
        this.io = io;
        this.svc = svc;
    }

    public void run() {
        boolean running = true;

        while (running) {
            int choice = getMenuChoice();
            switch (choice) {
                case CHOICE_DISPLAY_ORDERS:
                    displayOrders();
                    break;
                case CHOICE_REMOVE_ORDER:
                    removeOrder();
                    break;
                case CHOICE_ADD_ORDER:
                    addOrder();
                    break;
                case CHOICE_EDIT_ORDER:
                    editOrder();
                    break;
                case CHOICE_EXIT:
                    running = false;
                    break;
                default:
                    io.displayMessage("Invalid menu selection");
            }
        }
    }

    private void addOrder() {
        io.displayTitle("Add Order");
        Order order = svc.getNewOrder();
        selectServer(order);

        editOrder(order);
    }

    private void editOrder() {
        int orderId = io.getInt("Enter order ID");
        try {
            Order order = svc.getOrder(orderId);
            editOrder(order);
        } catch (RecordNotFoundException e) {
            io.displayMessage("Order not found");
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred");
        }
    }

    private void editOrder(Order order) {
        boolean continueRunning = true;
        while (continueRunning) {
            try {
                svc.calculateOrderTotals(order);
            } catch (Exception e) {
                io.displayMessage("A database error has occurred.  Canceling order. " + e.getLocalizedMessage());
                return;
            }

            io.displayOrder(order);
            int option = displayAndChooseOrderEditMenu(order);
            switch (option) {
                case CHOICE_SELECT_SERVER:
                    selectServer(order);
                    break;
                case CHOICE_ADD_ITEM:
                    addItem(order);
                    break;
                case CHOICE_EDIT_ITEM:
                    editItem(order);
                    break;
                case CHOICE_ADD_TIP:
                    addTip(order);
                    break;
                case CHOICE_ADD_PAYMENT:
                    addPayment(order);
                    break;
                case CHOICE_EDIT_PAYMENT:
                    editPayment(order);
                    break;
                case CHOICE_SAVE:
                    saveOrder(order);
                    continueRunning = false;
                    break;
                case CHOICE_CANCEL:
                    continueRunning = false;
                    break;
                default:
                    io.displayMessage("Invalid menu selection");
            }
        }
    }


    private void saveOrder(Order order) {
        try {
            if (order.getOrderID() > 0) {
                svc.updateOrder(order);
                io.displayMessage("Saved order #" + order.getOrderID());
            } else {
                Order output = svc.addOrder(order);
                io.displayMessage("Created order #" + output.getOrderID());
            }
        } catch (Exception e) {
            io.displayMessage("A database error has occurred.  Your changes have not been saved.");
        }
    }

    private void addTip(Order order) {
        order.setTip(io.getMoney("Enter amount of tip"));
    }

    private void editItem(Order order) {

        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem oi = order.getItems().get(i);
            io.displayMenuChoice(i + 1, oi.getItem().getItemName() + " (" + oi.getQuantity() + ")");
        }
        io.displayMenuChoice(0, "Cancel");

        int choice = io.getInt("Select an item");
        choice--;
        if (choice < 0 || choice >= order.getItems().size()) {
            io.displayMessage("Action canceled.");
            return;
        }

        OrderItem oi = order.getItems().get(choice);
        io.displayMessage("Selected:  " + oi.getItem().getItemName() + " (" + oi.getQuantity() + ")");
        int newQty = io.getInt("Enter new quantity");
        if (newQty <= 0) {
            io.displayMessage("Deleted " + oi.getItem().getItemName() + " from order");
            order.getItems().remove(oi);
        } else {
            io.displayMessage(oi.getItem().getItemName() + " updated");
            order.getItems().get(choice).setQuantity(newQty);
        }
    }

    private void addItem(Order order) {
        try {
            List<ItemCategory> categories = svc.getAllItemCategories();
            int category = 0;
            for (ItemCategory cat : categories) {
                io.displayMessage(String.format("%3d - %s", cat.getItemCategoryID(), cat.getItemCategoryName()));
            }
            category = io.getInt("Select a category");
            for (Item i : svc.getAllItemsByCategory(category)) {
                io.displayMessage(String.format("%3d - %s", i.getItemID(), i.getItemName()));
            }
            int itemId = io.getInt("Select an item");
            int quantity = io.getInt("Enter a quantity");

            OrderItem oi = new OrderItem();
            oi.setItem(svc.getItem(itemId));
            oi.setQuantity(quantity);
            oi.setPrice(oi.getItem().getUnitPrice());
            oi.setItemID(itemId);
            order.getItems().add(oi);
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred: " + e.getLocalizedMessage());
        } catch (RecordNotFoundException e) {
            io.displayMessage("Item not found.");
        }
    }

    private void addPayment(Order order) {
        try {
            List<PaymentType> paymentTypes = svc.getAllPaymentTypes();
            int paymentTypeID = 0;
            for (PaymentType pt : paymentTypes) {
                io.displayMessage(String.format("%3d - %s", pt.getPaymentTypeID(), pt.getPaymentTypeName()));
            }
            paymentTypeID = io.getInt("Select a payment type");
            BigDecimal amount = io.getMoney("Enter amount");
            PaymentType pt = null;
            for (PaymentType paymentType : paymentTypes) {
                if (paymentType.getPaymentTypeID() == paymentTypeID) {
                    pt = paymentType;
                    break;
                }
            }

            Payment payment = new Payment();
            payment.setPaymentTypeID(paymentTypeID);
            payment.setPaymentType(pt);
            payment.setAmount(amount);

            order.getPayments().add(payment);
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred: " + e.getLocalizedMessage());
        }
    }

    private void editPayment(Order order) {
        for (int i = 0; i < order.getPayments().size(); i++) {
            Payment p = order.getPayments().get(i);
            io.displayMenuChoice(i + 1, io.asCurrency(p.getAmount().doubleValue()) + " (" + p.getPaymentType().getPaymentTypeName() + ")");
        }
        io.displayMenuChoice(0, "Cancel");

        int choice = io.getInt("Select an item");
        choice--;
        if (choice < 0 || choice >= order.getItems().size()) {
            io.displayMessage("Action canceled.");
            return;
        }

        Payment p = order.getPayments().get(choice);
        io.displayMessage("Selected:  " + io.asCurrency(p.getAmount().doubleValue()) + " (" + p.getPaymentType().getPaymentTypeName() + ")");
        BigDecimal newAmount = io.getMoney("Enter new amount");
        if (newAmount.doubleValue() <= 0) {
            io.displayMessage("Deleted payment from order");
            order.getItems().remove(p);
        } else {
            io.displayMessage("Payment updated");
            order.getPayments().get(choice).setAmount(newAmount);
        }
    }

    private void selectServer(Order order) {
        try {
            List<Server> servers = svc.getAllAvailableServers();

            for (Server server : servers) {
                io.displayMenuChoice(server.getServerID(), server.getFirstName() + " " + server.getLastName());
            }

            int serverID = io.getInt("Choose a server ");
            Server server = svc.getServerByID(serverID);
            order.setServerID(serverID);
            order.setServer(server);
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred.");
        } catch (RecordNotFoundException e) {
            io.displayMessage("Server not found.");
        }
    }

    private int displayAndChooseOrderEditMenu(Order order) {
        if (order.getOrderID() > 0) {
            io.displayTitle("Editing Order #" + order.getOrderID());
        } else {
            io.displayTitle("Creating New Order");
        }
        io.displayMenuChoice(CHOICE_SELECT_SERVER, "Change Server");
        io.displayMenuChoice(CHOICE_ADD_ITEM, "Add an item");
        io.displayMenuChoice(CHOICE_EDIT_ITEM, "Edit an item");
        io.displayMenuChoice(CHOICE_ADD_TIP, "Add/Update tip");
        io.displayMenuChoice(CHOICE_ADD_PAYMENT, "Add Payment");
        io.displayMenuChoice(CHOICE_EDIT_PAYMENT, "Edit Payment");
        io.displayMenuChoice(CHOICE_SAVE, "Save");
        io.displayMenuChoice(CHOICE_CANCEL, "Cancel");

        return io.getInt("Select a choice:");
    }

    private void removeOrder() {
        int choice = io.getInt("Enter order ID to delete");

        try {
            Order order = svc.getOrder(choice);
            String confirm = io.getString("Delete order #" + order.getOrderID() + "? (y/N)");
            if (confirm.equals("y")) {
                svc.deleteOrder(order.getOrderID());
            } else {
                io.displayMessage("Action canceled.");
            }
        } catch (RecordNotFoundException e) {
            io.displayMessage("Order not found.");
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred.");
        }

    }

    private void displayOrders() {
        io.displayTitle("Order Display");
        int id = io.getInt("Enter an order number to display, or 0 to show a summary of all orders: ");
        try {
            if (id == 0) {
                List<Order> orders = svc.getAllOrders();
                io.displayOrders(orders);
            } else {
                Order order = svc.getOrder(id);
                io.displayOrder(order);
            }
        } catch (RecordNotFoundException e) {
            io.displayMessage("Order not found.");
        } catch (InternalErrorException e) {
            io.displayMessage("A database error has occurred.");
        }

    }

    private int getMenuChoice() {
        io.displayTitle("Main Menu");
        io.displayMessage("  1. Display Orders");
        io.displayMessage("  2. Remove an Order");
        io.displayMessage("  3. Add an Order");
        io.displayMessage("  4. Edit an Order");
        io.displayMessage("  5. Exit");
        io.displayMessage("");
        return io.getIntRequiredRange("Select", CHOICE_MIN, CHOICE_MAX);

    }
}
