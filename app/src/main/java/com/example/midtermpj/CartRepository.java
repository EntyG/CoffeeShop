package com.example.midtermpj;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static CartRepository instance;
    private final List<CartItem> cartItems = new ArrayList<>();

    private CartRepository() {}

    public static synchronized CartRepository getInstance() {
        if (instance == null) {
            instance = new CartRepository();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        // Check if the item already exists in the cart

        boolean itemExists = false;
        for (CartItem existingItem : cartItems) {
            if (existingItem.sameItem(item)) {
                itemExists = true;
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                break;
            }
        }

        if (!itemExists) {
            cartItems.add(item);
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getFinalPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}