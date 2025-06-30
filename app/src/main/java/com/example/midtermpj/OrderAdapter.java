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

    /**
     * Creates a new ViewHolder by inflating the item_order.xml layout.
     */
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    /**
     * Binds the data from a specific Order object to the views in the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // 1. Set the Total Price
        holder.orderPriceText.setText(String.format(Locale.US, "$%.2f", order.getTotalPrice()));

        // 2. Set the Shipping Address
        holder.orderAddressText.setText(order.getShippingAddress());

        // 3. Format and Set the Order Date
        // We create a formatter to turn the Date object into a readable string.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault());
        holder.orderDateText.setText(dateFormat.format(order.getOrderDate()));

        // 4. Summarize and Set the Order Items
        // This logic creates a string like "Americano + 2 more".
        if (!order.getItems().isEmpty()) {
            // Get the first item to display its name
            CartItem firstItem = order.getItems().get(0);
            String itemSummary = firstItem.getCoffeeProduct().getTitle();

            // Check if there are more items to summarize
            int remainingItems = order.getItems().size() - 1;
            if (remainingItems > 0) {
                itemSummary += " + " + remainingItems + (remainingItems == 1 ? " more" : " more");
            }
            holder.orderItemsText.setText(itemSummary);
        } else {
            // A fallback in case an order has no items
            holder.orderItemsText.setText("Order details unavailable");
        }
    }

    /**
     * Returns the total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    /**
     * ViewHolder class that holds references to the views for each list item.
     */
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