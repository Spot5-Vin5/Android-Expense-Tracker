package com.example.expensetracker;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class ExistingUserActivity extends AppCompatActivity {

    private Spinner spinnerExcelFiles;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);

        spinnerExcelFiles = findViewById(R.id.spinnerExcelFiles);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Placeholder for loading Excel files
        String[] excelFiles = {"file1.xlsx", "file2.xlsx"}; // This should be dynamically loaded
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, excelFiles);
        spinnerExcelFiles.setAdapter(adapter);

        buttonLogin.setOnClickListener(view -> {
            // Validate password and handle file access here
        });
    }
}
