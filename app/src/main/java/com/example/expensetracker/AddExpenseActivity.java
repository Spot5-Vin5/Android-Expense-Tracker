package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.DATE;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.NOTE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.SUBCATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.expenseColumnIndices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensetracker.utilities.ExpenseTrackerExcelUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {

    String note = null;
    //String note = "";

    //public HashMap<String, String> addExpenseDataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatePicker datePicker;
        Button showDatePickerButton;
        TextView selectedDate;
        EditText amountInputField, noteInputField;
        Button submitFormButton;
        Spinner categorySpinner;
        Spinner subCategorySpinner;
        Spinner paymentTypeSpinner;
        Spinner paymentSubtypeSpinner;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userExpense), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        amountInputField = findViewById(R.id.amountInputField);
        //amountInputField = <EditText>findViewById(R.id.amountInputField);
        //String amount = amountInputField.getText().toString();// test as the value is string.
        //String amount  = String.valueOf(amountInputField.getText());

        datePicker = findViewById(R.id.datePicker);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Set the maximum date of the DatePicker to the current date
        datePicker.setMaxDate(currentDate.getTimeInMillis());

        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        selectedDate = findViewById(R.id.selectedDate);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year); // - -> /
                datePicker.setVisibility(View.GONE); // This line will hide the DatePicker after a date is selected
            }
        });


        //  String date = selectedDate.getText().toString();

        categorySpinner = findViewById(R.id.categorySpinner);
        //  String category = categorySpinner.getTransitionName();
        subCategorySpinner = findViewById(R.id.subCategorySpinner);
        // String subCategory = subCategorySpinner.getTransitionName();

        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        // String paymentType = paymentTypeSpinner.getTransitionName();
        paymentSubtypeSpinner = findViewById(R.id.paymentSubtypeSpinner);
        // String paymentSubtype = paymentSubtypeSpinner.getTransitionName();

        noteInputField = findViewById(R.id.noteInputField);
       /* if (noteInputField != null) {
            boolean isNoteEmpty = noteInputField.getText().toString().trim().isEmpty();
        }*/

      /*  boolean isEmpty = noteInputField.getText().toString().trim().isEmpty();
        if (!isNoteEmpty) {
            note = noteInputField.getText().toString().trim();
            //Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
        }*/


        submitFormButton = findViewById(R.id.submitFormButton);

        // Set up Spinners with Dummy data
        setUpSpinner(categorySpinner, new String[]{"select", "CategoryActivity 1", "CategoryActivity 2", "CategoryActivity 3"});
        setUpSpinner(subCategorySpinner, new String[]{"select", "Subcategory A", "Subcategory B", "Subcategory C"});
        setUpSpinner(paymentTypeSpinner, new String[]{"select", "Cash", "Credit Card", "Debit Card"});
        setUpSpinner(paymentSubtypeSpinner, new String[]{"select", "Visa", "MasterCard", "Amex"});

        submitFormButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*String amount = amountInputField.getText().toString();
                String date = selectedDate.getText().toString();
                String category = categorySpinner.getTransitionName();
                String subCategory = subCategorySpinner.getTransitionName();
                String paymentType = paymentTypeSpinner.getTransitionName();
                String paymentSubtype = paymentSubtypeSpinner.getTransitionName();*/

                String amount = amountInputField.getText().toString();
                String date = selectedDate.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String subCategory = subCategorySpinner.getSelectedItem().toString();
                String paymentType = paymentTypeSpinner.getSelectedItem().toString();
                String paymentSubtype = paymentSubtypeSpinner.getSelectedItem().toString();

                boolean isNoteEmpty = true; // Assuming note is empty by default
                if (noteInputField != null) {
                    isNoteEmpty = noteInputField.getText().toString().trim().isEmpty();
                    note = noteInputField.getText().toString().trim();
                }

                 HashMap<String, String> addExpenseDataMap = new HashMap<>(); // map to carry data for each expense

                if (amount.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                } else if (date.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else if (category.isEmpty() || category.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } else if (subCategory.isEmpty() || subCategory.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a sub category", Toast.LENGTH_SHORT).show();
                } else if (paymentType.isEmpty() || paymentType.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a payment type", Toast.LENGTH_SHORT).show();
                } else if (paymentSubtype.isEmpty() || paymentSubtype.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a payment sub type", Toast.LENGTH_SHORT).show();
                } else {
                    // All mandatory fields are filled, proceed with form submission
                    // Submit the form
                    addExpenseDataMap.put(AMOUNT, amount);
                    addExpenseDataMap.put(DATE, date);
                    addExpenseDataMap.put(CATEGORY, category);
                    addExpenseDataMap.put(SUBCATEGORY, subCategory);
                    addExpenseDataMap.put(PAYMENT, paymentType);
                    addExpenseDataMap.put(PAYMENT_SUBTYPE, paymentSubtype);
                    if (!isNoteEmpty) {
                        addExpenseDataMap.put(NOTE, note);
                    }

                    String submitMessage = ExpenseTrackerExcelUtil.writeAddExpenseToSheet(EXPENSE, addExpenseDataMap);
                    Toast.makeText(AddExpenseActivity.this, submitMessage, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(AddExpenseActivity.this, AppHomeActivity.class));
                }
            }
        });
    }

    // Method to set up a Spinner
    private void setUpSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
    }
}
