package com.example.midtermpj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

// The activity is now much cleaner and only handles UI.
public class MainActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameTextView = findViewById(R.id.textViewUserName);

        // --- 2. Call the update method for the initial setup ---
        updateGreeting();

        // 1. Get the repository instance (our single source of truth for data)
        CoffeeRepository coffeeRepository = CoffeeRepository.getInstance();

        // 2. Fetch the list of products
        //    (Later, the repository will load this from the JSON file)
        List<CoffeeProduct> coffeeList = coffeeRepository.getAllProducts();

        // 3. Find the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCoffee);

        // 4. Set the Layout Manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        View cartButton = findViewById(R.id.imageViewCartIcon);cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        View profileButton = findViewById(R.id.imageViewProfileIcon);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // 5. Create and set the adapter
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
                // User clicked the icon for the current screen. Do nothing.
                return true;
            }

            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            } else if (itemId == R.id.nav_rewards) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
            }

            if (intent != null) {
                // --- THIS IS THE KEY FIX ---
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // --- END OF FIX ---
                startActivity(intent);
                // Optional: Remove the screen-changing animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the greeting in case the user name was changed
        updateGreeting();
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void updateGreeting() {
        // Get the current user from our central repository
        User currentUser = UserRepository.getInstance().getCurrentUser();
        if (currentUser != null && userNameTextView != null) {
            // Set the updated name in the TextView
            userNameTextView.setText(currentUser.getName());
        }
    }
}