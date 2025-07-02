package com.example.midtermpj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RewardHistoryAdapter extends RecyclerView.Adapter<RewardHistoryAdapter.RewardViewHolder> {
    private final List<Order> completedOrders;

    public RewardHistoryAdapter(List<Order> completedOrders) {
        this.completedOrders = completedOrders;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_rewards, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Order order = completedOrders.get(position);

        holder.pointsText.setText("+ " + order.getPointsEarned() + " Pts");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault());
        holder.dateText.setText(dateFormat.format(order.getOrderDate()));

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            CartItem firstItem = order.getItems().get(0);
            String itemSummary = firstItem.getCoffeeProduct().getTitle();

            int remainingItems = order.getItems().size() - 1;
            if (remainingItems > 0) {
                itemSummary += " + " + remainingItems + (remainingItems == 1 ? " more" : " more");
            }
            holder.titleText.setText(itemSummary);

        } else {
            holder.titleText.setText("Completed Order");
        }
    }

    @Override
    public int getItemCount() {
        return completedOrders.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText, pointsText;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.rewardItemsText);
            dateText = itemView.findViewById(R.id.rewardDateText);
            pointsText = itemView.findViewById(R.id.rewardPointText);
        }
    }
}