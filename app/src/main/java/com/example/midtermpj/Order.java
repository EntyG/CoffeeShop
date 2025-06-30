package com.example.midtermpj;

import java.util.Date;
import java.util.List;

public class Order {
    private final String orderId;
    private final List<CartItem> items;
    private final double totalPrice;
    private final Date orderDate;
    private final String shippingAddress;
    private OrderStatus status;

    public Order(List<CartItem> items, double totalPrice, String shippingAddress) {
        this.orderId = "ID" + System.currentTimeMillis(); // Simple unique ID
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.orderDate = new Date(); // Sets the order time to now
        this.status = OrderStatus.ONGOING; // All new orders start as "On going"
    }

    // Add Getters for all fields
    // Add a setStatus() method to move an order to history
    public String getOrderId() { return orderId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public Date getOrderDate() { return orderDate; }
    public String getShippingAddress() { return shippingAddress; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}