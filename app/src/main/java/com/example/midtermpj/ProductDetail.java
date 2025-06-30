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

    // UI Elements
    private TextView coffeeNameText, totalAmountText, quantityText;
    private ImageView coffeeImageView, minusButton, plusButton;
    private RadioGroup shotRadioGroup, sizeRadioGroup, iceRadioGroup;

    // State Variables
    private int quantity = 1;
    private double selectedShotModifier = 0.0;
    private double selectedSizeModifier = 0.0;
    private double selectedIceModifier = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // 1. Find all your views
        findViews();

        // 2. Get the coffee ID from the Intent
        int coffeeId = getIntent().getIntExtra(CoffeeAdapter.EXTRA_COFFEE_ID, -1);
        if (coffeeId == -1) {
            handleError("Invalid Coffee ID.");
            return;
        }

        // 3. Get the coffee data from the repository
        currentCoffee = CoffeeRepository.getInstance().getProductById(coffeeId);
        if (currentCoffee == null) {
            handleError("Coffee not found in repository.");
            return;
        }

        // 4. Populate the UI with the data
        populateUi();

        // 5. Set up all the listeners for user interaction
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

        // --- DYNAMICALLY POPULATE OPTIONS ---
        setupOptionGroup(shotRadioGroup, currentCoffee.getCustomizationOptions().shotOptions, "shot");
        setupOptionGroup(sizeRadioGroup, currentCoffee.getCustomizationOptions().sizeOptions, "size");
        setupOptionGroup(iceRadioGroup, currentCoffee.getCustomizationOptions().iceOptions, "ice");

        // Set initial total price
        calculateTotalPrice();
    }

    /**
     * Dynamically creates RadioButtons for an option group.
     * This makes the UI data-driven.
     */
    private void setupOptionGroup(RadioGroup group, List<Option> options, String type) {
        // If there's only one option (or none), hide the whole section to prevent confusion.
        // For example, if a drink is always "Hot", no need to show a radio button.

        // Clear any placeholder buttons from the XML
        group.removeAllViews();
        boolean isFirst = true;

        for (Option option : options) {
            // Create a new RadioButton. You would use your custom selectors here.
            RadioButton button = new RadioButton(this);
            button.setText(option.option); // This is for text-based buttons like "Single"/"Double"
            // For icon-based buttons, you would set the button drawable instead
            // button.setButtonDrawable(getIconForOption(option.option));

            // Use the tag to store the price modifier. This is a clean way to retrieve it later.
            button.setTag(option.priceModifier);

            group.addView(button);

            // Set the first option as the default selection
            if (isFirst) {
                button.setChecked(true);
                // Set the initial price modifier based on the default selection
                if (type.equals("shot")) selectedShotModifier = option.priceModifier;
                if (type.equals("size")) selectedSizeModifier = option.priceModifier;
                if (type.equals("ice")) selectedIceModifier = option.priceModifier;
                isFirst = false;
            }
        }
    }

    private String getSelectedOptionsDescription() {
        List<String> selectedOptions = new ArrayList<>();

        // Get selected Shot text
        int selectedShotId = shotRadioGroup.getCheckedRadioButtonId();
        if (selectedShotId != View.NO_ID) {
            RadioButton selectedShotButton = findViewById(selectedShotId);
            selectedOptions.add(selectedShotButton.getText().toString());
        }

        // Get selected Size text
        // NOTE: This assumes your size radio buttons also have text. If they are icon-only,
        // you might need a different way to get their description.
        int selectedSizeId = sizeRadioGroup.getCheckedRadioButtonId();
        if (selectedSizeId != View.NO_ID) {
            RadioButton selectedSizeButton = findViewById(selectedSizeId);
            // We get the option name from the original data model, which is more reliable
            // than getting it from a button that might not have text.
            for (Option opt : currentCoffee.getCustomizationOptions().sizeOptions) {
                if (opt.priceModifier == (double) selectedSizeButton.getTag()) {
                    selectedOptions.add(opt.option);
                    break;
                }
            }
        }

        // Get selected Ice text
        int selectedIceId = iceRadioGroup.getCheckedRadioButtonId();
        if (selectedIceId != View.NO_ID) {
            RadioButton selectedIceButton = findViewById(selectedIceId);
            for (Option opt : currentCoffee.getCustomizationOptions().iceOptions) {
                if (opt.priceModifier == (double) selectedIceButton.getTag()) {
                    selectedOptions.add(opt.option);
                    break;
                }
            }
        }

        // Join the collected strings with a " | " separator
        return String.join(" | ", selectedOptions);
    }

    private void setupListeners() {
        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Listener for Shot selection
        shotRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null) {
                selectedShotModifier = (double) selectedButton.getTag();
                calculateTotalPrice();
            }
        });

        // Listener for Size selection
        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null) {
                selectedSizeModifier = (double) selectedButton.getTag();
                calculateTotalPrice();
            }
        });

        // Listener for Ice selection
        iceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null) {
                selectedIceModifier = (double) selectedButton.getTag();
                calculateTotalPrice();
            }
        });

        // Listeners for quantity buttons
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

        // Inside the setupListeners() method of ProductDetail.java

        // Assuming you have an "addToCartButton"
        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            // 1. Create a description of the selected options
            //    You would get the selected option names from your RadioButtons
            String optionsDesc = getSelectedOptionsDescription();
            // 2. Calculate the price for a single item with all modifiers
            double singleItemPrice = currentCoffee.getBasePrice() + selectedShotModifier + selectedSizeModifier + selectedIceModifier;

            // 3. Create the CartItem
            CartItem newItem = new CartItem(currentCoffee, quantity, optionsDesc, singleItemPrice);

            // 4. Add the item to the global repository
            CartRepository.getInstance().addItem(newItem);

            // 5. Navigate to the CartActivity
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

    /**
     * This is the core logic. It calculates the final price based on selections.
     */
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