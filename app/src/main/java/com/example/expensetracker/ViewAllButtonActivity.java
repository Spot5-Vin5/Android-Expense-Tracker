package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.models.TransactionModel;
import com.example.expensetracker.utilities.TransactionExpenseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewAllButtonActivity extends AppCompatActivity {

    private LinearLayout transactionsListMain;
    private LinearLayout paymentsListMain;
    private Button btnTransactions;
    private Button btnPayments;
    private ListView transactionsList;
    private ListView paymentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_button);

        transactionsListMain = findViewById(R.id.transactionsListMain);
        paymentsListMain = findViewById(R.id.paymentsListMain);
        btnTransactions = findViewById(R.id.btnTransactions);
        btnPayments = findViewById(R.id.btnPayments);
        transactionsList = findViewById(R.id.transactionsList);
        paymentsList = findViewById(R.id.paymentsList);

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

        // By default, show Transactions
        showTransactions();

        // Initialize the list with sample data
        List<TransactionModel> sampleTransactions = new ArrayList<>();
        sampleTransactions.add(new TransactionModel("21-05-2024", "100", "Food", "Lunch", "Credit card","ICICI", "party"));
        sampleTransactions.add(new TransactionModel("23-05-2024", "300", "Food", "Dinner", "Credit card","1Card", "party"));
        sampleTransactions.add(new TransactionModel("25-05-2024", "5000", "Fuel", "Petrol", "Credit card","SBI BPCL", "Travel"));

        List<TransactionModel> samplePayments = new ArrayList<>();
      /*  samplePayments.add(new TransactionModel("Salary"));
        samplePayments.add(new TransactionModel("Freelance Project"));
        samplePayments.add(new TransactionModel("Dividends"));*/

        TransactionExpenseAdapter transactionsAdapter = new TransactionExpenseAdapter(this, sampleTransactions);
        TransactionExpenseAdapter paymentsAdapter = new TransactionExpenseAdapter(this, samplePayments);

        transactionsList.setAdapter(transactionsAdapter);
        paymentsList.setAdapter(paymentsAdapter);
    }

    private void showTransactions() {
        paymentsListMain.setVisibility(View.GONE);
        transactionsListMain.setVisibility(View.VISIBLE);
        btnTransactions.setBackgroundColor(Color.parseColor("#6200EE")); // Selected color
        btnPayments.setBackgroundColor(Color.parseColor("#BBBBBB")); // Unselected color
    }

    private void showPayments() {
        transactionsListMain.setVisibility(View.GONE);
        paymentsListMain.setVisibility(View.VISIBLE);
        btnTransactions.setBackgroundColor(Color.parseColor("#BBBBBB")); // Unselected color
        btnPayments.setBackgroundColor(Color.parseColor("#6200EE")); // Selected color
    }
}
