package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.CASH;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.DATE;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.NOTE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.SUBCATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.TRANSACTIONID;
import static com.example.expensetracker.utilities.HeadingConstants.expenseColumnIndices;

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

import com.example.expensetracker.utilities.ExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

public class AddExpenseActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil;
    private ArrayList<String> readAllCategoryListFromSheet;
    private HashMap<String, ArrayList<String>> categoryToCategoriesSubTypesMap;
    private ArrayList<String> readPaymentListFromSheet;
    private HashMap<String, ArrayList<String>> paymentToPaymentSubTypesMap;
    private DatePicker datePicker;
    private Button showDatePickerButton;
    private TextView selectedDate, subCategoryLabel, paymentSubtypeLabel;
    private EditText amountInputField, noteInputField;
    private Button submitFormButton;
    private Spinner categorySpinner, subCategorySpinner, paymentTypeSpinner, paymentSubtypeSpinner;
    private ArrayList<String> subCategoryList = new ArrayList<>();

    private String subCategory = "";
    private String paymentSubtype = "";
    private String note = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 1 ====started====");
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        loadDataFromSheet();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 2 ====started===");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userExpense), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 3 ====started====");
        amountInputField = findViewById(R.id.amountInputField);
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () amountInputField : " + amountInputField);
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

        categorySpinner = findViewById(R.id.categorySpinner);
        subCategoryLabel = findViewById(R.id.subCategoryLabel);
        subCategorySpinner = findViewById(R.id.subCategorySpinner);
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 4 ====started====");
        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        paymentSubtypeLabel = findViewById(R.id.paymentSubtypeLabel);
        paymentSubtypeSpinner = findViewById(R.id.paymentSubtypeSpinner);
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 5 ====started====");
        noteInputField = findViewById(R.id.noteInputField);
        System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () 6 ====started====");
        submitFormButton = findViewById(R.id.submitFormButton);

        // Set up Spinners with Dummy data
