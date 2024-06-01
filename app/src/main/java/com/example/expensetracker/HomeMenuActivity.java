package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeMenuActivity extends AppCompatActivity {

    private Button categoriesButton,accountTypeButton,budgetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoriesButton = findViewById(R.id.categoriesButton);
        // Setting onClick listeners for each button
        if (categoriesButton != null) {
            categoriesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to navigate to Profile Activity
                    startActivity(new Intent(HomeMenuActivity.this, CategoryActivity.class));
                }
            });
        }

        accountTypeButton = findViewById(R.id.accountTypeButton);
        // Setting onClick listeners for each button
        if (accountTypeButton != null) {
            accountTypeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to navigate to Profile Activity
                    startActivity(new Intent(HomeMenuActivity.this, PaymentTypeActivity.class));
                }
            });
        }


        budgetButton = findViewById(R.id.budgetButton);
        // Setting onClick listeners for each button
        if (budgetButton != null) {
            budgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to navigate to Profile Activity
                    startActivity(new Intent(HomeMenuActivity.this, BudgetActivity.class));
                }
            });
        }
    }
}