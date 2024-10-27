package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;

import java.io.File;
import java.util.ArrayList;

public class ExistingUserActivity extends AppCompatActivity {

    private EditText editTextSigninEmail;
    private EditText editTextSigninPassword;
    private Button buttonSigninNext;
    private Button buttonSignIn;
    public static String basePath, fileName, filePath, expensesFilesAppFolder;
    public static String email;
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        // Initially set content view to email layout
        setContentView(R.layout.activity_existing_user);
        // Initialize email layout elements
        editTextSigninEmail = findViewById(R.id.editTextSigninEmail);
        buttonSigninNext = findViewById(R.id.buttonSigninNext);
        // Set up click listener for the 'Next' button
        buttonSigninNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextSigninEmail.getText().toString().trim();
                if (isValidEmail(email)) {
                    if (isEmailInDatabase(email)) {
                        // Switch to password layout on successful email validation and existence in the database
                        loadPasswordLayout();
                    } else {
                        // Show error message if email is not found in the database
                        Toast.makeText(ExistingUserActivity.this, "Email not found. Please check your email or sign up.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show error message if email format is invalid
                    Toast.makeText(ExistingUserActivity.this, "Invalid email. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email.contains("@gmail.com")) {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isEmailInDatabase(String email) {

        fileName = email + "_expensesFile.xlsx"; // check this if it works
        basePath = getExternalFilesDir(null).getAbsolutePath(); // App-specific external directory
        expensesFilesAppFolder = basePath + "/Expense Tracker App/"; // Create the folder path
        // Construct the full file path
        //filePath = basePath + "/Expense Tracker App/" + fileName;

        // Create a File object for the folder
        //File folder = new File(basePath);
        File appFolder = new File(expensesFilesAppFolder);
        // List all files in the folder
        File[] excelFiles = appFolder.listFiles((dir, name) -> name.endsWith(".xlsx") || name.endsWith(".xls"));
        // Store file names in an ArrayList
        ArrayList<String> accountExcelFiles = new ArrayList<>();
        for (File fileName : excelFiles) {
            accountExcelFiles.add(fileName.getName());
        }
        if (accountExcelFiles.contains(fileName)) {
            return true;
        }
        return false;
    }

    private void loadPasswordLayout() {
        // Switch to password layout
        setContentView(R.layout.activity_existing_user_password);
        // Initialize password layout elements
        editTextSigninPassword = findViewById(R.id.editTextSigninPassword);
        buttonSignIn = findViewById(R.id.buttonSignin);
        // Set up click listener for 'Sign In' button

        ArrayList<String> getScripts = new ArrayList<>();
        ArrayList<String> scripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, getScripts);
        String passwordInDB = scripts.get(0);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signInPassword = editTextSigninPassword.getText().toString().trim();
                // Validate password as needed (e.g., non-empty or more checks)
                if (!TextUtils.isEmpty(signInPassword) && signInPassword.equals(passwordInDB)) {
                    Toast.makeText(ExistingUserActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                    // Proceed with login or navigate to the next activity
                    startActivity(new Intent(ExistingUserActivity.this, AppHomeActivity.class));
                } else {
                    Toast.makeText(ExistingUserActivity.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}