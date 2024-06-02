package com.example.expensetracker.models;

public class PaymentsModel {

    private String payment;
    private String amount;

    public PaymentsModel(String paymentSubType, String amount) {
        this.payment = paymentSubType;
        this.amount = amount;
    }

    public String getPayment() {
        return payment;
    }

    public String getAmount() {
        return amount;
    }


}
