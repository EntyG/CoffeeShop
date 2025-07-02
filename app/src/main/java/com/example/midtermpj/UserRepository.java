package com.example.midtermpj;

public class UserRepository {
    private static UserRepository instance;
    private final User currentUser;

    private UserRepository() {
        currentUser = new User(
                "BroccoLee",
                "0338391647",
                "nhut6653@gmail.com",
                "Viet Nam"
        );

        currentUser.setRewardPoints(2750);
        currentUser.setLoyaltyStamps(4);
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}