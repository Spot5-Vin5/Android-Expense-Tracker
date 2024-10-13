package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import com.example.expensetracker.models.TransactionModel;
import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil;

    //private ArrayList<String> readAllCategoryListFromSheet;
    private List<String> readAllCategoryListFromSheet;
    private HashMap<String, ArrayList<String>> categoryToCategoriesSubTypesMap;

    //private ArrayList<String> readPaymentListFromSheet;
    private List<String> readPaymentListFromSheet;
    private HashMap<String, ArrayList<String>> paymentToPaymentSubTypesMap;
    private DatePicker datePicker;
    private Button showDatePickerButton;
    private TextView selectedDate, categoryLabel,subCategoryLabel, paymentTypeLabel, paymentSubtypeLabel;
    private EditText amountInputField, noteInputField;
    private Button submitFormButton;
    private Spinner categorySpinner, subCategorySpinner, paymentTypeSpinner, paymentSubtypeSpinner;
    private TransactionModel transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        System.out.println("inside AddExpenseActivity class, inside onCreate () ==start== 123");
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        loadDataFromSheet();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userExpense), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        configureDatePicker();
        configureSpinners();
        handleTransactionData();
        handleSubmitButton();
        System.out.println("inside AddExpenseActivity class, inside onCreate () ==ended== 123");
    }

    private void initializeViews() {
        System.out.println("inside AddExpenseActivity class, inside initializeViews () ==started==");
        amountInputField = findViewById(R.id.amountInputField);

        datePicker = findViewById(R.id.datePicker);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        selectedDate = findViewById(R.id.selectedDate);

        categoryLabel = findViewById(R.id.CategoryLabel);
        categorySpinner = findViewById(R.id.categorySpinner);

        subCategoryLabel = findViewById(R.id.subCategoryLabel);
        subCategorySpinner = findViewById(R.id.subCategorySpinner);

        paymentTypeLabel = findViewById(R.id.paymentTypeLabel);
        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);

        paymentSubtypeLabel = findViewById(R.id.paymentSubtypeLabel);
        paymentSubtypeSpinner = findViewById(R.id.paymentSubtypeSpinner);

        noteInputField = findViewById(R.id.noteInputField);

        submitFormButton = findViewById(R.id.submitFormButton);

        System.out.println("inside AddExpenseActivity class, inside initializeViews () ==ended==");
    }

    private void configureDatePicker() {
        System.out.println("inside AddExpenseActivity class, inside configureDatePicker () ==started==");
        Calendar currentDate = Calendar.getInstance();
        datePicker.setMaxDate(currentDate.getTimeInMillis());
        showDatePickerButton.setOnClickListener(v -> datePicker.setVisibility(View.VISIBLE));
        datePicker.init(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
                    datePicker.setVisibility(View.GONE);
                });
        System.out.println("inside AddExpenseActivity class, inside configureDatePicker () ==ended==");
    }

    private void configureSpinners() {
        System.out.println("inside AddExpenseActivity class, inside configureSpinners () ==started==");
        setUpSpinner(categorySpinner, readAllCategoryListFromSheet);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                updateSubCategorySpinner(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subCategoryLabel.setVisibility(View.GONE);
                subCategorySpinner.setVisibility(View.GONE);
            }
        });
        System.out.println("inside AddExpenseActivity class, inside configureSpinners () ^^categorySpinner^^ ==ended==");
        setUpSpinner(paymentTypeSpinner, readPaymentListFromSheet);
        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPaymentType = (String) parent.getItemAtPosition(position);
                updatePaymentSubtypeSpinner(selectedPaymentType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentSubtypeLabel.setVisibility(View.GONE);
                paymentSubtypeSpinner.setVisibility(View.GONE);
            }
        });
        System.out.println("inside AddExpenseActivity class, inside configureSpinners () ^^paymentTypeSpinner^^ ==ended==");
        System.out.println("inside AddExpenseActivity class, inside configureSpinners () ==ended==");
    }

    private void updateSubCategorySpinner(String category) {
        ArrayList<String> subCategoryList = categoryToCategoriesSubTypesMap.getOrDefault(category, new ArrayList<>());
        if (!subCategoryList.isEmpty()) {
            subCategoryLabel.setVisibility(View.VISIBLE);
            subCategorySpinner.setVisibility(View.VISIBLE);
            setUpSpinner(subCategorySpinner, subCategoryList);
        } else {
            subCategoryLabel.setVisibility(View.GONE);
            subCategorySpinner.setVisibility(View.GONE);
        }
    }

    private void updatePaymentSubtypeSpinner(String paymentType) {
        if (!CASH.equals(paymentType)) {
            ArrayList<String> paymentSubtypes = paymentToPaymentSubTypesMap.getOrDefault(paymentType, new ArrayList<>());
            if (!paymentSubtypes.isEmpty()) {
                paymentSubtypeLabel.setVisibility(View.VISIBLE);
                paymentSubtypeSpinner.setVisibility(View.VISIBLE);
                setUpSpinner(paymentSubtypeSpinner, paymentSubtypes);
            }
        } else {
            paymentSubtypeLabel.setVisibility(View.GONE);
            paymentSubtypeSpinner.setVisibility(View.GONE);
        }
    }

    private void setUpSpinner(Spinner spinner, List<String> data) {
        if (data == null) {
            System.out.println("inside AddExpenseActivity class, inside setUpSpinner (), Spinner data is null for : "+ spinner.getId());
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
    }

    private void loadDataFromSheet() {

        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () ==start== ");
        readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(CATEGORIES);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil : " + readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil);
        readAllCategoryListFromSheet = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllCategoryListFromSheet : " + readAllCategoryListFromSheet);
        categoryToCategoriesSubTypesMap = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.get(readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next());// cat=catSubTypesList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () categoryToCategoriesSubTypesMap : " + categoryToCategoriesSubTypesMap);


        readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil : " + readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil);
        readPaymentListFromSheet = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readPaymentListFromSheet : " + readPaymentListFromSheet);
        paymentToPaymentSubTypesMap = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.get(readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next());// payment=paymentSubTypesList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () paymentToPaymentSubTypesMap : " + paymentToPaymentSubTypesMap);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () ==ended== ");
    }

    private void handleTransactionData() {
        System.out.println("inside AddExpenseActivity class, inside handleTransactionData () ==started==");
        transaction = (TransactionModel) getIntent().getSerializableExtra("transaction");
        if (transaction != null) {
            populateFieldsWithTransactionData();
        }
        System.out.println("inside AddExpenseActivity class, inside handleTransactionData () ==ended==");
    }

    private void populateFieldsWithTransactionData() {
        amountInputField.setText(transaction.getAmount());
        selectedDate.setText(transaction.getDate());
        setSpinnerSelection(categorySpinner, transaction.getCategoryType());
        setSpinnerSelection(paymentTypeSpinner, transaction.getPaymentType());
        setSpinnerSelection(subCategorySpinner, transaction.getCategorySubType());
        setSpinnerSelection(paymentSubtypeSpinner, transaction.getPaymentSubType());
        noteInputField.setText(transaction.getNote());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    private void handleSubmitButton() {
        System.out.println("inside AddExpenseActivity class, inside handleSubmitButton () ==started==");
        submitFormButton.setOnClickListener(v -> {
            String amount = amountInputField.getText().toString();
            String date = selectedDate.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();
            String paymentType = paymentTypeSpinner.getSelectedItem().toString();
            String subCategory = subCategorySpinner.getSelectedItem() != null ? subCategorySpinner.getSelectedItem().toString() : "";
            String paymentSubtype = paymentSubtypeSpinner.getSelectedItem() != null ? paymentSubtypeSpinner.getSelectedItem().toString() : "";
            String note = noteInputField.getText().toString().trim();

            if (amount.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            } else if (date.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else if ("Select".equals(category)) {
                Toast.makeText(AddExpenseActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
            } else if ("Select".equals(paymentType)) {
                Toast.makeText(AddExpenseActivity.this, "Please select a payment type", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, String> addExpenseDataMap = new HashMap<>();
                addExpenseDataMap.put(TRANSACTIONID, UUID.randomUUID().toString());
                addExpenseDataMap.put(AMOUNT, amount);
                addExpenseDataMap.put(DATE, date);
                addExpenseDataMap.put(CATEGORY, category);
                addExpenseDataMap.put(PAYMENT, paymentType);
                addExpenseDataMap.put(SUBCATEGORY, subCategory);
                addExpenseDataMap.put(PAYMENT_SUBTYPE, paymentSubtype);
                addExpenseDataMap.put(NOTE, note);

                String submitMessage = singleTonExpenseTrackerExcelUtil.writeAddExpenseToSheet(EXPENSE, addExpenseDataMap);
                Toast.makeText(AddExpenseActivity.this, submitMessage, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(AddExpenseActivity.this, AppHomeActivity.class);
                startActivity(intent);
            }
        });
        System.out.println("inside AddExpenseActivity class, inside handleSubmitButton () ==ended==");
    }
}
