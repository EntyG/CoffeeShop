package com.example.midtermpj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final Context context;
    private final List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderPriceText.setText(String.format(Locale.US, "$%.2f", order.getTotalPrice()));

        holder.orderAddressText.setText(order.getShippingAddress());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault());
        holder.orderDateText.setText(dateFormat.format(order.getOrderDate()));

        if (!order.getItems().isEmpty()) {
            CartItem firstItem = order.getItems().get(0);
            String itemSummary = firstItem.getCoffeeProduct().getTitle();

            int remainingItems = order.getItems().size() - 1;
            if (remainingItems > 0) {
                itemSummary += " + " + remainingItems + (" more");
            }
            holder.orderItemsText.setText(itemSummary);
        } else {
            holder.orderItemsText.setText("Order details unavailable");
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateText, orderPriceText, orderItemsText, orderAddressText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderPriceText = itemView.findViewById(R.id.orderPriceText);
            orderItemsText = itemView.findViewById(R.id.orderItemsText);
            orderAddressText = itemView.findViewById(R.id.orderAddressText);
        }
    }
}