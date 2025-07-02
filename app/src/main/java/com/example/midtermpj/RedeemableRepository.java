package com.example.midtermpj;

import java.util.ArrayList;
import java.util.List;

public class RedeemableRepository {
    private static RedeemableRepository instance;
    private final List<RedeemableItem> redeemableItems = new ArrayList<>();

    private RedeemableRepository() {
        redeemableItems.add(new RedeemableItem("Americano", "Valid until 04.07.21", 1340, R.drawable.americano));
        redeemableItems.add(new RedeemableItem("Flat White", "Valid until 04.07.21", 1340, R.drawable.flatwhite));
        redeemableItems.add(new RedeemableItem("Cappuccino", "Valid until 04.07.21", 1340, R.drawable.cappuccino));
    }

    public static synchronized RedeemableRepository getInstance() {
        if (instance == null) {
            instance = new RedeemableRepository();
        }
        return instance;
    }

    public List<RedeemableItem> getRedeemableItems() {
        return redeemableItems;
    }
}