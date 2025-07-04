package com.example.midtermpj;

import com.google.firebase.firestore.Exclude; // <-- IMPORTANT: Import this

public class CartItem {
    private CoffeeProduct coffeeProduct;
    private String selectedOptionsDescription;
    private double finalPrice;
    private int quantity;
    private int productId;
    private String productName;
    public CartItem() {}

    public CartItem(CoffeeProduct coffeeProduct, int quantity, String selectedOptionsDescription, double finalPrice) {
        this.coffeeProduct = coffeeProduct;
        this.quantity = quantity;
        this.selectedOptionsDescription = selectedOptionsDescription;
        this.finalPrice = finalPrice;

        if (coffeeProduct != null) {
            this.productId = coffeeProduct.getId();
            this.productName = coffeeProduct.getTitle();
        }
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean sameItem(CartItem other) {
        return this.coffeeProduct.equals(other.coffeeProduct) &&
            this.selectedOptionsDescription.equals(other.selectedOptionsDescription);
    }
    @Exclude
    public void setCoffeeProduct(CoffeeProduct coffeeProduct) {
        this.coffeeProduct = coffeeProduct;
    }
    public String getSelectedOptionsDescription() { return selectedOptionsDescription; }
    public double getFinalPrice() { return finalPrice; }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }

    public void setProductId(int productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setSelectedOptionsDescription(String description) { this.selectedOptionsDescription = description; }
    public void setFinalPrice(double price) { this.finalPrice = price; }

    @Exclude
    public CoffeeProduct getCoffeeProduct() {
        return coffeeProduct;
    }
}