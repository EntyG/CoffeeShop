package com.example.midtermpj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

        filterAndDisplayOrders("ONGOING");

        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNav, R.id.nav_orders);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.selectTab(tabLayout.getTabAt(0));
        filterAndDisplayOrders("ONGOING");
        bottomNav.setSelectedItemId(R.id.nav_orders);
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

    private void setupRecyclerView() {
        adapter = new OrderAdapter(this, displayedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                Order orderToMove = displayedOrders.get(position);
                if (selectedTabPosition == 0) {
                    processRewardsForCompletedOrder(orderToMove);
                    OrderRepository.getInstance().updateOrderStatusAndPointsEarned(orderToMove, "HISTORY", (int) (orderToMove.getTotalPrice() * 10));

                    displayedOrders.remove(position);
                    adapter.notifyItemRemoved(position);

                    Toast.makeText(MyOrdersActivity.this, "Order moved to History", Toast.LENGTH_SHORT).show();
                } else if (selectedTabPosition == 1) {
                    showReorderDialog(position);
                    adapter.notifyItemChanged(position);
                }
            }

            private void processRewardsForCompletedOrder(Order order) {
                User currentUser = UserRepository.getInstance().getCurrentUser();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                int pointsFromOrder = (int) (order.getTotalPrice() * 10);

                int newStampCount = currentUser.getLoyaltyStamps() + 1;

                order.setPointsEarned(pointsFromOrder);

                int bonusPoints = 0;
                if (newStampCount >= 8) {
                    bonusPoints = 1000;
                    newStampCount = 0;
                }

                int finalPoints = currentUser.getRewardPoints() + pointsFromOrder + bonusPoints;

                currentUser.setRewardPoints(finalPoints);
                currentUser.setLoyaltyStamps(newStampCount);
                updateFieldInFirestore(currentUserId, "rewardPoints", finalPoints);
                updateFieldInFirestore(currentUserId, "loyaltyStamps", newStampCount);
            }

            private void updateFieldInFirestore(String currentUserId, String field, Object value) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(currentUserId).update(field, value);
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void showReorderDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Reorder")
                .setMessage("Would you like to add all items from this order back to your cart?")
                .setPositiveButton("Yes, Reorder", (dialog, which) -> {
                    reorderFromHistory(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void reorderFromHistory(int position) {
        if (position >= displayedOrders.size()) return;

        Order orderToReorder = displayedOrders.get(position);
        if (orderToReorder.getTotalPrice() == 0) {
            Toast.makeText(this, "Can not reorder a free order.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<CartItem> itemsToReorder = orderToReorder.getItems();

        if (itemsToReorder == null || itemsToReorder.isEmpty()) {
            Toast.makeText(this, "This order has no items to reorder.", Toast.LENGTH_SHORT).show();
            return;
        }

        CartRepository cartRepo = CartRepository.getInstance();
        for (CartItem item : itemsToReorder) {
            if(item.getCoffeeProduct() != null) {
                cartRepo.addItem(item);
            }
        }

        Toast.makeText(this, "Items added back to your cart!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(MyOrdersActivity.this, CartActivity.class));
    }


    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("On going"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    filterAndDisplayOrders("ONGOING");
                } else {
                    filterAndDisplayOrders("HISTORY");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterAndDisplayOrders(String status) {
        List<Order> filteredOrders = OrderRepository.getInstance().getOrdersByStatus(status);
        displayedOrders.clear();
        displayedOrders.addAll(filteredOrders);
        adapter.notifyDataSetChanged();
    }
}