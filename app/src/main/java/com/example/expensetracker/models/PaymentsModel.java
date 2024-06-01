package com.example.expensetracker.models;

public class PaymentsModel {
    private String paymentSubType;
    private String amount;

    public PaymentsModel(String paymentSubType, String amount) {
        this.paymentSubType = paymentSubType;
        this.amount = amount;
    }
}
