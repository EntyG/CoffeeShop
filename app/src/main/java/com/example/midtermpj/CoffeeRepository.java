package com.example.midtermpj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoffeeRepository {
    private static CoffeeRepository instance;
    private List<CoffeeProduct> coffeeList;

    private CoffeeRepository() {
        loadCoffeeProducts();
    }

    public static synchronized CoffeeRepository getInstance() {
        if (instance == null) {
            instance = new CoffeeRepository();
        }
        return instance;
    }

    public List<CoffeeProduct> getAllProducts() {
        return coffeeList;
    }

    public CoffeeProduct getProductById(int id) {
        for (CoffeeProduct product : coffeeList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private void loadCoffeeProducts() {
        coffeeList = new ArrayList<>();

        // --- 1. Americano Options ---
        List<Option> americanoShot = Arrays.asList(new Option("Single", 0.0), new Option("Double", 0.75));
        List<Option> americanoSize = Arrays.asList(new Option("Small", -0.25), new Option("Medium", 0.0), new Option("Large", 0.50));
        List<Option> americanoIce = Arrays.asList(new Option("Hot", 0.0), new Option("Light", 0.0), new Option("Regular", 0.0), new Option("Extra", 0.0));
        CustomizationOptions americanoOptions = new CustomizationOptions(americanoShot, americanoSize, americanoIce);

        coffeeList.add(new CoffeeProduct(1, "Americano", 3.00, R.drawable.americano, americanoOptions));

        // --- 2. Cappuccino Options ---
        List<Option> cappuccinoShot = Arrays.asList(new Option("Single", 0.0), new Option("Double", 0.75));
        List<Option> cappuccinoSize = Arrays.asList(new Option("Small", -0.25), new Option("Medium", 0.0), new Option("Large", 0.50));
        // A traditional cappuccino is always hot
        List<Option> cappuccinoIce = Collections.singletonList(new Option("Hot", 0.0));
        CustomizationOptions cappuccinoOptions = new CustomizationOptions(cappuccinoShot, cappuccinoSize, cappuccinoIce);

        coffeeList.add(new CoffeeProduct(2, "Cappuccino", 3.50, R.drawable.cappuccino, cappuccinoOptions));

        // --- 3. Mocha Options ---
        List<Option> mochaShot = Arrays.asList(new Option("Single", 0.0), new Option("Double", 0.75));
        List<Option> mochaSize = Arrays.asList(new Option("Small", -0.25), new Option("Medium", 0.0), new Option("Large", 0.50));
        List<Option> mochaIce = Arrays.asList(new Option("Hot", 0.0), new Option("Light", 0.0), new Option("Regular", 0.0), new Option("Extra", 0.0));
        CustomizationOptions mochaOptions = new CustomizationOptions(mochaShot, mochaSize, mochaIce);

        coffeeList.add(new CoffeeProduct(3, "Mocha", 4.00, R.drawable.mocha, mochaOptions));

        // --- 4. Flat White Options ---
        // A flat white is traditionally a double shot.
        List<Option> flatWhiteShot = Collections.singletonList(new Option("Double", 0.0));
        List<Option> flatWhiteSize = Arrays.asList(new Option("Small", 0.0), new Option("Medium", 0.25), new Option("Large", 0.50));
        // And it's always hot.
        List<Option> flatWhiteIce = Collections.singletonList(new Option("Hot", 0.0));
        CustomizationOptions flatWhiteOptions = new CustomizationOptions(flatWhiteShot, flatWhiteSize, flatWhiteIce);

        coffeeList.add(new CoffeeProduct(4, "Flat White", 3.75, R.drawable.flatwhite, flatWhiteOptions));
    }
}