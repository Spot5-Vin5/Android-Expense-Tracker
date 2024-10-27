package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.BUDGET;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.DATE;
import static com.example.expensetracker.utilities.HeadingConstants.EMAIL;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.LIMIT;
import static com.example.expensetracker.utilities.HeadingConstants.NAME;
import static com.example.expensetracker.utilities.HeadingConstants.NOTE;
import static com.example.expensetracker.utilities.HeadingConstants.PASSWORD;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;
import static com.example.expensetracker.utilities.HeadingConstants.SUBCATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.SUBTYPES;
import static com.example.expensetracker.utilities.HeadingConstants.TRANSACTIONID;
import static com.example.expensetracker.utilities.HeadingConstants.TYPE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class NewUserActivity extends AppCompatActivity {
    public static String basePath, fileName, filePath;
    public static String email;
    private EditText editTextSignupName, editTextSignupEmail, editTextSignupPassword, editTextSignupConfirmPassword;
    private Button buttonAccountCreate;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside NewUserActivity class, inside onCreate(), 1 === started===");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        editTextSignupName = findViewById(R.id.editTextSignupName);
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextSignupConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);
        buttonAccountCreate = findViewById(R.id.buttonAccountCreate);

        System.out.println("inside NewUserActivity class, inside onCreate(), before buttonCreate button clicked");
        buttonAccountCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExcelFile();
                startActivity(new Intent(NewUserActivity.this, AppHomeActivity.class));
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createExcelFile();
        } else {

            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();

        }
    }

    private void createExcelFile() {
        System.out.println("inside NewUserActivity class, inside createExcelFile(), after buttonCreate button clicked: ===started=== ");
        email = editTextSignupEmail.getText().toString();
        if (!email.contains("@gmail.com")) {
            Toast.makeText(this, "Enter valid gmail id", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = editTextSignupPassword.getText().toString();
        String confirmPassword = editTextSignupConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

       /* String[] s = email.split("@", 2);
        String s1 = s[0];
        fileName = s1 + "_expenses.xlsx";*/
        fileName = email+ "_expensesFile.xlsx"; // check this if it works
        basePath = getExternalFilesDir(null).getAbsolutePath(); // App-specific external directory
        filePath = basePath + "/Expense Tracker App/" + fileName;

        File file = new File(filePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet expense = workbook.createSheet(EXPENSE);
            expense.createRow(0).createCell(0).setCellValue(EXPENSE);
            expense.createRow(1).createCell(0).setCellValue(TRANSACTIONID);
            expense.getRow(1).createCell(1).setCellValue(DATE);
            expense.getRow(1).createCell(2).setCellValue(AMOUNT);
            expense.getRow(1).createCell(3).setCellValue(CATEGORY);
            expense.getRow(1).createCell(4).setCellValue(SUBCATEGORY);
            expense.getRow(1).createCell(5).setCellValue(PAYMENT);
            expense.getRow(1).createCell(6).setCellValue(PAYMENT_SUBTYPE);
            expense.getRow(1).createCell(7).setCellValue(NOTE);

            //sample data insert
            expense.createRow(2).createCell(0).setCellValue(UUID.randomUUID().toString());
            expense.getRow(2).createCell(1).setCellValue("23-04-2024");
            expense.getRow(2).createCell(2).setCellValue("100");
            expense.getRow(2).createCell(3).setCellValue("Food");
            expense.getRow(2).createCell(4).setCellValue("Dinner");
            expense.getRow(2).createCell(5).setCellValue("Credit Card");
            expense.getRow(2).createCell(6).setCellValue("BOB one");
            expense.getRow(2).createCell(7).setCellValue("Party");

            expense.createRow(3).createCell(0).setCellValue(UUID.randomUUID().toString());
            expense.getRow(3).createCell(1).setCellValue("21-04-2024");
            expense.getRow(3).createCell(2).setCellValue("500");
            expense.getRow(3).createCell(3).setCellValue("Fuel");
            expense.getRow(3).createCell(4).setCellValue("Petrol");
            expense.getRow(3).createCell(5).setCellValue("Credit Card");
            expense.getRow(3).createCell(6).setCellValue("SBI BPCL");
            expense.getRow(3).createCell(7).setCellValue("Goa");

            expense.createRow(4).createCell(0).setCellValue(UUID.randomUUID().toString());
            expense.getRow(4).createCell(1).setCellValue("01-05-2024");
            expense.getRow(4).createCell(2).setCellValue("5000");
            expense.getRow(4).createCell(3).setCellValue("Fuel");
            expense.getRow(4).createCell(4).setCellValue("Petrol");
            expense.getRow(4).createCell(5).setCellValue("Credit Card");
            expense.getRow(4).createCell(6).setCellValue("SBI BPCL");
            expense.getRow(4).createCell(7).setCellValue("Goa");

           /* expense.createRow(4).createCell(0).setCellValue("28-04-2024");
            expense.getRow(4).createCell(1).setCellValue("1000");
            expense.getRow(4).createCell(2).setCellValue("Grocery");
            //expense.getRow(4).createCell(3).setCellValue("");
            expense.getRow(4).createCell(4).setCellValue("Cash");
            //expense.getRow(4).createCell(5).setCellValue("");
            //expense.getRow(4).createCell(6).setCellValue("");

            expense.createRow(5).createCell(0).setCellValue("01-05-2024");
            expense.getRow(5).createCell(1).setCellValue("2300");
            expense.getRow(5).createCell(2).setCellValue("Fuel");
            expense.getRow(5).createCell(3).setCellValue("Petrol");
            expense.getRow(5).createCell(4).setCellValue("Credit Card");
            expense.getRow(5).createCell(5).setCellValue("SBI BPCL");
            expense.getRow(5).createCell(6).setCellValue("KPHB");

            expense.createRow(6).createCell(0).setCellValue("10-05-2024");
            expense.getRow(6).createCell(1).setCellValue("2300");
            expense.getRow(6).createCell(2).setCellValue("Fuel");
            expense.getRow(6).createCell(3).setCellValue("Petrol");
            expense.getRow(6).createCell(4).setCellValue("Cash");
            //expense.getRow(6).createCell(5).setCellValue("");
            //expense.getRow(6).createCell(6).setCellValue("");*/

            XSSFSheet profileSheet = workbook.createSheet(PROFILE_ACTIVITY);
            profileSheet.createRow(0).createCell(0).setCellValue(PROFILE_ACTIVITY);
            profileSheet.createRow(1).createCell(0).setCellValue(NAME);
            profileSheet.getRow(1).createCell(1).setCellValue(editTextSignupName.getText().toString());
            profileSheet.createRow(2).createCell(0).setCellValue(EMAIL);
            profileSheet.getRow(2).createCell(1).setCellValue(editTextSignupEmail.getText().toString());
            profileSheet.createRow(3).createCell(0).setCellValue(PASSWORD);
            profileSheet.getRow(3).createCell(1).setCellValue(editTextSignupPassword.getText().toString());

            XSSFSheet categoriesSheet = workbook.createSheet(CATEGORIES);
            categoriesSheet.createRow(0).createCell(0).setCellValue(CATEGORIES);
            categoriesSheet.createRow(1).createCell(0).setCellValue(TYPE);
            categoriesSheet.getRow(1).createCell(1).setCellValue(SUBTYPES);
            //Sample data insert
            categoriesSheet.createRow(2).createCell(0).setCellValue("Food");
            categoriesSheet.getRow(2).createCell(1).setCellValue("Breakfast");

            categoriesSheet.createRow(3).createCell(0).setCellValue("Fuel");
            categoriesSheet.getRow(3).createCell(1).setCellValue("Petrol");

            categoriesSheet.createRow(4).createCell(0).setCellValue("Grocery");


            XSSFSheet paymentSheet = workbook.createSheet(PAYMENT_TYPE);
            paymentSheet.createRow(0).createCell(0).setCellValue(PAYMENT_TYPE);
            paymentSheet.createRow(1).createCell(0).setCellValue(TYPE);
            paymentSheet.getRow(1).createCell(1).setCellValue(SUBTYPES);
            // Sample data insert
            paymentSheet.createRow(2).createCell(0).setCellValue("CreditCard");
            paymentSheet.getRow(2).createCell(1).setCellValue("BOB one");
            paymentSheet.getRow(2).createCell(2).setCellValue("SBI BPCL");

            paymentSheet.createRow(3).createCell(0).setCellValue("Cash");

            XSSFSheet budgetSheet = workbook.createSheet(BUDGET);
            budgetSheet.createRow(0).createCell(0).setCellValue(BUDGET);
            budgetSheet.createRow(1).createCell(0).setCellValue(LIMIT);

            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
                Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        System.out.println("inside NewUserActivity class, inside createExcelFile(), ===ended===");
    }
}