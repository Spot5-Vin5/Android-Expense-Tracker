package com.example.expensetracker;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExpenseTransactionImageButtonActivity extends AppCompatActivity {

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside ExpenseTransactionImageButtonActivity class, inside onCreate () :--start--");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_expense_transactions);

        ImageButton btnMoreOptions = findViewById(R.id.btnMoreOptions);
        btnMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        System.out.println("inside ExpenseTransactionImageButtonActivity class, Image button is clicked -> inside showPopupMenu () ");
        // Create a PopupMenu
        PopupMenu popup = new PopupMenu(this, view);
        // Inflate the menu from XML
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        // Set a click listener for menu item clicks
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.editExpense) {
                    Toast.makeText(ExpenseTransactionImageButtonActivity.this, "Edit Expense selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.deleteExpense) {
                    Toast.makeText(ExpenseTransactionImageButtonActivity.this, "Delete Expense selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.option3) {
                    Toast.makeText(ExpenseTransactionImageButtonActivity.this, "Option 3 selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.option4) {
                    Toast.makeText(ExpenseTransactionImageButtonActivity.this, "Option 4 selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        // Show the popup menu
        popup.show();
        System.out.println("inside ExpenseTransactionImageButtonActivity class, Image button is clicked -> inside showPopupMenu () --ended--");
    }*/
}
