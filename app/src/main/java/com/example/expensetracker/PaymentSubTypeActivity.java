package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;

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

public class PaymentSubTypeActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;

    private ListView subpaymentListView;
    private ArrayAdapter<String> subPaymentAdapter;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil;
    public ArrayList<String> readSubPaymentListfromSheet; // = new ArrayList<String>();
    //public static HashMap<String, ArrayList<String>> readSubCategoryMapfromSheet = ExpenseTrackerExcelUtil.readSubCategoriesfromExcelUtil(categoryToSubcategoriesMap);
    //public static HashMap<String, ArrayList<String>> readSubCategoryMapfromSheet = CategoryActivity.categoryToSubcategoriesMap;
    private HashMap<String, ArrayList<String>> paymentToPaymentSubTypesMap;

    private String paymentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        loadDataFromSheet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sub_type);
        subpaymentListView = findViewById(R.id.subpaymentList);

        paymentName = getIntent().getStringExtra("payment_name");
        readSubPaymentListfromSheet = paymentToPaymentSubTypesMap.get(paymentName);
        assert readSubPaymentListfromSheet != null; // checks if subcategoryList is null

        setupAdapter(); // original place
        Button addSubcatButton = findViewById(R.id.addSubpaymentButton);
        addSubcatButton.setOnClickListener(v -> showAddSubcategoryDialog(paymentName));
        //  setupAdapter(); // testing place
    }

    private void loadDataFromSheet() {
        readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE);
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet () , readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil :" + readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil);

        /*readCategoryListFromSheet = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet () 3 of 8, loadData" + readCategoryListFromSheet);*/

        paymentToPaymentSubTypesMap = readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.get(readAllPaymentTypesListandAllPaymentSubTypesMapFromExcelUtil.keySet().iterator().next());// to get value of key present at 0 index in map
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet (), categoryToSubcategoriesMap :" + paymentToPaymentSubTypesMap);
    }

    private void setupAdapter() {
        subPaymentAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item_payment_sub_type, readSubPaymentListfromSheet) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.activity_list_item_payment_sub_type, parent, false);
                }
                TextView categoryNameView = convertView.findViewById(R.id.subPaymentName);
                //  ImageButton editButton = convertView.findViewById(R.id.editButton);
                ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

                String subPayment = getItem(position);
                categoryNameView.setText(subPayment);


              /*  editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(CategoryActivity.this, SubcategoryActivity.class);
                    intent.putExtra("category_name", category);
                    startActivity(intent);
                });*/
                deleteButton.setOnClickListener(v -> confirmDeletion(position));
                return convertView;
            }

            private void confirmDeletion(int position) {
                new AlertDialog.Builder(PaymentSubTypeActivity.this).setTitle("Confirm Deletion").setMessage("Do you really want to delete this sub category?").setPositiveButton("Delete", (dialog, which) -> {
                    String subPayment = readSubPaymentListfromSheet.get(position); //meta AI
                    readSubPaymentListfromSheet.remove(position);
                    subPaymentAdapter.notifyDataSetChanged();
                    readSubPaymentListfromSheet.remove(subPayment); // testing to remove in arraylist
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            }
        /*    private void confirmDeletion(int position) {
                new AlertDialog.Builder(CategoryActivity.this).setTitle("Confirm Deletion").setMessage("Do you really want to delete this category?").setPositiveButton("Delete", (dialog, which) -> {
                    String category = readCategoryListfromSheet.get(position); //meta AI
                    readCategoryListfromSheet.remove(position);
                    categoryAdapter.notifyDataSetChanged();
                    //  CategoryActivity.categoryToSubcategoriesMap.remove(category); //meta AI
                    readCategoryListfromSheet.remove(category); // testing to remove in arraylist
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            }*/
        };
        subpaymentListView.setAdapter(subPaymentAdapter);
    }

    private void showAddSubcategoryDialog(String paymentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_payment_sub_type_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            EditText editText = customLayout.findViewById(R.id.editSubPaymentTypeText);
            String subcategory = editText.getText().toString();
            if (!subcategory.isEmpty() && !readSubPaymentListfromSheet.contains(subcategory)) {
               // ExpenseTrackerExcelUtil.writeSubTypesToExcelUtil(PAYMENT_TYPE ,paymentName, subcategory, readSubPaymentListfromSheet, paymentToSubpaymentMap);
                singleTonExpenseTrackerExcelUtil.writeSubTypesToExcelUtil(PAYMENT_TYPE ,paymentName, subcategory, readSubPaymentListfromSheet, paymentToPaymentSubTypesMap);
                subPaymentAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
