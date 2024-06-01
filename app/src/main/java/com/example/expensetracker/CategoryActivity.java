package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;

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

import com.example.expensetracker.utilities.ExpenseTrackerExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity {
    //public static final String sheetName = CATEGORIES;
    private ListView categoryListView;
    private ArrayAdapter<String> categoryAdapter;
    //public static ArrayList<String> categoryList = new ArrayList<String>();
    public static HashMap<String, ArrayList<String>> categoryToSubcategoriesMap = new HashMap<>();
    public static ArrayList<String> readCategoryListfromSheet = ExpenseTrackerExcelUtil.readTypesFromExcelUtil(CATEGORIES,
                                                                                                                new ArrayList<String>(),
                                                                                                                categoryToSubcategoriesMap);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryListView = findViewById(R.id.categoryList);

        setupAdapter(); // original place
        Button addCatButton = findViewById(R.id.addCategoryButton);
        addCatButton.setOnClickListener(v -> showAddCategoryDialog());
        //  setupAdapter(); // testing place
    }

    private void setupAdapter() {
        categoryAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item_category, readCategoryListfromSheet) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.activity_list_item_category, parent, false);
                }
                TextView categoryNameView = convertView.findViewById(R.id.categoryName);
                ImageButton editButton = convertView.findViewById(R.id.editButton);
                ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

                String category = getItem(position);
                categoryNameView.setText(category);

                editButton.setOnClickListener(v -> {
                   // String category = getItem(position);
                    if (category != null && !category.isEmpty()) {
                   // categoryNameView.setText(category);
                        Intent intent = new Intent(CategoryActivity.this, SubcategoryActivity.class);
                        intent.putExtra("category_name", category);
                        startActivity(intent);
                    }
                });

              /*  editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(CategoryActivity.this, SubcategoryActivity.class);
                    intent.putExtra("category_name", category);
                    startActivity(intent);
                });*/

                deleteButton.setOnClickListener(v -> confirmDeletion(position));
                return convertView;
            }

            private void confirmDeletion(int position) {
                new AlertDialog.Builder(CategoryActivity.this).setTitle("Confirm Deletion").setMessage("Do you really want to delete this category?").setPositiveButton("Delete", (dialog, which) -> {
                    String category = readCategoryListfromSheet.get(position); //meta AI
                    readCategoryListfromSheet.remove(position);
                    categoryAdapter.notifyDataSetChanged();
                    //  CategoryActivity.categoryToSubcategoriesMap.remove(category); //meta AI
                    readCategoryListfromSheet.remove(category); // testing to remove in arraylist
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            }
        };
        categoryListView.setAdapter(categoryAdapter);
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_category_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            EditText editText = customLayout.findViewById(R.id.editCategoryText);
            String category = editText.getText().toString();
            if (!category.isEmpty() && !readCategoryListfromSheet.contains(category)) {
                ExpenseTrackerExcelUtil.writeTypesToExcelUtil(CATEGORIES,category, readCategoryListfromSheet, categoryToSubcategoriesMap);
                categoryAdapter.notifyDataSetChanged();
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
            String category = data.getStringExtra("category_name");
            //ArrayList<String> subcategories = data.getStringArrayListExtra("subcategories");
            //   categoryToSubcategoriesMap.put(category, subcategories);

            //meta AI
            readCategoryListfromSheet.add(category);
            categoryAdapter.notifyDataSetChanged();
        }
    }
}