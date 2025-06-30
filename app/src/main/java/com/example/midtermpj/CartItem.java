package com.example.midtermpj;

public class CartItem {
    private final CoffeeProduct coffeeProduct;
    private int quantity;
    private final String selectedOptionsDescription; // e.g., "single | iced | medium"
    private final double finalPrice; // Price for one item with options

    public CartItem(CoffeeProduct coffeeProduct, int quantity, String selectedOptionsDescription, double finalPrice) {
        this.coffeeProduct = coffeeProduct;
        this.quantity = quantity;
        this.selectedOptionsDescription = selectedOptionsDescription;
        this.finalPrice = finalPrice;
    }

    // Getters
    public CoffeeProduct getCoffeeProduct() { return coffeeProduct; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean sameItem(CartItem other) {
        return this.coffeeProduct.equals(other.coffeeProduct) &&
                this.selectedOptionsDescription.equals(other.selectedOptionsDescription);
    }
    public String getSelectedOptionsDescription() { return selectedOptionsDescription; }
    public double getFinalPrice() { return finalPrice; }
}