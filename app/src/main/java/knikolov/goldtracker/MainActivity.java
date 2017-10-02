package knikolov.goldtracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //declaration of variables
    ListView mainListView;
    CustomListViewAdapter adapter;
    float totalSum=0;
    private String currency="";

    // list with the data for the listview
    public static List<EntryTemplate> entries;

    //get a reasons/expense name string[] from the list
    public String[] getReasons(List<EntryTemplate> source){
        String[] arr = new String[source.size()];
        for(int i=0; i<source.size(); i++){
            arr[i] = source.get(i).getName();
        }
        return arr;
    }
    //get the date when deleting, ID isn't reliable
    public String getDeleteDate(List<EntryTemplate> source, int i){
        return source.get(i).getDate();
    }

    //get the dates of entries in a string[]
    public String[] getDates(List<EntryTemplate> source){
        String[] arr = new String[source.size()];
        for(int i=0; i<source.size(); i++){
            arr[i] = source.get(i).getDate();
        }
        return arr;
    }

    //get the sums of the entries in a string[]
    public Float[] getSums(List<EntryTemplate> source){
        Float[] arr = new Float[source.size()];
        for(int i=0; i<source.size(); i++){
            arr[i] = source.get(i).getSum();
        }
        return arr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entries = new ArrayList<>();

        //check and get the set currency
        SharedPreferences p = getSharedPreferences("GoldTrackerPrefs", MODE_PRIVATE);
        currency = p.getString("Currency", currency);

        //get current date for the sum of current month
        Calendar calendar = Calendar.getInstance();
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisYear = calendar.get(Calendar.YEAR);
        thisMonth++;

        //set up database
        final MyDataBase mydb = new MyDataBase(this);
        final SQLiteDatabase sqdb = mydb.getWritableDatabase();
        String query = "SELECT " + mydb.EXPENSE + ", " + mydb.SUM + ", " + mydb.DATE + " FROM " + mydb.TABLE_NAME;
        final Cursor cursor = sqdb.rawQuery(query, null);

        //add all entries from the database to the entries list
        while(cursor.moveToNext()){
            entries.add(new EntryTemplate(cursor.getString(cursor.getColumnIndex(mydb.EXPENSE)),cursor.getString(cursor.getColumnIndex(mydb.DATE)), cursor.getFloat(cursor.getColumnIndex(mydb.SUM)))) ;

        }
        cursor.close();


        //if an entry's date is from this month, add it to "This Month" total
        String queryTotal = "SELECT " + mydb.SUM + " , strftime('%m'," + mydb.DATE +" ) as Month, "+ "strftime('%Y'," + mydb.DATE +" ) as Year"+" from " + mydb.TABLE_NAME;
        final Cursor cursorTotal = sqdb.rawQuery(queryTotal, null);
        while (cursorTotal.moveToNext()){
            //check month in db and current
            if(thisMonth == cursorTotal.getFloat(cursorTotal.getColumnIndex("Month")) && thisYear ==  cursorTotal.getFloat(cursorTotal.getColumnIndex("Year"))){
                totalSum+=cursorTotal.getFloat(cursorTotal.getColumnIndex(mydb.SUM));
            }
        }
        cursorTotal.close();

        Collections.reverse(entries);

        //add make an adapter from the entries and add them to the listview
        adapter = new
                CustomListViewAdapter(MainActivity.this, getReasons(entries), getDates(entries), getSums(entries), currency);
            mainListView=(ListView)findViewById(R.id.expenses_list);
            mainListView.setAdapter(adapter);
        //on item click - delete option
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int selectedItem, long l) {
                Dialog d = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Do you want to DELETE this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final MyDataBase mydb = new MyDataBase(MainActivity.this);
                                final SQLiteDatabase sqdb = mydb.getWritableDatabase();
                                String entryToDelete = getDeleteDate(entries, selectedItem);
                                String query = "DELETE FROM " + mydb.TABLE_NAME + " WHERE "+ mydb.DATE+ " = '" + entryToDelete + "';";
                                sqdb.execSQL(query);
                                sqdb.close();
                                mydb.close();
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                d.show();
            }
        });
        // this month textview set
        TextView totalSumView = (TextView)findViewById(R.id.totalSum);
        totalSumView.setText(String.format("%.2f", totalSum) + " " + currency);

        sqdb.close();
        mydb.close();

    }

    public void monthSummary(View view){
        //load database and calculate sum of each month
        MyDataBase mydb = new MyDataBase(this);
        SQLiteDatabase sqdb = mydb.getWritableDatabase();
        String query = "select SUM( "+ mydb.SUM  + " ) as totalSum, "+"strftime('%Y'," + mydb.DATE +" ) as Year" +" , strftime('%m'," + mydb.DATE +" ) as Month from " +  mydb.TABLE_NAME + " GROUP BY strftime('%Y-%m', "+ mydb.DATE +");";
        Cursor cursor = sqdb.rawQuery(query, null);
        ArrayList<String> summary = new ArrayList<>();

        while(cursor.moveToNext()){
            //TODO: string array with months, remove switch
            //<year> <month> <sum>
            summary.add(cursor.getString(cursor.getColumnIndex("Year"))+ " " +convertToNameOfMonth(cursor.getString(cursor.getColumnIndex("Month"))) + cursor.getString(cursor.getColumnIndex("totalSum")) + " " + currency);
        }
        mydb.close();
        sqdb.close();
        cursor.close();
        //setup dialog for month summary
        String[] summaryArray = new String[summary.size()];
        summaryArray = summary.toArray(summaryArray);
        Dialog d = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Month Summary")
                .setNegativeButton("Cancel", null)
                .setItems(summaryArray, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dlg, int position)
                    {

                    }
                })
                .create();
        d.show();
    }

    public String convertToNameOfMonth(String numOfMonth){
        String result = "";
        switch (numOfMonth){
            case "01": result = "January: ";
            break;
            case "02": result = "February: ";
            break;
            case "03": result = "March: ";
            break;
            case "04": result = "April: ";
            break;
            case "05": result = "May: ";
            break;
            case "06": result = "June: ";
            break;
            case "07": result = "July: ";
            break;
            case "08": result = "August: ";
            break;
            case "09": result = "September: ";
            break;
            case "10": result = "October: ";
            break;
            case "11": result = "November: ";
            break;
            case "12": result = "December: ";
            break;
        }
        return result;
    }

    //load activity for new entry
    public void addNew(View view) {
        Intent activityChangeIntent = new Intent(MainActivity.this, ExpenseDetails.class);
        startActivity(activityChangeIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater currencyMenu = getMenuInflater();
        currencyMenu.inflate(R.menu.menu_main, menu);
        return true;
    }

    //set currency option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        final String[] currencies = new String[]{"€", "£", "$", "lv.", "None" };
        Dialog d = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Select Currency")
                .setNegativeButton("Cancel", null)
                .setItems(currencies, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dlg, int position)
                    {
                        if(position == 4 ){
                            currency = "";
                        }
                        else currency = currencies[position];
                        SharedPreferences p = getSharedPreferences("GoldTrackerPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = p.edit();
                        prefEditor.putString("Currency", currency);
                        prefEditor.apply();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .create();
        d.show();
        return false;
    }
}

