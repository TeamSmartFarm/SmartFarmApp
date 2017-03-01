package smartfarm.team.smartfarmapp.log;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

/**
 * Created by Gaurav on 06-01-2017.
 */

public class LogListViewAdapter extends ArrayAdapter<String> {

    String[] data;
    Activity context;

    public LogListViewAdapter(Activity context, String[] data) {
        super(context, R.layout.listview_log, data.length);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.listview_log, null, true);
        TextView event,status;
        event = (TextView) row.findViewById(R.id.rowEvent);
        status = (TextView) row.findViewById(R.id.rowStatus);
    //    try {
            String messageBody = "Temperature: "+data[2]+"\tLight: " + data[0] +
                    "\nMoisture: "+data[3] + "\tWater: "+data[1];
            event.setText(messageBody);

//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return row;
    }
}
