package knikolov.goldtracker;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public CustomListViewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_list_view, null);

        TextView reason = (TextView)vi.findViewById(R.id.reasonOfExpense); // title
        TextView date = (TextView)vi.findViewById(R.id.dateOfExpense); // artist name
        TextView sum = (TextView)vi.findViewById(R.id.sumOfExpense); // duration

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        // Setting all values in listview
        reason.setText(song.get(MainActivity.KEY_REASON));
        date.setText(song.get(MainActivity.KEY_DATE));
        sum.setText(song.get(MainActivity.KEY_SUM));
        return vi;
    }
}