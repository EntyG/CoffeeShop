package com.example.midtermpj;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {
    private RecyclerView redeemRecyclerView;
    private RedeemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        redeemRecyclerView = findViewById(R.id.redeemRecyclerView);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<RedeemableItem> items = RedeemableRepository.getInstance().getRedeemableItems();

        adapter = new RedeemAdapter(this, items);
        redeemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        redeemRecyclerView.setAdapter(adapter);
    }
}