/*        setUpSpinner(categorySpinner, new String[]{"select", "CategoryActivity 1", "CategoryActivity 2", "CategoryActivity 3"});
        setUpSpinner(subCategorySpinner, new String[]{"select", "Subcategory A", "Subcategory B", "Subcategory C"});
        setUpSpinner(paymentTypeSpinner, new String[]{"select", "Cash", "Credit Card", "Debit Card"});
        setUpSpinner(paymentSubtypeSpinner, new String[]{"select", "Visa", "MasterCard", "Amex"});*/

        // Set up Category type Spinners and Category subtype Spinners with real data.
        String[] categoryArray = Stream.concat(Stream.of("Select"), readAllCategoryListFromSheet.stream()).toArray(String[]::new);
        setUpSpinner(categorySpinner, categoryArray);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  categorySpinner :: onItemSelected : --start--");
                String selectedCategory = (String) parent.getItemAtPosition(position);
                subCategoryList = categoryToCategoriesSubTypesMap.getOrDefault(selectedCategory, new ArrayList<>());
                if (!subCategoryList.isEmpty()) {
                    subCategoryLabel.setVisibility(View.VISIBLE);
                    subCategorySpinner.setVisibility(View.VISIBLE);
                    subCategorySpinner.setAdapter(new ArrayAdapter<>(AddExpenseActivity.this, android.R.layout.simple_spinner_dropdown_item, subCategoryList));
                } else {
                    subCategoryLabel.setVisibility(View.GONE);
                    subCategorySpinner.setVisibility(View.GONE);
                }
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  categorySpinner :: onItemSelected : --ended--");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  categorySpinner :: onNothingSelected : --start--");
                subCategoryLabel.setVisibility(View.GONE);
                subCategorySpinner.setVisibility(View.GONE);
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  categorySpinner :: onNothingSelected : --ended--");
            }
        });

        // Set up Payment type Spinners and paymentsubtype Spinners with real data.
        String[] paymentTypeArray = Stream.concat(Stream.of("Select"), paymentToPaymentSubTypesMap.keySet().stream()).toArray(String[]::new);
        setUpSpinner(paymentTypeSpinner, paymentTypeArray);
        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  paymentTypeSpinner :: onItemSelected : --start--");
                String selectedPaymentType = (String) parent.getItemAtPosition(position);
                if (!selectedPaymentType.equals(CASH)) {
                    // if paymentType is cash no need to select sub paymentType
                    readPaymentListFromSheet = paymentToPaymentSubTypesMap.getOrDefault(selectedPaymentType, new ArrayList<>());
                    if (!readPaymentListFromSheet.isEmpty()) {
                        paymentSubtypeLabel.setVisibility(View.VISIBLE);
                        paymentSubtypeSpinner.setVisibility(View.VISIBLE);
                        paymentSubtypeSpinner.setAdapter(new ArrayAdapter<>(AddExpenseActivity.this, android.R.layout.simple_spinner_dropdown_item, readPaymentListFromSheet));
                    }
                } else {
                    paymentSubtypeLabel.setVisibility(View.GONE);
                    paymentSubtypeSpinner.setVisibility(View.GONE);
                }
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  paymentTypeSpinner :: onItemSelected : --ended--");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  paymentTypeSpinner :: onNothingSelected : --start--");
                paymentSubtypeLabel.setVisibility(View.GONE);
                paymentSubtypeSpinner.setVisibility(View.GONE);
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  paymentTypeSpinner :: onNothingSelected : --ended--");
            }
        });

        submitFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountInputField.getText().toString();
                String date = selectedDate.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String paymentType = paymentTypeSpinner.getSelectedItem().toString();

                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  ::)" + "amount :" + amount + ", date :" + date + ",  category :" + category + ", " + ", paymentType :" + paymentType);

                boolean isSubCategoryEmpty = true;
                boolean isPaymentSubtypeEmpty = true;
                boolean isNoteEmpty = true; // Assuming note is empty by default

                if (subCategorySpinner != null) {
                    isSubCategoryEmpty = subCategorySpinner.getSelectedItem().toString().isEmpty();
                    subCategory = subCategorySpinner.getSelectedItem().toString();
                }
                if (paymentSubtypeSpinner != null) {
                    isPaymentSubtypeEmpty = paymentSubtypeSpinner.getSelectedItem().toString().isEmpty();
                    paymentSubtype = paymentSubtypeSpinner.getSelectedItem().toString();
                }
                if (noteInputField != null) {
                    isNoteEmpty = noteInputField.getText().toString().trim().isEmpty();
                    note = noteInputField.getText().toString().trim();
                    System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity ()  ::)" + "subCategory :" + subCategory + ", paymentSubtype :" + paymentSubtype + ", note :" + note);
                }

                HashMap<String, String> addExpenseDataMap = new HashMap<>(); // map to carry data for each expense
                System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () addExpenseDataMap ::starting :" +addExpenseDataMap);

                if (amount.isEmpty()) {
                    System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () amount.isEmpty() -if-");
                    Toast.makeText(AddExpenseActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                } else if (date.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else if (category.isEmpty() || category.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } /*else if (subCategory.isEmpty() || subCategory.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a sub category", Toast.LENGTH_SHORT).show();
                }*/ else if (paymentType.isEmpty() || paymentType.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a payment type", Toast.LENGTH_SHORT).show();
                } /*else if (paymentSubtype.isEmpty() || paymentSubtype.equals("select")) {
                    Toast.makeText(AddExpenseActivity.this, "Please select a payment sub type", Toast.LENGTH_SHORT).show();
                } */ else {
                    // All mandatory fields are filled, proceed with form submission
                    // Submit the form

                    addExpenseDataMap.put(TRANSACTIONID, UUID.randomUUID().toString());
                    addExpenseDataMap.put(AMOUNT, amount);
                    addExpenseDataMap.put(DATE, date);
                    addExpenseDataMap.put(CATEGORY, category);
                    addExpenseDataMap.put(PAYMENT, paymentType);
                    addExpenseDataMap.put(SUBCATEGORY, subCategory);
                    addExpenseDataMap.put(PAYMENT_SUBTYPE, paymentSubtype);
                    addExpenseDataMap.put(NOTE, note);

                  /*  if (!isSubCategoryEmpty) {
                        addExpenseDataMap.put(SUBCATEGORY, subCategory);
                    }
                    if (!isPaymentSubtypeEmpty) {
                        addExpenseDataMap.put(PAYMENT_SUBTYPE, paymentSubtype);
                    }
                    if (!isNoteEmpty) {
                        addExpenseDataMap.put(NOTE, note);
                    }*/
                    System.out.println("inside AddExpenseActivity class, inside AddExpenseActivity () addExpenseDataMap ::)" + addExpenseDataMap);
                    String submitMessage = singleTonExpenseTrackerExcelUtil.writeAddExpenseToSheet(EXPENSE, addExpenseDataMap);
                    Toast.makeText(AddExpenseActivity.this, submitMessage, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(AddExpenseActivity.this, AppHomeActivity.class));
                }
            }
        });
    }

    private void loadDataFromSheet() {
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () ==start== ");
        readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(CATEGORIES);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil : " + readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil);
        readAllCategoryListFromSheet = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next();//categoryList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllCategoryListFromSheet : " + readAllCategoryListFromSheet);
        categoryToCategoriesSubTypesMap = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.get(readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next());// cat=catSubTypesList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () categoryToCategoriesSubTypesMap : " + categoryToCategoriesSubTypesMap);


        readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil : " + readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil);
        readPaymentListFromSheet = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next();//PaymentTypeList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () readPaymentListFromSheet : " + readPaymentListFromSheet);
        paymentToPaymentSubTypesMap = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.get(readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next());// payment=paymentSubTypesList
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () paymentToPaymentSubTypesMap : " + paymentToPaymentSubTypesMap);
        System.out.println("inside AddExpenseActivity class, inside loadDataFromSheet () ==ended== ");
    }

    // Method to set up a Spinner
    private void setUpSpinner(Spinner spinner, String[] data) {
        // Create an ArrayAdapter using the data and the spinner layout
        System.out.println("inside AddExpenseActivity class, inside setUpSpinner () : =start== ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
        System.out.println("inside AddExpenseActivity class, inside setUpSpinner () : =end== ");
    }

}
