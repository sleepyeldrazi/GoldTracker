package knikolov.goldtracker;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<String> {

    //TODO: try to use List for expenseName, expenseDate and expenseSum
    private Activity context;
    private String[] expenseName;
    private String[] expenseDate;
    private Float[] expenseSum;
    private String currency;


    public CustomListViewAdapter(Activity context, String[] expenseName, String[] expenseDate, Float[] expenseSum, String currency) {
        super(context, R.layout.custom_list_view, expenseName);
        this.context = context;
        this.expenseName = expenseName;
        this.expenseDate = expenseDate;
        this.expenseSum = expenseSum;
        this.currency = currency;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View vi= inflater.inflate(R.layout.custom_list_view, null, true);

        TextView reason = vi.findViewById(R.id.reasonOfExpense); // expense name
        TextView date = vi.findViewById(R.id.dateOfExpense); // expense date
        TextView sum = vi.findViewById(R.id.sumOfExpense); // total sum


        // Setting all values in listview
        reason.setText(expenseName[position]);
        date.setText(expenseDate[position]);
        sum.setText(String.format("%.2f", expenseSum[position]) + " " + currency);
        return vi;
    }
}