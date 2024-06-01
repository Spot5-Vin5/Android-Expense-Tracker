package com.example.expensetracker.models;

public class HomeCategoryModel {
    private int iconResId;
    private String name;
    private String amount;
    private String percentage;

    public HomeCategoryModel(int iconResId, String name, String amount, String percentage) {
        this.iconResId = iconResId;
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
    }
    public int getIconResId() {
        return iconResId;
    }
    public String getName() {
        return name;
    }
    public String getAmount() {
        return amount;

    }
    public String getPercentage() {
        return percentage;
    }
}
