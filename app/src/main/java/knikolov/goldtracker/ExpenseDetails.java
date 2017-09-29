package knikolov.goldtracker;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;



public class ExpenseDetails extends AppCompatActivity {

    float sum;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

    }

    public void newEntry(View view){

        EditText expenseName = (EditText) findViewById(R.id.expense_name);
        EditText expenseSum = (EditText) findViewById(R.id.sum);
        sum = Float.valueOf(expenseSum.getText().toString());
        name = expenseName.getText().toString();
        MyDataBase mydb = new MyDataBase(this);
        SQLiteDatabase sqdb = mydb.getWritableDatabase();
        String query = "INSERT INTO "+ mydb.TABLE_NAME + " (" + mydb.EXPENSE + ", " + mydb.SUM + ", " + mydb.DATE + ") VALUES (\"" + name + "\", " + sum + ",datetime());" ;
        sqdb.execSQL(query);
        sqdb.close();
        mydb.close();

        Intent activityChangeIntent = new Intent(ExpenseDetails.this, MainActivity.class);
        startActivity(activityChangeIntent);
    }
}
