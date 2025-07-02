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

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        CoffeeProduct product = item.getCoffeeProduct();

        holder.titleText.setText(product.getTitle());
        holder.imageView.setImageResource(product.getImageResource());
        holder.optionsText.setText(item.getSelectedOptionsDescription());
        holder.quantityText.setText("x " + item.getQuantity());

        double lineItemTotal = item.getFinalPrice() * item.getQuantity();
        holder.priceText.setText(String.format(Locale.US, "$%.2f", lineItemTotal));
    }

    @Override
    public int getItemCount() {
        // Return the actual size of the list, not 0
        return cartItems.size();
    }

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