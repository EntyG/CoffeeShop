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

    private int pointsEarned;

    public Order(List<CartItem> items, double totalPrice, String shippingAddress) {
        this.orderId = "ID" + System.currentTimeMillis();
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.orderDate = new Date();
        this.status = OrderStatus.ONGOING;
    }

    public String getOrderId() { return orderId; }

    public List<CartItem> getItems() { return items; }

    public double getTotalPrice() { return totalPrice; }

    public Date getOrderDate() { return orderDate; }

    public String getShippingAddress() { return shippingAddress; }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}