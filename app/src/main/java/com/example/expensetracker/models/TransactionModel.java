package com.example.expensetracker.models;


public class TransactionModel {
    private String date;
    private String amount;
    private String categoryType;
    private String categorySubType;
    private String paymentType;
    private String paymentSubType;
    private String note;

    public TransactionModel(String date, String amount, String categoryType, String categorySubType, String paymentType, String paymentSubType, String note) {
        this.date = date;
        this.amount = amount;
        this.categoryType = categoryType;
        this.categorySubType = categorySubType;
        this.paymentType = paymentType;
        this.paymentSubType = paymentSubType;
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryType() {

        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategorySubType() {
        return categorySubType;
    }

    public void setCategorySubType(String categorySubType) {
        this.categorySubType = categorySubType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentSubType() {
        return paymentSubType;
    }

    public void setPaymentSubType(String paymentSubType) {
        this.paymentSubType = paymentSubType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
