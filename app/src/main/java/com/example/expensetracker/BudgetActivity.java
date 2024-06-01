package com.example.expensetracker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BudgetActivity extends AppCompatActivity {

    private TextView textViewLimit;
    private TextView textViewLimitValue; // Changed to TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        textViewLimit = findViewById(R.id.textViewLimit);
        textViewLimitValue = findViewById(R.id.textViewLimitValue); // Changed to TextView

        // Set click listener for the TextView
        textViewLimitValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBudgetInputDialog();
            }
        });
    }

    private void showBudgetInputDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_budget_dialog);
        dialog.setTitle("Enter Budget Limit");

        final TextView inputBudget = dialog.findViewById(R.id.editTextBudgetInput); // Changed to TextView
        Button btnSave = dialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetValue = inputBudget.getText().toString();
                textViewLimitValue.setText(budgetValue); // Update TextView instead of EditText
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
