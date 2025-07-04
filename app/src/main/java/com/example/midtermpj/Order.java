package com.example.midtermpj;

import com.google.firebase.firestore.DocumentId; // <-- Import
import com.google.firebase.firestore.ServerTimestamp; // <-- Import
import java.util.Date;
import java.util.List;

public class Order {
    private List<CartItem> items;
    private double totalPrice;
    private String shippingAddress;
    private String status;
    private int pointsEarned;

    @DocumentId
    private String orderId;
    private Date orderDate;

    private String userId;

    public Order() {}

    public Order(List<CartItem> items, double totalPrice, String shippingAddress) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.orderDate = new Date();
        this.status = "ONGOING";
    }

    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getShippingAddress() { return shippingAddress; }
    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    public String getOrderId() { return orderId; }
    public Date getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public String getUserId() { return userId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setStatus(String status) { this.status = status; }
    public void setUserId(String userId) { this.userId = userId; }
}