package com.example.midtermpj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.RedeemViewHolder> {
    private final List<RedeemableItem> redeemableItems;
    private final Context context;

    public RedeemAdapter(Context context, List<RedeemableItem> redeemableItems) {
        this.context = context;
        this.redeemableItems = redeemableItems;
    }

    @NonNull
    @Override
    public RedeemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_redeem, parent, false);
        return new RedeemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedeemViewHolder holder, int position) {
        RedeemableItem item = redeemableItems.get(position);
        holder.titleText.setText(item.getTitle());
        holder.validityText.setText(item.getValidUntil());
        holder.imageView.setImageResource(item.getImageResource());
        holder.redeemButton.setText(item.getPointCost() + " pts");

        holder.redeemButton.setOnClickListener(v -> {
            User currentUser = UserRepository.getInstance().getCurrentUser();

            if (currentUser.getRewardPoints() >= item.getPointCost()) {
                int newPoints = currentUser.getRewardPoints() - item.getPointCost();
                currentUser.setRewardPoints(newPoints);

                createOrderForRedeemedItem(item);

                Toast.makeText(context, "Redeemed " + item.getTitle() + " successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);

                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            } else {
                Toast.makeText(context, "Not enough points to redeem this item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrderForRedeemedItem(RedeemableItem redeemedItem) {
        CoffeeProduct correspondingProduct = findCoffeeProductByTitle(redeemedItem.getTitle());

        if (correspondingProduct == null) {
            return;
        }

        CartItem freeCartItem = new CartItem(
                correspondingProduct,
                1,
                "Redeemed with points",
                0.00
        );

        CartRepository.getInstance().addItem(freeCartItem);
    }

    private CoffeeProduct findCoffeeProductByTitle(String title) {
        List<CoffeeProduct> allProducts = CoffeeRepository.getInstance().getAllProducts();
        for (CoffeeProduct product : allProducts) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return redeemableItems.size();
    }

    public static class RedeemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, validityText;
        Button redeemButton;

        public RedeemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.redeemItemImageView);
            titleText = itemView.findViewById(R.id.redeemItemTitleText);
            validityText = itemView.findViewById(R.id.redeemItemValidityText);
            redeemButton = itemView.findViewById(R.id.redeemItemButton);
        }
    }
}