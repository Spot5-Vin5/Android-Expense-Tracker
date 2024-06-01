package com.example.expensetracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonExistingUser, buttonNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Inside MainActivity class: " + "onCreate() ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonExistingUser = findViewById(R.id.buttonExistingUser);
        buttonNewUser = findViewById(R.id.buttonNewUser);

        buttonExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Inside MainActivity class: " + "onCreate()-> buttonExistingUser clicked");
                // Intent to navigate to Existing User Activity
                startActivity(new Intent(MainActivity.this, ExistingUserActivity.class));
            }
        });

        System.out.println("inside main class,=== before new button click===");
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Inside MainActivity class: " + "onCreate()-> buttonNewUser clicked");

                if (checkPermission()) {

                    startActivity(new Intent(MainActivity.this, NewUserActivity.class));

                } else {
                    requestStoragePermission();
                }

                // Intent to navigate to New User Activity
                //  startActivity(new Intent(MainActivity.this, NewUserActivity.class));
            }
        });

        /*System.out.println("inside main class,=== before new button click===");
        buttonNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent to navigate to New User Activity
                startActivity(new Intent(MainActivity.this, AppHomeActivity.class));
            }
        });*/

    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
}