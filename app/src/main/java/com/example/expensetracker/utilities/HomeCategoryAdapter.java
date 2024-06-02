package com.example.expensetracker.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.expensetracker.R;
import com.example.expensetracker.models.HomeCategoryModel;

import java.util.List;

public class HomeCategoryAdapter extends ArrayAdapter<HomeCategoryModel> {

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView amount;
        TextView percentage;
    }
    private final Context context;
    private final List<HomeCategoryModel> categoryExpenses;

    public HomeCategoryAdapter(Context context, List<HomeCategoryModel> categoryExpenses) {
        super(context, R.layout.activity_list_item_home_categories, categoryExpenses);
        this.context = context;
        this.categoryExpenses = categoryExpenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_list_item_home_category_model, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.home_category_model_icon);
            holder.name = convertView.findViewById(R.id.home_category_model_name);
            holder.amount = convertView.findViewById(R.id.home_category_model_amount);
            holder.percentage = convertView.findViewById(R.id.home_category_model_percentage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeCategoryModel expense = categoryExpenses.get(position);
        holder.icon.setImageResource(expense.getIconResId());
        holder.name.setText(expense.getName());
        holder.amount.setText(expense.getAmount());
        holder.percentage.setText(expense.getPercentage());

        return convertView;
    }
}
