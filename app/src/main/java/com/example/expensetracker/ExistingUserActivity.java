package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonSharedLoginVariables;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class ExistingUserActivity extends AppCompatActivity {

    private static final String TAG = "ExistingUserActivity";
    private EditText editTextSigninEmail;
    private EditText editTextSigninPassword;
    private Button buttonSigninNext;
    private Button buttonSignIn;

    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        // Initialize variables in SharedVariables
        SingleTonSharedLoginVariables sharedVariables = SingleTonSharedLoginVariables.getInstance();
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
                handleSigninNextClick(sharedVariables);
            }
        });
    }

    private void handleSigninNextClick(SingleTonSharedLoginVariables sharedVariables) {
        //email = getEmailFromEditText();
        sharedVariables.setEmail(getEmailFromEditText());
        //if (email == null) {
        if (sharedVariables.getEmail() == null) {
            showToast("Enter email to login!");
            return;
        }

        //if (isValidEmail(email)) {
        if (isValidEmail(sharedVariables)) {
            // checkEmailInDatabase(email);
            checkEmailInDatabase(sharedVariables);
        } else {
            showToast("Invalid email. Please try again.");
        }
    }

    private String getEmailFromEditText() {
        if (editTextSigninEmail != null) {
            return editTextSigninEmail.getText().toString().trim();
        }
        return "user didn't sent email!!";
    }

    //private boolean isValidEmail(String email) {
    private boolean isValidEmail(SingleTonSharedLoginVariables sharedVariables) {
        //return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return !TextUtils.isEmpty(sharedVariables.getEmail()) && android.util.Patterns.EMAIL_ADDRESS.matcher(sharedVariables.getEmail()).matches();
    }

    private void checkEmailInDatabase(SingleTonSharedLoginVariables sharedVariables) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //boolean emailExists = isEmailInDatabase(email);
                boolean emailExists = isEmailInDatabase(sharedVariables);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (emailExists) {
                            System.out.println("Email exists in database. Loading password layout.");
                            //loadPasswordLayout(email);
                            loadPasswordLayout(sharedVariables);
                        } else {
                            System.out.println("Email not found. Please check your email or sign up.");
                            showToast("Email not found. Please check your email or sign up.");
                        }
                    }
                });
            }
        }).start();
    }

    //private boolean isEmailInDatabase(String email) {
    private boolean isEmailInDatabase(SingleTonSharedLoginVariables sharedVariables) {
        // fileName = email + "_expensesFile.xlsx";
        // Log.d(TAG, "fileName: " + fileName);
        // System.out.println("fileName: " + fileName);
        sharedVariables.setFileName(sharedVariables.getEmail() + "_expensesFile.xlsx");
        System.out.println("fileName: " + sharedVariables.getFileName());

        /*basePath = getExternalFilesDir(null).getAbsolutePath();
        Log.d(TAG, "basePath: " + basePath);
        System.out.println("basePath: " + basePath);*/
        sharedVariables.setBasePath(getExternalFilesDir(null).getAbsolutePath());
        System.out.println("basePath: " + sharedVariables.getBasePath());

        /*expensesFilesAppFolder = basePath + "/Expense Tracker App/";
        Log.d(TAG, "expensesFilesAppFolder: " + expensesFilesAppFolder);
        System.out.println("expensesFilesAppFolder: " + expensesFilesAppFolder);*/
        sharedVariables.setExpensesFilesAppFolder(sharedVariables.getBasePath() + "/Expense Tracker App/");
        System.out.println("expensesFilesAppFolder: " + sharedVariables.getExpensesFilesAppFolder());

        sharedVariables.setFilePath(sharedVariables.getBasePath() + "/Expense Tracker App/" + sharedVariables.getFileName());
        System.out.println("filePath: " + sharedVariables.getBasePath() + "/Expense Tracker App/" + sharedVariables.getFileName());

        //File appFolder = new File(expensesFilesAppFolder);
        File appFolder = new File(sharedVariables.getExpensesFilesAppFolder());

        if (!appFolder.exists()) {
            boolean created = appFolder.mkdirs();
            if (!created) {
                //Log.e(TAG, "Failed to create directory: " + expensesFilesAppFolder);
                Log.e(TAG, "Failed to create directory: " + sharedVariables.getExpensesFilesAppFolder());
                System.out.println("Failed to create directory: " + sharedVariables.getExpensesFilesAppFolder());
                return false;
            }
        }

        File[] excelFiles = appFolder.listFiles((dir, name) -> name.endsWith(".xlsx") || name.endsWith(".xls"));
        if (excelFiles == null) {
            //Log.e(TAG, "No files found in directory: " + expensesFilesAppFolder);
            Log.e(TAG, "No files found in directory: " + sharedVariables.getExpensesFilesAppFolder());
            System.out.println("No files found in directory: " + sharedVariables.getExpensesFilesAppFolder());
            return false;
        }

        ArrayList<String> accountExcelFiles = new ArrayList<>();
        for (File file : excelFiles) {
            System.out.println(file.getName());
            accountExcelFiles.add(file.getName());
        }
        //return accountExcelFiles.contains(fileName);
        return accountExcelFiles.contains(sharedVariables.getFileName());
    }

    private void loadPasswordLayout(SingleTonSharedLoginVariables sharedVariables) {
        System.out.println("Loading password layout.");
        setContentView(R.layout.activity_existing_user_password);
        editTextSigninPassword = findViewById(R.id.editTextSigninPassword);
        buttonSignIn = findViewById(R.id.buttonSignin);

        if (editTextSigninPassword == null || buttonSignIn == null) {
            System.out.println("Failed to initialize password layout elements.");
            showToast("Error loading password layout. Please try again.");
            return;
        }
        //ArrayList<String> getScripts = new ArrayList<>();
        //ArrayList<String> getScripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, email);
        //TreeMap<String, String> getScripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, email);
        TreeMap<String, String> getScripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, sharedVariables);
        if (getScripts.isEmpty()) {
            System.out.println("No scripts found in database.");
            showToast("Error loading password. Please try again.");
            return;
        }
        System.out.println("scripts: " + getScripts);
        String passwordInDB = getScripts.get("Password");

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleSignInClick(passwordInDB);
            }
        });
    }

    private void handleSignInClick(String passwordInDB) {
        String signInPassword = editTextSigninPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(signInPassword) && signInPassword.equals(passwordInDB)) {

            System.out.println("SignIn Successful");
            //System.out.println("fileName: " + fileName);
            showToast("SignIn Successful");
            startActivity(new Intent(ExistingUserActivity.this, AppHomeActivity.class));
            finish();

        } else {
            showToast("Please enter correct password");
        }
    }

    private void showToast(String message) {
        Toast.makeText(ExistingUserActivity.this, message, Toast.LENGTH_LONG).show();
    }
}