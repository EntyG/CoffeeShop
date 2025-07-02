package com.example.midtermpj;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {
    public static final String EXTRA_COFFEE_ID = "EXTRA_COFFEE_ID";
    private final List<CoffeeProduct> coffeeList;
    private final Context context;

    public CoffeeAdapter(Context context, List<CoffeeProduct> coffeeList) {
        this.context = context;
        this.coffeeList = coffeeList;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coffee_grid, parent, false);
        return new CoffeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        CoffeeProduct coffee = coffeeList.get(position);
        holder.coffeeName.setText(coffee.getTitle());
        holder.coffeeImage.setImageResource(coffee.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra(EXTRA_COFFEE_ID, coffee.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return coffeeList.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        ImageView coffeeImage;
        TextView coffeeName;

        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);
            coffeeImage = itemView.findViewById(R.id.imageViewCoffee);
            coffeeName = itemView.findViewById(R.id.textViewCoffeeName);
        }
    }
}