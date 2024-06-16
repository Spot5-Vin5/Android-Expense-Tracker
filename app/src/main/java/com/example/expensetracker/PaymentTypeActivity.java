package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentTypeActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private ListView paymentListView;
    private ArrayAdapter<String> paymentAdapter;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil;
    private HashMap<String, ArrayList<String>> paymentToPaymentSubTypesMap;
    private ArrayList<String> readPaymentListFromSheet;/* = ExpenseTrackerExcelUtil.readTypesFromExcelUtil(PAYMENT_TYPE,new ArrayList<String>(), paymentToSubpaymentMap);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        loadDataFromExcel();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);
        paymentListView = findViewById(R.id.paymentList);

        setupAdapter(); // original place
        Button addCatButton = findViewById(R.id.addPaymentButton);
        addCatButton.setOnClickListener(v -> showAddCategoryDialog());
        //  setupAdapter(); // testing place
    }

    private void loadDataFromExcel() {
        readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE);
        System.out.println("inside PaymentTypeActivity class, inside loadDataFromExcel () , readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil :" + readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil);

        readPaymentListFromSheet = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside PaymentTypeActivity class, inside loadDataFromExcel () 3 of 8, readPaymentListFromSheet :" + readPaymentListFromSheet);

        paymentToPaymentSubTypesMap = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.get(readPaymentListFromSheet);// to get value of key present at 0 index in map
        System.out.println("inside PaymentTypeActivity class, inside loadDataFromExcel (), paymentToSubpaymentMap :" + paymentToPaymentSubTypesMap);
    }

    private void setupAdapter() {
        paymentAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item_payment_type, readPaymentListFromSheet) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.activity_list_item_payment_type, parent, false);
                }
                TextView paymentNameView = convertView.findViewById(R.id.paymentTypeName);
                ImageButton editpayButton = convertView.findViewById(R.id.editPayButton);
                ImageButton deletepayButton = convertView.findViewById(R.id.deletePayButton);

                String paymentType = getItem(position);
                paymentNameView.setText(paymentType);

                editpayButton.setOnClickListener(v -> {
                    // String category = getItem(position);
                    if (paymentType != null && !paymentType.isEmpty()) {
                        // categoryNameView.setText(category);
                        Intent intent = new Intent(PaymentTypeActivity.this, PaymentSubTypeActivity.class);
                        intent.putExtra("payment_name", paymentType);
                        startActivity(intent);
                    }
                });

                deletepayButton.setOnClickListener(v -> confirmDeletion(position));
                return convertView;
            }

            private void confirmDeletion(int position) {
                new AlertDialog.Builder(PaymentTypeActivity.this).setTitle("Confirm Deletion").setMessage("Do you really want to delete this category?").setPositiveButton("Delete", (dialog, which) -> {
                    String payment = readPaymentListFromSheet.get(position); //meta AI
                    readPaymentListFromSheet.remove(position);
                    paymentAdapter.notifyDataSetChanged();
                    //  CategoryActivity.categoryToSubcategoriesMap.remove(category); //meta AI
                    readPaymentListFromSheet.remove(payment); // testing to remove in arraylist
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            }
        };
        paymentListView.setAdapter(paymentAdapter);
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_payment_type_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            EditText editText = customLayout.findViewById(R.id.editPaymentTypeText);
            String payment = editText.getText().toString();
            if (!payment.isEmpty() && !readPaymentListFromSheet.contains(payment)) {
                //  ExpenseTrackerExcelUtil.writeTypesToExcelUtil(PAYMENT_TYPE,payment, readPaymentListfromSheet, paymentToSubpaymentMap);//edit
                singleTonExpenseTrackerExcelUtil.writeTypesToExcelUtil(PAYMENT_TYPE, payment, readPaymentListFromSheet, paymentToPaymentSubTypesMap);//edit
                paymentAdapter.notifyDataSetChanged();
                // setupAdapter(); //testing to refresh adapter even after notifying
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == SUBCATEGORY_REQUEST_CODE && resultCode == RESULT_OK)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String payment = data.getStringExtra("category_name");
            //ArrayList<String> subcategories = data.getStringArrayListExtra("subcategories");
            //   categoryToSubcategoriesMap.put(category, subcategories);

            //meta AI
            readPaymentListFromSheet.add(payment);
            paymentAdapter.notifyDataSetChanged();
        }
    }
}