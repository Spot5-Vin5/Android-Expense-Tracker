package com.example.expensetracker.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.expensetracker.R;
import com.example.expensetracker.models.TransactionModel;

import java.util.List;

public class TransactionExpenseAdapter extends BaseAdapter {

    private Context context;
    private List<TransactionModel> expenseList;
    private LayoutInflater inflater;

    public TransactionExpenseAdapter(Context context, List<TransactionModel> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int position) {
        return expenseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_item_transactions, parent, false);
        }

        TextView expenseName = convertView.findViewById(R.id.expenseName);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        TransactionModel expense = expenseList.get(position);
        expenseName.setText(expense.getCategoryType());

        // Set listeners for edit and delete buttons if needed
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
            }
        });

        return convertView;
    }
}
