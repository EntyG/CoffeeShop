package com.example.midtermpj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> cartItems;
    private final Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    /**
     * This method is called by the RecyclerView to create a new ViewHolder.
     * It inflates the XML layout for a single row.
     */
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. Get the LayoutInflater from the context
        LayoutInflater inflater = LayoutInflater.from(context);
        // 2. Inflate your item_cart.xml layout
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        // 3. Return a new ViewHolder instance with the inflated view
        return new CartViewHolder(view);
    }

    /**
     * This method is called by the RecyclerView to bind data to a specific row.
     * It takes the data from the cartItems list and sets it on the views in the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Get the product details from the CartItem
        CoffeeProduct product = item.getCoffeeProduct();

        // Populate the views
        holder.titleText.setText(product.getTitle());
        holder.imageView.setImageResource(product.getImageResource()); // Using local drawables
        holder.optionsText.setText(item.getSelectedOptionsDescription());
        holder.quantityText.setText("x " + item.getQuantity());

        // Calculate the total price for this line item (item price * quantity)
        double lineItemTotal = item.getFinalPrice() * item.getQuantity();
        holder.priceText.setText(String.format(Locale.US, "$%.2f", lineItemTotal));
    }

    /**
     * This method tells the RecyclerView the total number of items in your data list.
     * It should return the size of your cartItems list.
     */
    @Override
    public int getItemCount() {
        // Return the actual size of the list, not 0
        return cartItems.size();
    }

    /**
     * The ViewHolder class holds references to the views for a single row.
     * This avoids repeated calls to findViewById(), making the scrolling smoother.
     */
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, optionsText, quantityText, priceText;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cartItemImageView);
            titleText = itemView.findViewById(R.id.cartItemTitleText);
            optionsText = itemView.findViewById(R.id.cartItemOptionsText);
            quantityText = itemView.findViewById(R.id.cartItemQuantityText);
            priceText = itemView.findViewById(R.id.cartItemPriceText);
        }
    }
}