package com.example.midtermpj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameTextView = findViewById(R.id.textViewUserName);

        updateHeader();

        CoffeeRepository coffeeRepository = CoffeeRepository.getInstance();

        List<CoffeeProduct> coffeeList = coffeeRepository.getAllProducts();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCoffee);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        View cartButton = findViewById(R.id.imageViewCartIcon);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        View profileButton = findViewById(R.id.imageViewProfileIcon);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        CoffeeAdapter coffeeAdapter = new CoffeeAdapter(this, coffeeList);
        recyclerView.setAdapter(coffeeAdapter);

        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNav, R.id.nav_home);
    }

    private void setupBottomNavigation(BottomNavigationView bottomNav, int currentNavId) {
        bottomNav.setSelectedItemId(currentNavId);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == currentNavId) {
                return true;
            }

            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            } else if (itemId == R.id.nav_rewards) {
                intent = new Intent(getApplicationContext(), RewardActivity.class);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeader();
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void updateLoyaltyCard(View loyaltyCardRootView, int stampCount) {
        if (loyaltyCardRootView == null) return;

        TextView loyaltyStatusTextView = loyaltyCardRootView.findViewById(R.id.loyaltyStatusTextView);
        LinearLayout iconsContainer = loyaltyCardRootView.findViewById(R.id.loyalty_icons_container);

        loyaltyStatusTextView.setText(stampCount + " / 8");

        int totalIcons = iconsContainer.getChildCount();
        for (int i = 0; i < totalIcons; i++) {
            ImageView cupIcon = (ImageView) iconsContainer.getChildAt(i);
            if (cupIcon != null) {
                if (i < stampCount) {
                    cupIcon.setImageResource(R.drawable.coffee_icon);
                } else {
                    cupIcon.setImageResource(R.drawable.mt_coffee_icon);
                }
            }
        }
    }

    private void updateHeader() {
        User currentUser = UserRepository.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameTextView.setText(currentUser.getUserName());
            View loyaltyCard = findViewById(R.id.loyalty_card_view);
            updateLoyaltyCard(loyaltyCard, currentUser.getLoyaltyStamps());
        }
    }
}