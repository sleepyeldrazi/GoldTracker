package knikolov.goldtracker;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView mainListView;

    //set up popup
    private PopupWindow pw;
    private View popupView;
    private LayoutInflater inflater;
    public static int count;
    CustomListViewAdapter adapter;
    float totalSum=0;

    public static List<EntryTemplate> entries;

    public String[] getReasons(List<EntryTemplate> source){
        String[] arr = new String[source.size()];
        for(int i=0; i<source.size(); i++){
            arr[i] = source.get(i).getName();
        }
        return arr;
    }

    public String[] getDates(List<EntryTemplate> source){
        String[] arr = new String[source.size()];
        for(int i=0; i<source.size(); i++){
            arr[i] = source.get(i).getDate();
        }
        return arr;
    }
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


        //set up database
        MyDataBase mydb = new MyDataBase(this);
        SQLiteDatabase sqdb = mydb.getWritableDatabase();
        String query = "SELECT " + mydb.EXPENSE + ", " + mydb.SUM + ", " + mydb.DATE + " FROM " + mydb.TABLE_NAME;
        Cursor cursor = sqdb.rawQuery(query, null);
        count = cursor.getCount();
        while(cursor.moveToNext()){
            entries.add(new EntryTemplate(cursor.getString(cursor.getColumnIndex(mydb.EXPENSE)),cursor.getString(cursor.getColumnIndex(mydb.DATE)), cursor.getFloat(cursor.getColumnIndex(mydb.SUM)))) ;
            totalSum+=cursor.getFloat(cursor.getColumnIndex(mydb.SUM));
        }
        cursor.close();

        adapter = new
                CustomListViewAdapter(MainActivity.this, getReasons(entries), getDates(entries), getSums(entries));
            mainListView=(ListView)findViewById(R.id.expenses_list);
            mainListView.setAdapter(adapter);
        TextView totalSumView = (TextView)findViewById(R.id.totalSum);
        totalSumView.setText(String.format("%.2f", totalSum) + " €");

        sqdb.close();
        mydb.close();

    }

    public void refresh(View view){
        adapter.notifyDataSetChanged();
    }

    public void addNew(View view) {
        Intent activityChangeIntent = new Intent(MainActivity.this, ExpenseDetails.class);
        startActivity(activityChangeIntent);
    }
}

