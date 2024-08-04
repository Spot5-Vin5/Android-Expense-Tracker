package com.example.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.R;
import com.example.expensetracker.models.TransactionModel;

import java.util.List;

public class TransactionExpenseAdapter extends BaseAdapter {

    private Context context;
    private List<TransactionModel> expensetransactionsList;
    private LayoutInflater inflater;

    public TransactionExpenseAdapter(Context context, List<TransactionModel> expenseList) {
        this.context = context;
        this.expensetransactionsList = expenseList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return expensetransactionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return expensetransactionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_item_expense_transactions, parent, false);
        }

        TextView date = convertView.findViewById(R.id.tvDate); // Corrected ID
        TextView amount = convertView.findViewById(R.id.tvAmount); // Corrected ID
        TextView category = convertView.findViewById(R.id.tvCategory); // Corrected ID
        TextView subCategory = convertView.findViewById(R.id.tvSubCategory); // Corrected ID
        TextView paymentType = convertView.findViewById(R.id.tvPaymentType); // Corrected ID
        TextView paymentSubType = convertView.findViewById(R.id.tvSubPaymentType); // Corrected ID
        TextView note = convertView.findViewById(R.id.tvNote); // Corrected ID
        ImageButton moreOptions = convertView.findViewById(R.id.btnMoreOptions); // Corrected ID

        TransactionModel transaction = expensetransactionsList.get(position);
        date.setText(transaction.getDate());
        amount.setText(transaction.getAmount());
        category.setText(transaction.getCategoryType());
        if (transaction.getCategorySubType() != null) {
            subCategory.setText(transaction.getCategorySubType());
            subCategory.setVisibility(View.VISIBLE);
        } else {
            subCategory.setVisibility(View.GONE);
        }
        paymentType.setText(transaction.getPaymentType());
        if (transaction.getPaymentSubType() != null) {
            paymentSubType.setText(transaction.getPaymentSubType());
            paymentSubType.setVisibility(View.VISIBLE);
        } else {
            paymentSubType.setVisibility(View.GONE);
        }
        if (transaction.getNote() != null) {
            note.setText(transaction.getNote());
            note.setVisibility(View.VISIBLE);
        } else {
            note.setVisibility(View.GONE);
        }

        // Implement popup for moreOptions button if needed
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(moreOptions);
            }
        });

        return convertView;
    }

    private void showPopupMenu(ImageButton btnMoreOptions) {
        System.out.println("inside TransactionExpenseAdapter class, Image button is clicked -> inside showPopupMenu () ");
        // Create a PopupMenu
        PopupMenu popup = new PopupMenu(context, btnMoreOptions);
        // Inflate the menu from XML
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        // Set a click listener for menu item clicks
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.editExpense) {
                    Toast.makeText(context, "Edit Expense selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.deleteExpense) {
                    Toast.makeText(context, "Delete Expense selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        // Show the popup menu
        popup.show();
        System.out.println("inside TransactionExpenseAdapter class, Image button is clicked -> inside showPopupMenu () --ended--");
    }
}
