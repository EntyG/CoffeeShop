package com.example.midtermpj;

public class User {
    private String username;
    private String phone;
    private String email;
    private String address;
    private int rewardPoints;
    private int loyaltyStamps;

    public User() {}

    public User(String uid, String name, String phone, String email, String address, int rewardPoints, int loyaltyStamps) {
        this.username = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rewardPoints = rewardPoints;
        this.loyaltyStamps = loyaltyStamps;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getLoyaltyStamps() {
        return loyaltyStamps;
    }

    public void setLoyaltyStamps(int loyaltyStamps) {
        this.loyaltyStamps = loyaltyStamps;
    }
}