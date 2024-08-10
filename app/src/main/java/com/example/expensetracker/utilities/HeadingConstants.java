package com.example.expensetracker.utilities;

import java.util.HashMap;

public class HeadingConstants {

    //Excel  Sheet Names
    public static final String PROFILE_ACTIVITY = "Profile";
    public static final String CATEGORIES = "Categories";
    public static final String PAYMENT_TYPE = "Payment Types";
    public static final String BUDGET = "Budget";
    public static final String EXPENSE = "Expenses";

    //Column Names
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String TYPE = "Type";
    public static final String SUBTYPES = "SubTypes";
    public static final String LIMIT = "Limit";
    public static final String TRANSACTIONID = "TransactionID";
    public static final String DATE = "Date";
    public static final String AMOUNT = "Amount";
    public static final String CATEGORY = "Category";
    public static final String SUBCATEGORY = "SubCategory";
    public static final String PAYMENT = "PaymentType";
    public static final String PAYMENT_SUBTYPE = "PaymentSubtype";
    public static final String NOTE = "Note";

    public static final String CASH = "Cash";
    public static final String CASH1 = "cash";
    public static final String CHECK = "Check";


    public static final HashMap<String, Integer> expenseColumnIndices = new HashMap<String, Integer>();

    static {
        expenseColumnIndices.put(TRANSACTIONID,0);
        expenseColumnIndices.put(DATE, 1);
        expenseColumnIndices.put(AMOUNT, 2);
        expenseColumnIndices.put(CATEGORY, 3);
        expenseColumnIndices.put(SUBCATEGORY, 4);
        expenseColumnIndices.put(PAYMENT, 5);
        expenseColumnIndices.put(PAYMENT_SUBTYPE, 6);
        expenseColumnIndices.put(NOTE, 7);
    }

    //->correct path   //"/storage/emulated/0/Android/data/com.example.expensetracker/files/Expense Tracker App/";
    public static final String BASE_PATH = "/storage/emulated/0/Android/data/com.example.expensetracker/files/Expense Tracker App/";

    //public static final String BASE_PATH = "/Expense Tracker App/"; // not working, need to test

}
