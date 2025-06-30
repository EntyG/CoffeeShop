package com.example.midtermpj;

public class UserRepository {
    private static UserRepository instance;
    private final User currentUser;

    private UserRepository() {
        // Initialize with default user data
        currentUser = new User(
                "BroccoLee",
                "0338391647",
                "nhut6653@gmail.com",
                "Viet Nam"
        );
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