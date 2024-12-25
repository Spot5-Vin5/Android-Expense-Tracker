package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.EMAIL;
import static com.example.expensetracker.utilities.HeadingConstants.NAME;
import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonSharedVariables;

public class ProfileActivity extends AppCompatActivity {
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
        System.out.println("Inside ProfileActivity class: " + "onCreate method() 2");
        // Correctly cast to TextView
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
        System.out.println("Inside ProfileActivity class: " + "onCreate method() 3");

        // Retrieve data from your database
        String nameFromDB;
        String emailFromDB;

        //var scripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY,  email);
        var scripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY,  sharedVariables);

        if (scripts.get(EMAIL).contains("@")) {
            emailFromDB = scripts.get(EMAIL);
            nameFromDB = scripts.get(NAME);
            nameTextView.setText(nameFromDB);
            emailTextView.setText(emailFromDB);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}


