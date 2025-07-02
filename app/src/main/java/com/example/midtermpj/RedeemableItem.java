package com.example.midtermpj;

public class RedeemableItem {
    private final String title;
    private final String validUntil;
    private final int pointCost;
    private final int imageResource;

    public RedeemableItem(String title, String validUntil, int pointCost, int imageResource) {
        this.title = title;
        this.validUntil = validUntil;
        this.pointCost = pointCost;
        this.imageResource = imageResource;
    }

    public String getTitle() { return title; }

    public String getValidUntil() { return validUntil; }

    public int getPointCost() { return pointCost; }

    public int getImageResource() { return imageResource; }
}