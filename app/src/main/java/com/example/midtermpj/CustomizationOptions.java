package com.example.midtermpj;

import java.util.List;

public class CustomizationOptions {
    List<Option> shotOptions;
    List<Option> sizeOptions;
    List<Option> iceOptions;
    // Add constructor and getters

    public CustomizationOptions(List<Option> shotOptions, List<Option> sizeOptions, List<Option> iceOptions) {
        this.shotOptions = shotOptions;
        this.sizeOptions = sizeOptions;
        this.iceOptions = iceOptions;
    }
}