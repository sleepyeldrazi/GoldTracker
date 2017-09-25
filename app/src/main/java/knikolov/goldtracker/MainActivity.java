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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // XML node keys
    static final String KEY_ID = "id";
    static final String KEY_REASON = "reason";
    static final String KEY_DATE = "date";
    static final String KEY_SUM = "sum";

    ListView list;
    CustomListViewAdapter adapter;

    //set up popup
    private PopupWindow pw;
    private View popupView;
    private LayoutInflater inflater;
    public static int count;

    public static List<EntryTemplate> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText expName = (EditText) findViewById(R.id.expense_name);
        EditText expSum = (EditText) findViewById(R.id.sum);

        //set up database
        MyDataBase mydb = new MyDataBase(this);
        SQLiteDatabase sqdb = mydb.getWritableDatabase();
        String query = "SELECT " + mydb.EXPENSE + ", " + mydb.SUM + ", " + mydb.DATE + " FROM " + mydb.TABLE_NAME;
        Cursor cursor = sqdb.rawQuery(query, null);
        count = cursor.getCount();
        while(cursor.moveToNext()){
            entries.add(new EntryTemplate(cursor.getString(cursor.getColumnIndex(mydb.EXPENSE)),cursor.getString(cursor.getColumnIndex(mydb.DATE)), cursor.getFloat(cursor.getColumnIndex(mydb.SUM)))) ;
        }
        cursor.close();



        sqdb.close();
        mydb.close();

    }

    public void addNew(View view) {
        Intent activityChangeIntent = new Intent(MainActivity.this, ExpenseDetails.class);
        startActivity(activityChangeIntent);
    }
}

