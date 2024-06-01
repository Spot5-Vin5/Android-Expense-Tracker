package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.expenseColumnIndices;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensetracker.models.HomeCategoryModel;
import com.example.expensetracker.utilities.ExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.HomeCategoryAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppHomeActivity extends AppCompatActivity {


    private Button appHomeMenu, addExpense, profile, viewAll;
    //private TextView balanceInfoText;
    private PieChart pieChart;

    private HashMap<String, ArrayList<String>> categoryToSubcategoriesMap = new HashMap<>();
    private ArrayList<String> readAllCategoryListFromSheet; //= new ArrayList<>();
    private HashMap<String, ArrayList<String>> paymentToSubPaymentMap = new HashMap<>();
    private ArrayList<String> readAllPaymentListFromSheet; //= new ArrayList<>();
    private ArrayList<String> readAllSubPaymentListFromSheet; //= new ArrayList<>();
    private ArrayList<HashMap<String, String>> readExpenseDataRowMapListFromExcel;// = new ArrayList<>();

    private int totalMonthExpense = 0;
    private HashMap<String, Integer> eachCategoryAmount = new HashMap<>();
    private HashMap<String, Integer> eachPaymentTypeAmount = new HashMap<>();

    private static WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside AppHomeActivity class, 1 onCreate(), ===started===");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appHomeMain), AppHomeActivity::onApplyWindowInsets);
        System.out.println("inside AppHomeActivity class, inside 2 onCreate(), before initializeUI ()");
        // Initialize UI elements
        initializeUI();
        System.out.println("inside AppHomeActivity class,inside 3 onCreate(), before loadData()");
        // Load data
        loadData();
        System.out.println("inside AppHomeActivity class,inside 4 onCreate(), before setupListView()");
        // Set up ListView
        setupListView(eachCategoryAmount);
        System.out.println("inside AppHomeActivity class,inside 5 onCreate(), before setupPieChart(), setupPieChart() ");
        // Set up Pie Chart
        setupPieChart();
        System.out.println("inside AppHomeActivity class,inside 6 onCreate(), before setupPieChart(), loadPieChartData() ");
        loadPieChartData();
        System.out.println("inside AppHomeActivity class, 7 onCreate(), ===ended===");
    }

    private void initializeUI() {
        System.out.println("inside AppHomeActivity class, inside initializeUI () 1 of 3");
        profile = findViewById(R.id.profile);
        addExpense = findViewById(R.id.addExpense);
        appHomeMenu = findViewById(R.id.appHomeMenu);
        pieChart = findViewById(R.id.pie_chart_container);
        viewAll = findViewById(R.id.view_all_button);

        System.out.println("inside AppHomeActivity class, inside initializeUI () 2 of 3");
        if (profile != null) {
            profile.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, ProfileActivity.class)));
        }

        if (addExpense != null) {
            addExpense.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, AddExpenseActivity.class)));
        }

        if (appHomeMenu != null) {
            appHomeMenu.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, HomeMenuActivity.class)));
        }
        if (viewAll != null) {
            viewAll.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, ViewAllButtonActivity.class)));
        }
        System.out.println("inside AppHomeActivity class, inside initializeUI () 3 of 3");
    }


    private void loadData() {
        System.out.println("inside AppHomeActivity class, inside loadData () 1 of 8, ==Started==");
        readAllCategoryListFromSheet = ExpenseTrackerExcelUtil.readTypesFromExcelUtil(CATEGORIES, new ArrayList<String>(), categoryToSubcategoriesMap);
        System.out.println("inside AppHomeActivity class, inside loadData () 2 of 8, readAllCategoryListFromSheet :" + readAllCategoryListFromSheet);
        readAllPaymentListFromSheet = ExpenseTrackerExcelUtil.readTypesFromExcelUtil(PAYMENT_TYPE, new ArrayList<String>(), paymentToSubPaymentMap);
        System.out.println("inside AppHomeActivity class, inside loadData () 3 of 8, readAllPaymentListFromSheet" + readAllPaymentListFromSheet);
        readAllSubPaymentListFromSheet = ExpenseTrackerExcelUtil.readAllSubPaymentsFromExcel(paymentToSubPaymentMap);
        System.out.println("inside AppHomeActivity class, inside loadData () 4 of 8, readAllSubPaymentListFromSheet :" + readAllSubPaymentListFromSheet);
        readExpenseDataRowMapListFromExcel = ExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>());
        System.out.println("inside AppHomeActivity class, inside loadData () 5 of 8, readExpenseDataRowMapListFromExcel :" + readExpenseDataRowMapListFromExcel);
        System.out.println("inside AppHomeActivity class, inside loadData () 6 of 8, before calculateTotalMonthExpense ()");

        calculateTotalMonthExpense();
        System.out.println("inside AppHomeActivity class, inside loadData () 7 of 8, before calculateEachCategoryAmount()");
        calculateEachCategoryAmount();
        System.out.println("inside AppHomeActivity class, inside loadData () 8 of 8, before calculateEachPaymentTypeAmount()");
        calculateEachPaymentTypeAmount();
        System.out.println("inside AppHomeActivity class, inside loadData () 8 of 8, ==ended==");
    }

    private void setupListView(HashMap<String, Integer> eachCategoryAmount) {
        System.out.println("inside AppHomeActivity class, inside setupListView () 1, ==Started==");
        ListView homeCategoryList = findViewById(R.id.home_category_model_list);

        System.out.println("inside AppHomeActivity class, inside setupListView () 2" + homeCategoryList);
        // Create a list of HomeCategoryModel objects

       /* // Populate the list with your data
        expenses.add(new HomeCategoryModel(R.drawable.some_icon, "Expense 1", "$100", "16.7%"));
        expenses.add(new HomeCategoryModel(R.drawable.some_icon, "Expense 2", "$200", "33.3%"));
        expenses.add(new HomeCategoryModel(R.drawable.some_icon, "Expense 3", "$300", "40%"));*/

        // Populate the list with dynamic data from excel
        System.out.println("inside AppHomeActivity class, inside setupListView () 3, setExpenses() calling :");
        List<HomeCategoryModel> homeCategoryModel = setHomeCategoryModelExpenses(eachCategoryAmount);//dynamic data

        System.out.println("inside AppHomeActivity class, inside setupListView () 3, Expenses model setted up :" + homeCategoryModel);
        // Create an instance of HomeCategoryAdapter
        HomeCategoryAdapter adapter = new HomeCategoryAdapter(this, homeCategoryModel);
        System.out.println("inside AppHomeActivity class, inside setupListView () 4, HomeCategoryAdapter adapter setted up");

        // Set the adapter for your ListView
        homeCategoryList.setAdapter(adapter);
        System.out.println("inside AppHomeActivity class, inside setupListView () 5, expenseList setted up: ===ended==" + homeCategoryList);
    }

    private List<HomeCategoryModel> setHomeCategoryModelExpenses(HashMap<String, Integer> eachCategoryAmount) {
        System.out.println("inside AppHomeActivity class, inside setExpenses () 1, ==Started==");
        List<HomeCategoryModel> homeCategoryModelExpensesList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : eachCategoryAmount.entrySet()) {
            Integer i = entry.getValue();
            int sum = i;
            System.out.println("inside AppHomeActivity class, inside setExpenses () int :" + sum);
            System.out.println("inside AppHomeActivity class, inside setExpenses () int totalMonthExpense :" + totalMonthExpense);

            // Ensure totalMonthExpense is not zero to avoid division by zero
            if (totalMonthExpense != 0) {
                float myFloat = (float) sum / totalMonthExpense;
                String myString = String.format("%.2f%%", myFloat * 100);  // Properly format the percentage to 2 decimal places
                System.out.printf("per %%: %s%n", myString);

                homeCategoryModelExpensesList.add(new HomeCategoryModel(R.drawable.some_icon, entry.getKey(), "$" + entry.getValue(), myString));
            } else {
                System.out.println("Total month expense is zero, cannot calculate percentages.");
            }
        }

        return homeCategoryModelExpensesList;
    }

    private void calculateTotalMonthExpense() {
        System.out.println("inside AppHomeActivity class, inside calculateTotalMonthExpense () 1 of 5, ==Started==");
        //totalMonthExpense = 0;
        for (HashMap<String, String> rowExpenseMap : readExpenseDataRowMapListFromExcel) {
            System.out.println("inside AppHomeActivity class, inside calculateTotalMonthExpense () 2 of 5 , " + rowExpenseMap);
            try {
                String amountStr = rowExpenseMap.get(AMOUNT);
                System.out.println("inside AppHomeActivity class, inside calculateTotalMonthExpense () 3 of 5 , " + amountStr);
                if (amountStr != null) {
                    totalMonthExpense += Integer.parseInt(amountStr);
                    System.out.println("inside AppHomeActivity class, inside calculateTotalMonthExpense () 4 of 5, " + totalMonthExpense);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        System.out.println("inside AppHomeActivity class, inside calculateTotalMonthExpense () 5 of 5, ==ended==" + totalMonthExpense);
    }

    private void calculateEachCategoryAmount() {
        System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 1 of 7, ==Started==");
        eachCategoryAmount.clear();
        for (String category : readAllCategoryListFromSheet) {
            System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 2 of 7, " + category);
            for (HashMap<String, String> rowExpenseMap : readExpenseDataRowMapListFromExcel) {
                System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 3 of 7, " + rowExpenseMap);
                try {
                    String amountStr = rowExpenseMap.get(AMOUNT);
                    System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 4 of 7, " + amountStr);
                    if (rowExpenseMap.get(CATEGORY).equals(category) && amountStr != null) {

                        if (eachCategoryAmount.isEmpty() || !eachCategoryAmount.containsKey(category)) {
                            eachCategoryAmount.put(category, Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 5 of 7, if " + eachCategoryAmount);
                        } else {
                            eachCategoryAmount.put(category, eachCategoryAmount.get(category) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 6 of 7, else " + eachCategoryAmount);
                        }

                    }
                } catch (NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("inside AppHomeActivity class, inside calculateEachCategoryAmount () 7 of 7, ==ended==");
    }

    private void calculateEachPaymentTypeAmount() {
        System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 1 of 7, ==Started==");
        eachPaymentTypeAmount.clear();
        for (String subPayment : readAllSubPaymentListFromSheet) {
            System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 2 of 7" + subPayment);
            for (HashMap<String, String> rowExpenseMap : readExpenseDataRowMapListFromExcel) {
                System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 3 of 7" + rowExpenseMap);
                try {
                    String amountStr = rowExpenseMap.get(AMOUNT);
                    System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 4 of 7" + amountStr);
                    if (rowExpenseMap.get(PAYMENT_SUBTYPE).equals(subPayment) && amountStr != null) {
                        if (eachPaymentTypeAmount.isEmpty() || !eachPaymentTypeAmount.containsKey(subPayment)) {
                            eachPaymentTypeAmount.put(subPayment, Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 5 of 7 if" + eachPaymentTypeAmount);
                        } else {
                            eachPaymentTypeAmount.put(subPayment, eachPaymentTypeAmount.get(subPayment) + Integer.parseInt(rowExpenseMap.get(AMOUNT)));
                            System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 6 of 7 else" + eachPaymentTypeAmount);
                        }
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("inside AppHomeActivity class, inside calculateEachPaymentTypeAmount () 7 of 7 , ==ended==");
    }

    private void setupPieChart() {
        System.out.println("inside AppHomeActivity class, inside setupPieChart () 1, ==Started==");
        if (pieChart == null) return;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false); // false
        pieChart.setExtraOffsets(5, 15, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);//true
        pieChart.setHoleColor(Color.LTGRAY);
        pieChart.setTransparentCircleRadius(61f);

        // Assuming pieChart is inside a RelativeLayout
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieChart.getLayoutParams();
        params.width = (int) (370 * getResources().getDisplayMetrics().density); // Set desired width
        params.height = (int) (370 * getResources().getDisplayMetrics().density); // Set desired height
        pieChart.setLayoutParams(params);

        System.out.println("inside AppHomeActivity class, inside setupPieChart () 2, pieChart setted up ====ended===");
    }

    private void loadPieChartData() {
        System.out.println("inside AppHomeActivity class, inside loadPieChartData () 1, ==Started==");
        if (pieChart == null) return;
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : eachCategoryAmount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        System.out.println("inside AppHomeActivity class, inside loadPieChartData () 2, entries :" + entries);
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setSliceSpace(3f); //The setSliceSpace method sets the space between each slice of the pie chart to 3 pixels.
        dataSet.setSelectionShift(10f); //The setSelectionShift method sets the offset distance for the selected slice to 5 pixels.
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK); // Set the value text color to black here
        System.out.println("inside AppHomeActivity class, inside loadPieChartData () 3, dataSet :" + dataSet);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        System.out.println("inside AppHomeActivity class, inside loadPieChartData () 4, entries :" + data);

        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart to apply changes
        System.out.println("inside AppHomeActivity class, inside loadPieChartData () 5, ==ended==");
    }
}
