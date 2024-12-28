package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.CASH;
import static com.example.expensetracker.utilities.HeadingConstants.CASH1;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.DATE;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.NOTE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.SUBCATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.TRANSACTIONID;
import static com.example.expensetracker.utilities.HeadingConstants.expenseColumnIndices;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.adapters.PaymentTypeExpenseAdapter;
import com.example.expensetracker.adapters.TransactionExpenseAdapter;
import com.example.expensetracker.models.PaymentsModel;
import com.example.expensetracker.models.TransactionModel;
import com.example.expensetracker.utilities.Callback;
import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonSharedVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ViewAllButtonActivity extends AppCompatActivity {

    private ListView transactionsList;
    private ListView paymentsList;
    private Button btnTransactions;
    private Button btnPayments;

    private HashMap<String, ArrayList<String>> paymentToSubPaymentMap = new HashMap<>();
    private ArrayList<HashMap<String, String>> readExpenseDataRowMapListFromExcel;
    private ArrayList<String> readAllSubPaymentListFromSheet;
    private HashMap<String, Integer> eachPaymentTypeAmount = new HashMap<>();
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readTypesListandSubTypesMapFromExcelUtil;
    private ArrayList<String> readAllPaymentTypeListFromSheet;
    private boolean isOnCompleteExecuted = false; // Initialize the flag as false
    private CountDownLatch latch = new CountDownLatch(1); // Initialize with count 1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () ");

        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        // Initialize variables in SharedVariables
        SingleTonSharedVariables sharedVariables = SingleTonSharedVariables.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_button);

        transactionsList = findViewById(R.id.expenseTransactionsList);
        paymentsList = findViewById(R.id.expensePaymentsList);
        btnTransactions = findViewById(R.id.btnTransactions);
        btnPayments = findViewById(R.id.btnPayments);

        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactions();
            }
        });

        btnPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayments();
            }
        });

        System.out.println("inside ViewAllButtonActivity class, inside onCreate () before initializeMap() called");
        initializeMap(sharedVariables);
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () before loadData() called");
        loadData(sharedVariables);

        // By default, show Transactions
        showTransactions();

        // Initialize the transactionlist and paymentslist with sample data
        List<TransactionModel> transactions = new ArrayList<>();
        for (HashMap<String, String> expenseRowMap : readExpenseDataRowMapListFromExcel) {
            System.out.println("inside ViewAllButtonActivity class, inside onCreate () , map :" + expenseRowMap);
            transactions.add(new TransactionModel(expenseRowMap.get(TRANSACTIONID), expenseRowMap.get(DATE), expenseRowMap.get(AMOUNT), expenseRowMap.get(CATEGORY), expenseRowMap.get(SUBCATEGORY), expenseRowMap.get(PAYMENT), expenseRowMap.get(PAYMENT_SUBTYPE), expenseRowMap.get(NOTE)));
        }

        /*transactions.add(new TransactionModel("21-05-2024", "100", "Food", "Lunch", "Credit card", "ICICI", "party"));
        transactions.add(new TransactionModel("23-05-2024", "300", "Food", "Dinner", "Credit card", "1Card", "party"));
        transactions.add(new TransactionModel("25-05-2024", "5000", "Fuel", "Petrol", "Credit card", "SBI BPCL", "Travel"));
        transactions.add(new TransactionModel("29-05-2024", "300", "Shopping", "Petrol", "cash", "", "Travel"));
        transactions.add(new TransactionModel("5-05-2024", "5020", "Bills", "", "Credit card", "SBI BPCL", "Travel"));
        transactions.add(new TransactionModel("19-05-2024", "200", "Grociries", "", "cash", "", ""));*/

        /*readAllSubPaymentListFromSheet = ExpenseTrackerExcelUtil.readAllSubPaymentsFromExcel(paymentToSubPaymentMap, "ViewAllButtonActivity class");
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () , readAllSubPaymentListFromSheet :" + readAllSubPaymentListFromSheet);*/

        //readExpenseDataRowMapListFromExcel = ExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>());


        System.out.println("inside ViewAllButtonActivity class, inside onCreate () , before calculateEachPaymentTypeAmount()");
        calculateEachPaymentTypeAmount();

        System.out.println("inside ViewAllButtonActivity class, inside onCreate () 1 of 7, ==Started==");
        List<PaymentsModel> paymentTypes = new ArrayList<>();
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () 2 of 7, paymentTypes: " + paymentTypes);

        for (String payment : eachPaymentTypeAmount.keySet()) {
            System.out.println("inside ViewAllButtonActivity class, inside onCreate () 1 of 7, ==payment==" + payment);
            paymentTypes.add(new PaymentsModel(payment, eachPaymentTypeAmount.get(payment).toString()));
        }
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () 1 of 7, ==paymentTypes==" + paymentTypes);
      /*  paymentTypes.add(new PaymentsModel("ICICI", "1000"));
        paymentTypes.add(new PaymentsModel("1Card", "2000"));
        paymentTypes.add(new PaymentsModel("1Card", "4000"));
        paymentTypes.add(new PaymentsModel("Cash", "5000"));*/

        TransactionExpenseAdapter transactionsAdapter = new TransactionExpenseAdapter(this, transactions);
        PaymentTypeExpenseAdapter paymentsAdapter = new PaymentTypeExpenseAdapter(this, paymentTypes);

        transactionsList.setAdapter(transactionsAdapter);
        paymentsList.setAdapter(paymentsAdapter);
    }

    private void initializeMap(SingleTonSharedVariables sharedVariables) {
        System.out.println("inside ViewAllButtonActivity class, inside initializeMap(): started");

        singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtilAsync(PAYMENT_TYPE, sharedVariables.getFilePath(), new Callback<HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>>>() {
            @Override
            public void onComplete(HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> result) {
                // Set the flag to true
                isOnCompleteExecuted = true;
                // Update the UI with the result
                Log.i("ExcelData", "Data loaded successfully: " + result);
              /*  paymentToSubPaymentMap = readTypesListandSubTypesMapFromExcelUtil.get(readTypesListandSubTypesMapFromExcelUtil.keySet().iterator().next());// to get value of key present at 0 index in map
                System.out.println("inside ViewAllButtonActivity class, inside loadData (), paymentToSubPaymentMap" + paymentToSubPaymentMap);*/
                // Assign the result to the global variable
                readTypesListandSubTypesMapFromExcelUtil = result;
                // Signal that the map is initialized

                Log.i("ExcelData", "Data loaded successfully: result: " + result);
                result.forEach((key, value) -> {
                    System.out.println("Map contents: " + result);

                    /*System.out.println("\tKey: " + key);
                    System.out.println("\tValue:");
                    value.forEach((subKey, subValue) -> {
                        System.out.println("\t\tSubKey: " + subKey + ", SubValue: " + subValue);
                    });*/
                });
                // Release the latch
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                // Handle error
                Log.i("ExcelError", "Error loading data: ", e);
                latch.countDown(); // Ensure the latch is released even on error
            }

            @Override
            public void onFinished() {
                Log.i("Callback", "All operations in onComplete have finished.");
            }
        });
        System.out.println("inside ViewAllButtonActivity class, inside initializeMap() readTypesListandSubTypesMapFromExcelUtil: "+readTypesListandSubTypesMapFromExcelUtil);
        System.out.println("inside ViewAllButtonActivity class, inside initializeMap(): ended");
    }

    /*private void loadData(SingleTonSharedVariables sharedVariables) {
        System.out.println("inside ViewAllButtonActivity class,  loadData() : started");
        try {
            System.out.println("inside ViewAllButtonActivity class,  loadData() try loop : started");
            latch.await(); // Blocks until latch.countDown() is called
            System.out.println("inside ViewAllButtonActivity class,  loadData() try loop : ended");
        } catch (InterruptedException e) {
            System.out.println("inside ViewAllButtonActivity class,  loadData() catch loop : started");
            e.printStackTrace();
            System.out.println("inside ViewAllButtonActivity class,  loadData() catch loop : ended");
        }
        System.out.println("inside ViewAllButtonActivity class,  loadData() part2");
        readExpenseDataRowMapListFromExcel = singleTonExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>(), sharedVariables.getFilePath());
        System.out.println("inside ViewAllButtonActivity class, inside loadData () , readExpenseDataRowMapListFromExcel :" + readExpenseDataRowMapListFromExcel);

      *//*  readTypesListandSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE, sharedVariables.getFilePath());
        System.out.println("inside ViewAllButtonActivity class, inside loadData () , readTypesListandSubTypesMapFromExcelUtil" + readTypesListandSubTypesMapFromExcelUtil);*//*


        // Later in your code, check the flag
        //if (isOnCompleteExecuted) {
        if (readTypesListandSubTypesMapFromExcelUtil != null) {
            Log.i("Check", "onComplete has finished executing.");
            paymentToSubPaymentMap = readTypesListandSubTypesMapFromExcelUtil.get(readTypesListandSubTypesMapFromExcelUtil.keySet().iterator().next());// to get value of key present at 0 index in map
            System.out.println("inside ViewAllButtonActivity class, inside loadData (), paymentToSubPaymentMap" + paymentToSubPaymentMap);

            readAllSubPaymentListFromSheet = singleTonExpenseTrackerExcelUtil.readAllSubPaymentsFromExcel(paymentToSubPaymentMap, "AppHomeActivity class");
            System.out.println("inside ViewAllButtonActivity class, inside loadData () , readAllSubPaymentListFromSheet :" + readAllSubPaymentListFromSheet);
        } else {
            Log.i("Check", "onComplete is not yet executed.");
        }
        *//*paymentToSubPaymentMap = readTypesListandSubTypesMapFromExcelUtil.get(readTypesListandSubTypesMapFromExcelUtil.keySet().iterator().next());// to get value of key present at 0 index in map
        System.out.println("inside ViewAllButtonActivity class, inside loadData (), paymentToSubPaymentMap" + paymentToSubPaymentMap);

        readAllSubPaymentListFromSheet = singleTonExpenseTrackerExcelUtil.readAllSubPaymentsFromExcel(paymentToSubPaymentMap, "AppHomeActivity class");
        System.out.println("inside ViewAllButtonActivity class, inside loadData () , readAllSubPaymentListFromSheet :" + readAllSubPaymentListFromSheet);*//*
        System.out.println("inside ViewAllButtonActivity class, inside onCreate () before loadData() : ended");
    }*/

    private void loadData(SingleTonSharedVariables sharedVariables) {
        System.out.println("inside ViewAllButtonActivity class,  loadData() : started");
        new Thread(() -> {
            try {
                System.out.println("inside ViewAllButtonActivity class,  loadData() try loop : started");
                latch.await(); // Blocks until latch.countDown() is called
                System.out.println("inside ViewAllButtonActivity class,  loadData() try loop : ended");
                // Perform the data loading in the main thread
                runOnUiThread(() -> {
                    System.out.println("inside ViewAllButtonActivity class,  loadData() part2");
                    readExpenseDataRowMapListFromExcel = singleTonExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>(), sharedVariables.getFilePath());
                    System.out.println("inside ViewAllButtonActivity class, inside loadData () , readExpenseDataRowMapListFromExcel :" + readExpenseDataRowMapListFromExcel);
                    if (readTypesListandSubTypesMapFromExcelUtil != null) {
                        HashMap<String, ArrayList<String>> paymentToSubPaymentMap = readTypesListandSubTypesMapFromExcelUtil.get(readTypesListandSubTypesMapFromExcelUtil.keySet().iterator().next());
                        System.out.println("inside ViewAllButtonActivity class, inside loadData (), paymentToSubPaymentMap" + paymentToSubPaymentMap);
                        readAllSubPaymentListFromSheet = singleTonExpenseTrackerExcelUtil.readAllSubPaymentsFromExcel(paymentToSubPaymentMap, "AppHomeActivity class");
                        System.out.println("inside ViewAllButtonActivity class, inside loadData () , readAllSubPaymentListFromSheet :" + readAllSubPaymentListFromSheet);
                    } else {
                        Log.e("LoadData", "Map is still null!");
                    }
                });
            } catch (InterruptedException e) {
                System.out.println("inside ViewAllButtonActivity class,  loadData() catch loop : started");
                e.printStackTrace();
                System.out.println("inside ViewAllButtonActivity class,  loadData() catch loop : ended");
            }
        }).start();
    }

    private void calculateEachPaymentTypeAmount() {

        System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 1 of 7, ==Started==");
        System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () starting1 eachPaymentTypeAmount: " + eachPaymentTypeAmount);
        eachPaymentTypeAmount.clear();
        for (String subPayment : readAllSubPaymentListFromSheet) {
            System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 2 of 7" + subPayment);
            for (HashMap<String, String> rowExpenseMap : readExpenseDataRowMapListFromExcel) {
                System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 3 of 7" + rowExpenseMap);
                try {
                    String amountStr = rowExpenseMap.get(AMOUNT);
                    System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 4 of 7" + amountStr);
                    if (rowExpenseMap.get(PAYMENT_SUBTYPE).equals(subPayment) && amountStr != null) {
                        if (eachPaymentTypeAmount.isEmpty() || !eachPaymentTypeAmount.containsKey(subPayment)) {
                            eachPaymentTypeAmount.put(subPayment, Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 5 of 7 if" + eachPaymentTypeAmount);
                        } else {
                            eachPaymentTypeAmount.put(subPayment, eachPaymentTypeAmount.getOrDefault(subPayment, 0) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            //eachPaymentTypeAmount.put(subPayment, eachPaymentTypeAmount.get(subPayment) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 6 of 7 else" + eachPaymentTypeAmount);
                        }
                    }

                    // to handle cash payment
                    if ((rowExpenseMap.get(PAYMENT).equals(CASH)) || (rowExpenseMap.get(PAYMENT).equals(CASH1)) && amountStr != null) {

                        if (eachPaymentTypeAmount.isEmpty() || !eachPaymentTypeAmount.containsKey(CASH) || !eachPaymentTypeAmount.containsKey(CASH1)) {
                            eachPaymentTypeAmount.put(CASH, Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () --cash payment-- 5 of 7 if" + eachPaymentTypeAmount);
                        } else {
                            eachPaymentTypeAmount.put(CASH, eachPaymentTypeAmount.getOrDefault(CASH, 0) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            //eachPaymentTypeAmount.put(CASH, eachPaymentTypeAmount.get(CASH) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () --cash payment-- 6 of 7 else" + eachPaymentTypeAmount);
                        }
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 7 of 7 starting1 eachPaymentTypeAmount:, " + eachPaymentTypeAmount);
        System.out.println("inside ViewAllButtonActivity class, inside calculateEachPaymentTypeAmount () 7 of 7 , ==ended==");
    }

    private void showTransactions() {
        paymentsList.setVisibility(View.GONE);
        transactionsList.setVisibility(View.VISIBLE);
        btnTransactions.setBackgroundResource(R.drawable.button_selector_transactions); // Selected state
        btnPayments.setBackgroundResource(R.drawable.button_selector_payments); // Unselected state
    }

    private void showPayments() {
        transactionsList.setVisibility(View.GONE);
        paymentsList.setVisibility(View.VISIBLE);
        btnTransactions.setBackgroundResource(R.drawable.button_selector_payments); // Unselected state
        btnPayments.setBackgroundResource(R.drawable.button_selector_transactions); // Selected state
    }
}
