package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetail extends AppCompatActivity {
    private CoffeeProduct currentCoffee;
    private TextView coffeeNameText, totalAmountText, quantityText;
    private ImageView coffeeImageView, minusButton, plusButton;
    private RadioGroup shotRadioGroup, sizeRadioGroup, iceRadioGroup;
    private int quantity = 1;
    private double selectedShotModifier = 0.0;
    private double selectedSizeModifier = 0.0;
    private double selectedIceModifier = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        findViews();

        int coffeeId = getIntent().getIntExtra(CoffeeAdapter.EXTRA_COFFEE_ID, -1);
        if (coffeeId == -1) {
            handleError("Invalid Coffee ID.");
            return;
        }

        currentCoffee = CoffeeRepository.getInstance().getProductById(coffeeId);
        if (currentCoffee == null) {
            handleError("Coffee not found in repository.");
            return;
        }

        populateUi();
        setupListeners();
    }

    private void findViews() {
        coffeeNameText = findViewById(R.id.coffeeNameText);
        totalAmountText = findViewById(R.id.totalAmountText);
        coffeeImageView = findViewById(R.id.coffeeImageView);
        quantityText = findViewById(R.id.quantityText);
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        shotRadioGroup = findViewById(R.id.shotRadioGroup);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        iceRadioGroup = findViewById(R.id.iceRadioGroup);
    }

    private void populateUi() {
        coffeeNameText.setText(currentCoffee.getTitle());
        coffeeImageView.setImageResource(currentCoffee.getImageResource());

        setupOptionGroup(shotRadioGroup, currentCoffee.getCustomizationOptions().shotOptions, "shot");
        setupOptionGroup(sizeRadioGroup, currentCoffee.getCustomizationOptions().sizeOptions, "size");
        setupOptionGroup(iceRadioGroup, currentCoffee.getCustomizationOptions().iceOptions, "ice");

        calculateTotalPrice();
    }

    private void setupOptionGroup(RadioGroup group, List<Option> options, String type) {
        group.removeAllViews();
        boolean isFirst = true;

        for (Option option : options) {
            RadioButton button = new RadioButton(this);
            button.setText(option.option);

            button.setTag(option);

            group.addView(button);

            if (isFirst) {
                button.setChecked(true);
                if (type.equals("shot")) selectedShotModifier = option.priceModifier;
                if (type.equals("size")) selectedSizeModifier = option.priceModifier;
                if (type.equals("ice")) selectedIceModifier = option.priceModifier;
                isFirst = false;
            }
        }
    }

    private String getSelectedOptionsDescription() {
        List<String> selectedOptions = new ArrayList<>();

        int selectedShotId = shotRadioGroup.getCheckedRadioButtonId();
        if (selectedShotId != View.NO_ID) {
            RadioButton selectedButton = findViewById(selectedShotId);
            if (selectedButton.getTag() instanceof Option) {
                selectedOptions.add(((Option) selectedButton.getTag()).option);
            }
        }

        int selectedSizeId = sizeRadioGroup.getCheckedRadioButtonId();
        if (selectedSizeId != View.NO_ID) {
            RadioButton selectedButton = findViewById(selectedSizeId);
            if (selectedButton.getTag() instanceof Option) {
                selectedOptions.add(((Option) selectedButton.getTag()).option);
            }
        }

        int selectedIceId = iceRadioGroup.getCheckedRadioButtonId();
        if (selectedIceId != View.NO_ID) {
            RadioButton selectedButton = findViewById(selectedIceId);
            if (selectedButton.getTag() instanceof Option) {
                selectedOptions.add(((Option) selectedButton.getTag()).option);
            }
        }

        return String.join(" | ", selectedOptions);
    }

    private void setupListeners() {
        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        shotRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null && selectedButton.getTag() instanceof Option) {
                Option selectedOpt = (Option) selectedButton.getTag();
                selectedShotModifier = selectedOpt.priceModifier;
                calculateTotalPrice();
            }
        });

        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null && selectedButton.getTag() instanceof Option) {
                Option selectedOpt = (Option) selectedButton.getTag();
                selectedSizeModifier = selectedOpt.priceModifier;
                calculateTotalPrice();
            }
        });

        iceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null && selectedButton.getTag() instanceof Option) {
                Option selectedOpt = (Option) selectedButton.getTag();
                selectedIceModifier = selectedOpt.priceModifier;
                calculateTotalPrice();
            }
        });

        plusButton.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            calculateTotalPrice();
        });

        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                calculateTotalPrice();
            }
        });

        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            String optionsDesc = getSelectedOptionsDescription();
            double singleItemPrice = currentCoffee.getBasePrice() + selectedShotModifier + selectedSizeModifier + selectedIceModifier;

            CartItem newItem = new CartItem(currentCoffee, quantity, optionsDesc, singleItemPrice);

            CartRepository.getInstance().addItem(newItem);

            Intent intent = new Intent(ProductDetail.this, CartActivity.class);
            startActivity(intent);
            finish();
        });

        View cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetail.this, CartActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void calculateTotalPrice() {
        double basePrice = currentCoffee.getBasePrice();
        double singleItemPrice = basePrice + selectedShotModifier + selectedSizeModifier + selectedIceModifier;
        double totalPrice = singleItemPrice * quantity;

        totalAmountText.setText(String.format(Locale.US, "$%.2f", totalPrice));
    }

    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}