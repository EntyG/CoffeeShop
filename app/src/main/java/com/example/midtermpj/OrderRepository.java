package com.example.midtermpj;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {
    private static OrderRepository instance;
    private final List<Order> allOrders = new ArrayList<>();

    private OrderRepository() {
        // You can add dummy data here for testing
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void addOrder(Order order) {
        allOrders.add(order);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus() == status) {
                filteredList.add(order);
            }
        }
        return filteredList;
    }
}