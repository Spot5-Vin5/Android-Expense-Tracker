package com.example.expensetracker.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        return convertView;
    }
}
