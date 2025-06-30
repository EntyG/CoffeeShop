package com.example.midtermpj;

public class CoffeeProduct {
    private int id;
    private String title;
    private double basePrice;
    private int imageResource; // CHANGED from String picUrl to int imageResource
    private CustomizationOptions customizationOptions;

    // CONSTRUCTOR UPDATED
    public CoffeeProduct(int id, String title, double basePrice, int imageResource, CustomizationOptions options) {
        this.id = id;
        this.title = title;
        this.basePrice = basePrice;
        this.imageResource = imageResource; // Changed
        this.customizationOptions = options;
    }

    // GETTERS
    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getBasePrice() { return basePrice; }
    public CustomizationOptions getCustomizationOptions() { return customizationOptions; }

    // GETTER UPDATED
    public int getImageResource() { return imageResource; }
}