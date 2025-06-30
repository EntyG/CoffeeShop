package com.example.midtermpj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midtermpj.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    // The duration of the splash screen in milliseconds
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize the handler
        handler = new Handler(Looper.getMainLooper());

        // Define the runnable that will start the HomeActivity
        // This method will be executed after the delay
        runnable = this::startHomeActivity;

        RelativeLayout splashLayout = findViewById(R.id.splash_layout);

        splashLayout.setOnClickListener(view -> {
            // If the user clicks, cancel the delayed task and start the activity immediately
            handler.removeCallbacks(runnable);
            startHomeActivity();
        });

        // Post the runnable with a delay
        handler.postDelayed(runnable, SPLASH_DELAY);
    }

    private void startHomeActivity() {
        // Prevent starting the activity multiple times
        if (isFinishing()) {
            return;
        }
        // Start HomeActivity
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

        // **IMPORTANT**: Finish the SplashActivity so the user can't navigate back to it
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks when the activity is destroyed
        handler.removeCallbacks(runnable);
    }
}