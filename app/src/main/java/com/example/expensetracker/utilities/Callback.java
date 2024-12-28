package com.example.expensetracker.utilities;

public interface Callback<T> {
    void onComplete(T result);
    void onError(Exception e);
    void onFinished(); // New method to indicate completion
}
