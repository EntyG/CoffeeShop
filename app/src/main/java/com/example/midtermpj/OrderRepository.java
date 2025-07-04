package com.example.midtermpj;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class OrderRepository {
    private static OrderRepository instance;
    private final List<Order> allOrders = new ArrayList<>();

    private FirebaseFirestore db;
    private static final String TAG = "OrderRepository";
    private OrderRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnOrdersLoadListener {
        void onOrdersLoaded();
        void onOrdersLoadFailed(Exception e);
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void addOrder(Order order, final OnDocumentWriteListener callback) {
        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Order added successfully with ID: " + documentReference.getId());
                    // Add to the front of the local list to keep it updated
                    order.setOrderId(documentReference.getId());
                    allOrders.add(0, order);
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding order", e);
                    callback.onFailure(e);
                });
    }

    public void updateOrderStatusAndPointsEarned(Order order, String newStatus, int pointsEarned) {
        if (order.getOrderId() == null) {
            Log.e(TAG, "Cannot update order with null ID.");
            return;
        }

        db.collection("orders").document(order.getOrderId())
                .update("status", newStatus,
                        "pointsEarned", pointsEarned)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Order status updated successfully in Firestore."))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating order status", e));

        order.setStatus(newStatus);
    }

    public interface OnDocumentWriteListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : allOrders) {
            if (Objects.equals(order.getStatus(), status)) {
                filteredList.add(order);
            }
        }
        return filteredList;
    }

    public void loadInitialOrders(final OnOrdersLoadListener listener) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (currentUserId == null) return;

        db.collection("orders")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        allOrders.clear();

                        CoffeeRepository coffeeRepo = CoffeeRepository.getInstance();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);

                            if (order.getItems() != null) {
                                for (CartItem item : order.getItems()) {
                                    int productId = item.getProductId();

                                    CoffeeProduct fullProduct = coffeeRepo.getProductById(productId);

                                    if (fullProduct != null) {
                                        item.setCoffeeProduct(fullProduct);
                                    } else {
                                        Log.w(TAG, "Could not find product with ID: " + productId + " for an order.");
                                    }
                                }
                            }

                            allOrders.add(order);
                        }
                        Log.d(TAG, "Successfully loaded and rehydrated " + allOrders.size() + " orders.");
                        sortOrdersByDate();
                        listener.onOrdersLoaded();
                    } else {
                        Log.e(TAG, "Error loading orders: ", task.getException());
                        listener.onOrdersLoadFailed(task.getException());
                    }
                });
    }

    public void sortOrdersByDate() {
        allOrders.sort((o1, o2) -> {
            if (o1.getOrderDate() == null && o2.getOrderDate() == null) {
                return 0;
            }
            if (o1.getOrderDate() == null) {
                return 1;
            }
            if (o2.getOrderDate() == null) {
                return -1;
            }
            return o2.getOrderDate().compareTo(o1.getOrderDate());
        });
    }
}