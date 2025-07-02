package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class RewardActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private User currentUser;

    private RecyclerView rewardsRecyclerView;
    private RewardHistoryAdapter historyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        currentUser = UserRepository.getInstance().getCurrentUser();
        if (currentUser != null) {
            updateLoyaltyCardAndPoints(findViewById(R.id.loyalty_card_view), findViewById(R.id.my_points_card_view), currentUser.getLoyaltyStamps(), currentUser.getRewardPoints());
        }

        rewardsRecyclerView = findViewById(R.id.rewardsRecyclerView);

        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNav, R.id.nav_rewards);

        setupRecyclerView();

        View redeemButton = findViewById(R.id.redeemButton);
        redeemButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, RedeemActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        List<Order> completedOrders = OrderRepository.getInstance().getOrdersByStatus(OrderStatus.HISTORY);

        historyAdapter = new RewardHistoryAdapter(completedOrders);
        rewardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rewardsRecyclerView.setAdapter(historyAdapter);

        rewardsRecyclerView.setNestedScrollingEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_rewards);
        currentUser = UserRepository.getInstance().getCurrentUser();
        if (currentUser != null) {
            updateLoyaltyCardAndPoints(findViewById(R.id.loyalty_card_view), findViewById(R.id.my_points_card_view), currentUser.getLoyaltyStamps(), currentUser.getRewardPoints());
        }
        setupRecyclerView();
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

    private void updateLoyaltyCardAndPoints(View loyaltyCardRootView, View pointsCardRootView, int stampCount, int points) {
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

        if (pointsCardRootView == null) return;

        TextView pointsStatusTextView = pointsCardRootView.findViewById(R.id.myPointsTextView);
        pointsStatusTextView.setText(String.valueOf(points));
    }
}
