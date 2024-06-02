package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.models.PaymentsModel;
import com.example.expensetracker.models.TransactionModel;
import com.example.expensetracker.utilities.PaymentTypeExpenseAdapter;
import com.example.expensetracker.utilities.TransactionExpenseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllButtonActivity extends AppCompatActivity {

    private ListView transactionsList;
    private ListView paymentsList;
    private Button btnTransactions;
    private Button btnPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // By default, show Transactions
        showTransactions();

        // Initialize the transactionlist and paymentslist with sample data
        List<TransactionModel> transactions = new ArrayList<>();
        transactions.add(new TransactionModel("21-05-2024", "100", "Food", "Lunch", "Credit card", "ICICI", "party"));
        transactions.add(new TransactionModel("23-05-2024", "300", "Food", "Dinner", "Credit card", "1Card", "party"));
        transactions.add(new TransactionModel("25-05-2024", "5000", "Fuel", "Petrol", "Credit card", "SBI BPCL", "Travel"));
        transactions.add(new TransactionModel("29-05-2024", "300", "Shopping", "Petrol", "cash", "", "Travel"));
        transactions.add(new TransactionModel("5-05-2024", "5020", "Bills", "", "Credit card", "SBI BPCL", "Travel"));
        transactions.add(new TransactionModel("19-05-2024", "200", "Grociries", "", "cash", "", ""));

        List<PaymentsModel> paymentTypes = new ArrayList<>();
     /*   AppHomeActivity appHomeActivity = new AppHomeActivity();
        HashMap<String, Integer> v = appHomeActivity.getEachPaymentTypeAmount();
        for (String payment : v.keySet()) {
            paymentTypes.add(new PaymentsModel(payment, v.get(payment).toString()));
        }*/
        paymentTypes.add(new PaymentsModel("ICICI", "1000"));
        paymentTypes.add(new PaymentsModel("1Card", "2000"));
        paymentTypes.add(new PaymentsModel("1Card", "4000"));
        paymentTypes.add(new PaymentsModel("Cash", "5000"));

        TransactionExpenseAdapter transactionsAdapter = new TransactionExpenseAdapter(this, transactions);
        PaymentTypeExpenseAdapter paymentsAdapter = new PaymentTypeExpenseAdapter(this, paymentTypes);

        transactionsList.setAdapter(transactionsAdapter);
        paymentsList.setAdapter(paymentsAdapter);
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
