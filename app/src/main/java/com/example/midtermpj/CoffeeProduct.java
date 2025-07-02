package com.example.midtermpj;

public class CoffeeProduct {
    private int id;
    private String title;
    private double basePrice;
    private int imageResource;
    private CustomizationOptions customizationOptions;

    public CoffeeProduct(int id, String title, double basePrice, int imageResource, CustomizationOptions options) {
        this.id = id;
        this.title = title;
        this.basePrice = basePrice;
        this.imageResource = imageResource;
        this.customizationOptions = options;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public CustomizationOptions getCustomizationOptions() {
        return customizationOptions;
    }

    public int getImageResource() {
        return imageResource;
    }
}