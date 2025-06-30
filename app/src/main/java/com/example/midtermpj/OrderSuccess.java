package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success);

        // Find the "Track My Order" button
        Button trackOrderButton = findViewById(R.id.trackOrderButton);

        // Set a listener on the button
        trackOrderButton.setOnClickListener(v -> {
            // Create an intent to navigate to the MyOrdersActivity
            Intent intent = new Intent(OrderSuccess.this, MyOrdersActivity.class);
            // Use CLEAR_TOP flag to make sure we don't stack activities unnecessarily
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            // IMPORTANT: Finish this activity so the user cannot navigate back to it
            finish();
        });
    }

    /**
     * Override the system back button to prevent going back to the empty cart.
     * Instead, we'll go to the main dashboard.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderSuccess.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
