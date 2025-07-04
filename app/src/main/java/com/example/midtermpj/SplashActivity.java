package com.example.midtermpj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loadAllInitialData();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void loadAllInitialData() {
        UserRepository.getInstance().loadInitialUser(new UserRepository.OnUserLoadListener() {
            @Override
            public void onUserLoaded() {
                OrderRepository.getInstance().loadInitialOrders(new OrderRepository.OnOrdersLoadListener() {
                    @Override
                    public void onOrdersLoaded() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    @Override
                    public void onOrdersLoadFailed(Exception e) {
                        handleLoadFailure(e);
                    }
                });
            }
            @Override
            public void onUserLoadFailed(Exception e) {
                handleLoadFailure(e);
            }
        });
    }

    private void handleLoadFailure(Exception e) {
        Toast.makeText(SplashActivity.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        UserRepository.getInstance().clearCurrentUser();
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}