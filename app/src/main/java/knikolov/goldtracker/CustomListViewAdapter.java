package knikolov.goldtracker;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] expenseName;
    private String[] expenseDate;
    private Float[] expenseSum;


    public CustomListViewAdapter(Activity context, String[] expenseName, String[] expenseDate, Float[] expenseSum) {
        super(context, R.layout.custom_list_view, expenseName);
        this.context = context;
        this.expenseName = expenseName;
        this.expenseDate = expenseDate;
        this.expenseSum = expenseSum;
    }

    public String[] getExpenseName() {
        return expenseName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View vi= inflater.inflate(R.layout.custom_list_view, null, true);

        TextView reason = vi.findViewById(R.id.reasonOfExpense); // title
        TextView date = vi.findViewById(R.id.dateOfExpense); // artist name
        TextView sum = vi.findViewById(R.id.sumOfExpense); // duration


        // Setting all values in listview
        reason.setText(expenseName[position]);
        date.setText(expenseDate[position]);
        sum.setText(String.format("%.2f", expenseSum[position]) + " €");
        return vi;
    }
}