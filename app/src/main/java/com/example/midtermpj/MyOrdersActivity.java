package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> displayedOrders = new ArrayList<>();

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        tabLayout = findViewById(R.id.ordersTabLayout);
        recyclerView = findViewById(R.id.ordersRecyclerView);

        setupRecyclerView();
        setupTabLayout();

        // Load the initial "On going" list
        filterAndDisplayOrders(OrderStatus.ONGOING);

        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNav, R.id.nav_orders);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the greeting in case the user name was changed
        bottomNav.setSelectedItemId(R.id.nav_orders);
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

    private void setupRecyclerView() {
        adapter = new OrderAdapter(this, displayedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // --- IMPLEMENT SWIPE-TO-DELETE HERE ---
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We don't need drag-and-drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the swiped item
                int position = viewHolder.getAdapterPosition();

                // Get the Order object from our displayed list
                Order orderToMove = displayedOrders.get(position);

                // --- THE CORE LOGIC ---
                // 1. Change the order's status to HISTORY
                orderToMove.setStatus(OrderStatus.HISTORY);

                // 2. Remove the item from the *currently visible list*
                displayedOrders.remove(position);

                // 3. Notify the adapter that the item has been removed from the view
                adapter.notifyItemRemoved(position);

                // 4. (Optional) Show a confirmation message
                Toast.makeText(MyOrdersActivity.this, "Order moved to History", Toast.LENGTH_SHORT).show();
            }

            /**
             * --- IMPORTANT IMPROVEMENT ---
             * This method disables the swipe gesture on the "History" tab.
             */
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // Check which tab is currently selected
                if (tabLayout.getSelectedTabPosition() == 0) {
                    // If on the "On going" tab, allow swiping to the left
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    // If on the "History" tab, disable swiping by returning 0
                    return 0;
                }
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("On going"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    filterAndDisplayOrders(OrderStatus.ONGOING);
                } else {
                    filterAndDisplayOrders(OrderStatus.HISTORY);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
            // ... onTabUnselected and onTabReselected can be left empty
        });
    }

    private void filterAndDisplayOrders(OrderStatus status) {
        // Get the filtered list from the repository
        List<Order> filteredOrders = OrderRepository.getInstance().getOrdersByStatus(status);

        // Update the adapter's data
        displayedOrders.clear();
        displayedOrders.addAll(filteredOrders);
        adapter.notifyDataSetChanged();
    }
}