package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.EMAIL;
import static com.example.expensetracker.utilities.HeadingConstants.NAME;
import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonSharedVariables;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameTextView, emailTextView;
    private Button backUpButton, logoutButton;
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        // Initialize variables in SharedVariables
        SingleTonSharedVariables sharedVariables = SingleTonSharedVariables.getInstance();

        Log.i(TAG, "Inside ProfileActivity class: " + "onCreate method()");
        System.out.println("Inside ProfileActivity class: " + "onCreate method()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        System.out.println("Inside ProfileActivity class: " + "onCreate method() 2");
        // Initialize UI elements
        initializeUI();

        System.out.println("Inside ProfileActivity class: " + "onCreate method() 3");
        // Retrieve data from your database
        loadDataAndDisplay(sharedVariables);

        logoutButtonOperation();
    }

    private void logoutButtonOperation() {
        logoutButton.setOnClickListener(v ->{
            // Finish the ProfileActivity to remove it from the stack
            finish();  // Optional, if you just want to finish this activity.

            // Start LoginActivity and clear all activities before it
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clears the activity stack
            startActivity(intent);

            // Optionally, finish this activity if it's still active (shouldn't be necessary if the flag is set)
            finishAffinity(); // Ensures no other activities remain in the stack.
        });
    }

    private void initializeUI() {
        // Correctly cast to TextView
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        //backUpButton = findViewById(R.id.backUpButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void loadDataAndDisplay(SingleTonSharedVariables sharedVariables) {
        String nameFromDB;
        String emailFromDB;

        var scripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, sharedVariables);

        if (scripts.get(EMAIL).contains("@")) {
            emailFromDB = scripts.get(EMAIL);
            nameFromDB = scripts.get(NAME);
            nameTextView.setText(nameFromDB);
            emailTextView.setText(emailFromDB);
        }
    }

    //Below method is useful when logout button layout is having android:onClick="onLogoutClick" attribute that's why method name also same as attribute value!!
/*    public void onLogoutClick(View view) {

        // Finish the ProfileActivity to remove it from the stack
        finish();  // Optional, if you just want to finish this activity.

        // Start LoginActivity and clear all activities before it
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clears the activity stack
        startActivity(intent);

        // Optionally, finish this activity if it's still active (shouldn't be necessary if the flag is set)
        finishAffinity(); // Ensures no other activities remain in the stack.
    }*/

}


