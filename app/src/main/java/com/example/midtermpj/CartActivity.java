package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalAmountTextCart);

        View backButton = findViewById(R.id.backCartButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        setupRecyclerView();
        updateTotalPrice();

        AppCompatButton checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            List<CartItem> items = CartRepository.getInstance().getCartItems();
            double totalPrice = CartRepository.getInstance().calculateTotalPrice();

            if (items.isEmpty()) {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userAddress = UserRepository.getInstance().getCurrentUser().getAddress();
            Order newOrder = new Order(new ArrayList<>(items), totalPrice, userAddress);
            OrderRepository.getInstance().addOrder(newOrder);
            CartRepository.getInstance().clearCart();

            Intent intent = new Intent(CartActivity.this, OrderSuccess.class);
            startActivity(intent);

            finish();
        });
    }

    private void setupRecyclerView() {
        cartItems = CartRepository.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                CartRepository.getInstance().removeItem(position);

                cartAdapter.notifyItemRemoved(position);

                updateTotalPrice();
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(cartRecyclerView);
    }

    public void updateTotalPrice() {
        double total = CartRepository.getInstance().calculateTotalPrice();
        totalPriceText.setText(String.format(Locale.US, "$%.2f", total));
    }
}