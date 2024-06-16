package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;

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

public class SubcategoryActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;

    private ListView subcategoryListView;
    private ArrayAdapter<String> subcategoryAdapter;
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil;
    public ArrayList<String> readSubCategoryListFromSheet;
    private HashMap<String, ArrayList<String>> categoryToCategoriesSubTypesMap;

    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        loadDataFromSheet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        subcategoryListView = findViewById(R.id.subcategoryList);

        categoryName = getIntent().getStringExtra("category_name");
        readSubCategoryListFromSheet = categoryToCategoriesSubTypesMap.get(categoryName);
        assert readSubCategoryListFromSheet != null; // checks if subcategoryList is null

        setupAdapter(); // original place
        Button addSubcatButton = findViewById(R.id.addSubcategoryButton);
        addSubcatButton.setOnClickListener(v -> showAddSubcategoryDialog(categoryName));
        //  setupAdapter(); // testing place
    }

    private void loadDataFromSheet() {
        readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(CATEGORIES);
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet () , readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil :" + readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil);

       /* readCategoryListFromSheet = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet () 3 of 8, loadData" + readCategoryListFromSheet);*/

        categoryToCategoriesSubTypesMap = readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.get(readAllCategoryTypesListandAllCategorySubTypesMapFromExcelUtil.keySet().iterator().next());// to get value of key present at 0 index in map
        System.out.println("inside SubcategoryActivity class, inside loadDataFromSheet (), categoryToSubcategoriesMap :" + categoryToCategoriesSubTypesMap);

    }

    private void setupAdapter() {
        subcategoryAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item_sub_category, readSubCategoryListFromSheet) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.activity_list_item_sub_category, parent, false);
                }
                TextView categoryNameView = convertView.findViewById(R.id.subCategoryName);
                //  ImageButton editButton = convertView.findViewById(R.id.editButton);
                ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

                String subCategory = getItem(position);
                categoryNameView.setText(subCategory);

              /*  editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(CategoryActivity.this, SubcategoryActivity.class);
                    intent.putExtra("category_name", category);
                    startActivity(intent);
                });*/
                deleteButton.setOnClickListener(v -> confirmDeletion(position));
                return convertView;
            }

            private void confirmDeletion(int position) {
                new AlertDialog.Builder(SubcategoryActivity.this).setTitle("Confirm Deletion").setMessage("Do you really want to delete this sub category?").setPositiveButton("Delete", (dialog, which) -> {
                    String subCategory = readSubCategoryListFromSheet.get(position); //meta AI
                    readSubCategoryListFromSheet.remove(position);
                    subcategoryAdapter.notifyDataSetChanged();
                    readSubCategoryListFromSheet.remove(subCategory); // testing to remove in arraylist
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
        subcategoryListView.setAdapter(subcategoryAdapter);
    }

    private void showAddSubcategoryDialog(String categoryName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_subcategory_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            EditText editText = customLayout.findViewById(R.id.editSubCategoryText);
            String subcategory = editText.getText().toString();
            if (!subcategory.isEmpty() && !readSubCategoryListFromSheet.contains(subcategory)) {
                //  ExpenseTrackerExcelUtil.writeSubTypesToExcelUtil(CATEGORIES ,categoryName, subcategory, readSubCategoryListFromSheet, categoryToSubcategoriesMap);
                singleTonExpenseTrackerExcelUtil.writeSubTypesToExcelUtil(CATEGORIES, categoryName, subcategory, readSubCategoryListFromSheet, categoryToCategoriesSubTypesMap);
                subcategoryAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
