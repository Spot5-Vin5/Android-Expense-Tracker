package com.example.expensetracker;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORIES;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.EXPENSE;
import static com.example.expensetracker.utilities.HeadingConstants.NAME;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_TYPE;
import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;
import static com.example.expensetracker.utilities.HeadingConstants.expenseColumnIndices;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensetracker.models.HomeCategoryModel;
import com.example.expensetracker.adapters.HomeCategoryAdapter;
import com.example.expensetracker.utilities.SingleTonExpenseTrackerExcelUtil;
import com.example.expensetracker.utilities.SingleTonSharedVariables;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppHomeActivity extends AppCompatActivity {
    private SingleTonExpenseTrackerExcelUtil singleTonExpenseTrackerExcelUtil;
    private Button appHomeMenuButton, addExpenseButton, profileButton, viewAllButton;
    //private TextView balanceInfoText;
    private PieChart pieChart;
    private HashMap<String, ArrayList<String>> categoryToSubcategoriesMap = new HashMap<>();
    private ArrayList<String> readAllCategoryListFromSheet; //= new ArrayList<>();
    //private ArrayList<String> singletonreadAllCategoryListFromSheet; //= new ArrayList<>();
    private HashMap<String, ArrayList<String>> paymentToSubPaymentMap = new HashMap<>();
    private HashMap<ArrayList<String>, HashMap<String, ArrayList<String>>> readTypesListandSubTypesMapFromExcelUtil;
    private ArrayList<String> readAllPaymentTypeListFromSheet; //= new ArrayList<>();
    private ArrayList<HashMap<String, String>> readExpenseDataRowMapListFromExcel;// = new ArrayList<>();
    private int totalMonthExpense = 0;
    private HashMap<String, Integer> eachCategoryAmount = new HashMap<>();
    private HashMap<String, Integer> eachPaymentTypeAmount = new HashMap<>();
    private TextView greetingTextView;
    private TextView homeBalance;

    private static WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        singleTonExpenseTrackerExcelUtil = SingleTonExpenseTrackerExcelUtil.getInstance(getApplicationContext());
        // Initialize variables in SharedVariables
        SingleTonSharedVariables sharedVariables = SingleTonSharedVariables.getInstance();

        System.out.println("inside AppHomeActivity class, 1 onCreate(), ===started===");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appHomeMain), AppHomeActivity::onApplyWindowInsets);
        System.out.println("inside AppHomeActivity class, inside 2 onCreate(), before initializeUI ()");
        // Initialize UI elements
        initializeUI();
        System.out.println("inside AppHomeActivity class,inside 3 onCreate(), before loadData()");
        // Update greeting based on the time
        updateGreeting(sharedVariables);
        // Load data
        loadData(sharedVariables);
        System.out.println("inside AppHomeActivity class,inside 4 onCreate(), before setupListView()");
        //System.out.println("inside AppHomeActivity class, inside 4 onCreate(), starting1:, "+eachPaymentTypeAmount);
        // Set up ListView
        setupListView(eachCategoryAmount);
        System.out.println("inside AppHomeActivity class,inside 5 onCreate(), before setupPieChart(), setupPieChart() ");
        // Set up Pie Chart
        setupPieChart();
        System.out.println("inside AppHomeActivity class,inside 6 onCreate(), before setupPieChart(), loadPieChartData() ");
        loadPieChartData();
        //Set up homeBalance
        loadHomeBalance();
        System.out.println("inside AppHomeActivity class, 7 onCreate(), ===ended===");
    }

    private void initializeUI() {
        System.out.println("inside AppHomeActivity class, inside initializeUI () 1 of 3");
        greetingTextView = findViewById(R.id.greeting);
        homeBalance = findViewById(R.id.homeBalance);
        profileButton = findViewById(R.id.profile);
        addExpenseButton = findViewById(R.id.addExpense);
        appHomeMenuButton = findViewById(R.id.appHomeMenu);
        pieChart = findViewById(R.id.pie_chart_container);
        viewAllButton = findViewById(R.id.view_all_button);

        System.out.println("inside AppHomeActivity class, inside initializeUI () 2 of 3");
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, ProfileActivity.class)));
        }
        if (addExpenseButton != null) {
            addExpenseButton.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, AddExpenseActivity.class)));
        }
        if (appHomeMenuButton != null) {
            appHomeMenuButton.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, HomeMenuActivity.class)));
        }
        if (viewAllButton != null) {
            viewAllButton.setOnClickListener(v -> startActivity(new Intent(AppHomeActivity.this, ViewAllButtonActivity.class)));
        }
        System.out.println("inside AppHomeActivity class, inside initializeUI () 3 of 3");
    }

    private void updateGreeting(SingleTonSharedVariables sharedVariables) {
        System.out.println("inside AppHomeActivity class, inside updateGreeting () started ");
        Calendar calendar = Calendar.getInstance();
        System.out.println("inside AppHomeActivity class, inside updateGreeting () calendar: "+calendar);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("inside AppHomeActivity class, inside updateGreeting () hour: "+hour);
        var scripts = singleTonExpenseTrackerExcelUtil.readProfileFromExcel(PROFILE_ACTIVITY,  sharedVariables);
        System.out.println("inside AppHomeActivity class, inside updateGreeting () scripts: "+scripts);
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning,"+scripts.get(NAME).toUpperCase(Locale.ROOT)+"!";
        } else if (hour >= 12 && hour < 16) {
            greeting = "Good Afternoon, "+scripts.get(NAME).toUpperCase(Locale.ROOT)+"!";
        } else if (hour >= 16 && hour < 20) {
            greeting = "Good Evening, "+scripts.get(NAME).toUpperCase(Locale.ROOT)+"!";
        } else {
            greeting = "Good Night, "+scripts.get(NAME).toUpperCase(Locale.ROOT)+"!";
        }
        System.out.println("inside AppHomeActivity class, inside updateGreeting () greeting: "+greeting);
        // Set the greeting in the TextView
        greetingTextView.setText(greeting);
        System.out.println("inside AppHomeActivity class, inside updateGreeting () ended");
    }


    private void loadData(SingleTonSharedVariables sharedVariables) {
        System.out.println("inside AppHomeActivity class, inside loadData ()");

        System.out.println("inside AppHomeActivity class, inside loadData () 1 of 8, ==Started==");
        readAllCategoryListFromSheet = singleTonExpenseTrackerExcelUtil.readTypesFromExcelUtil(CATEGORIES, new ArrayList<String>(), categoryToSubcategoriesMap, sharedVariables.getFilePath());
        System.out.println("inside AppHomeActivity class, inside loadData () 2 of 8, readAllCategoryListFromSheet :" + readAllCategoryListFromSheet);

        readTypesListandSubTypesMapFromExcelUtil = singleTonExpenseTrackerExcelUtil.readTypesListandSubTypesMapFromExcelUtil(PAYMENT_TYPE, sharedVariables.getFilePath());
        System.out.println("inside AppHomeActivity class, inside loadData () 3 of 8, readTypesListandSubTypesMapFromExcelUtil" + readTypesListandSubTypesMapFromExcelUtil);

        readAllPaymentTypeListFromSheet = readTypesListandSubTypesMapFromExcelUtil.keySet().iterator().next();
        System.out.println("inside AppHomeActivity class, inside loadData () 3 of 8, readAllPaymentTypeListFromSheet" + readAllPaymentTypeListFromSheet);

        paymentToSubPaymentMap = readTypesListandSubTypesMapFromExcelUtil.get(readAllPaymentTypeListFromSheet);
        System.out.println("inside AppHomeActivity class, inside loadData () 3 of 8, paymentToSubPaymentMap" + paymentToSubPaymentMap);

        readExpenseDataRowMapListFromExcel = singleTonExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>(), sharedVariables.getFilePath());
        System.out.println("inside AppHomeActivity class, inside loadData () 5 of 8, readExpenseDataRowMapListFromExcel :" + readExpenseDataRowMapListFromExcel);

        System.out.println("inside AppHomeActivity class, inside loadData () 6 of 8, before calculateTotalMonthExpense ()");
        calculateTotalMonthExpense();

        System.out.println("inside AppHomeActivity class, inside loadData () 7 of 8, before calculateEachCategoryAmount()");
        calculateEachCategoryAmount();

        System.out.println("inside AppHomeActivity class, inside loadData () 8 of 8, ==ended==");
    }

    private void setupListView(HashMap<String, Integer> eachCategoryAmount) {
        System.out.println("inside AppHomeActivity class, inside setupListView () 1, ==Started==");
        ListView homeCategoryList = findViewById(R.id.home_category_model_list);

        System.out.println("inside AppHomeActivity class, inside setupListView () 2" + homeCategoryList);

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

    private void loadHomeBalance() {
        System.out.println("inside AppHomeActivity class, inside loadhomeBalance () 1, ==Started==");
        homeBalance.setText(" Current Month Balance: ₹" + totalMonthExpense);
        System.out.println("inside AppHomeActivity class, inside loadhomeBalance () totalMonthExpense: " + totalMonthExpense);
        System.out.println("inside AppHomeActivity class, inside loadhomeBalance () 2, ==ended==");
    }

}
