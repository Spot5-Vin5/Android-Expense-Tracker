package com.example.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensetracker.R;
import com.example.expensetracker.models.PaymentsModel;

import java.util.List;



public class PaymentTypeExpenseAdapter extends BaseAdapter {
    private Context context;
    private List<PaymentsModel> expensePaymentsList;
    private LayoutInflater inflater;

    public PaymentTypeExpenseAdapter(Context context, List<PaymentsModel> paymenttypeExpenseList) {
        this.context = context;
        this.expensePaymentsList = paymenttypeExpenseList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return expensePaymentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return expensePaymentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_items_expense_payments, parent, false);
        }

        TextView expensePayment = convertView.findViewById(R.id.tvPaymentType); // Corrected ID
        TextView paymentAmount = convertView.findViewById(R.id.tvAmount); // Corrected ID

        PaymentsModel paymenttypeExpense = expensePaymentsList.get(position);
        expensePayment.setText(paymenttypeExpense.getPayment());
        paymentAmount.setText(paymenttypeExpense.getAmount());

        return convertView;
    }
}
