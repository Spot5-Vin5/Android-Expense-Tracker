package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.expensetracker.utilities.ExpenseTrackerExcelUtil;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Inside ProfileActivity class: " + "onCreate method()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Correctly cast to TextView
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);

     /*   // Retrieve data from your database (replace with actual data)
        String nameFromDB = "John Doe"; // Example name
        String emailFromDB = "john.doe@example.com"; // Example email

        nameTextView.setText(nameFromDB);
        emailTextView.setText(emailFromDB);*/

        // Retrieve data from your database
        String nameFromDB;
        String emailFromDB;

        ArrayList<String> getScripts = new ArrayList<>();
        ArrayList<String> scripts = ExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY, getScripts);
        if (scripts.get(0).contains("@")) {
            emailFromDB = scripts.get(0);
            nameFromDB = scripts.get(1);
        } else {
            nameFromDB = scripts.get(0);
            emailFromDB = scripts.get(1);
        }

        nameTextView.setText(nameFromDB);
        emailTextView.setText(emailFromDB);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}


