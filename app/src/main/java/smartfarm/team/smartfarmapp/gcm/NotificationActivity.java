package smartfarm.team.smartfarmapp.gcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

public class NotificationActivity extends AppCompatActivity {

    TextView start_temp,end_temp,start_light,end_light,water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        start_temp = (TextView) findViewById(R.id.start_temp);
        end_temp = (TextView) findViewById(R.id.end_temp);
        start_light = (TextView) findViewById(R.id.start_light);
        end_light = (TextView) findViewById(R.id.end_light);
        water = (TextView) findViewById(R.id.water);

        SharedPreferences notification_data = getSharedPreferences(
                getString(R.string.shared_previous_not), MODE_PRIVATE);


        if(getIntent().hasExtra(getString(R.string.gcm_start_light))) {
            start_temp.setText(getIntent().getStringExtra(getString(R.string.gcm_start_temp)));
            end_temp.setText(getIntent().getStringExtra(getString(R.string.gcm_end_temp)));
            start_light.setText(getIntent().getStringExtra(getString(R.string.gcm_start_light)));
            end_light.setText(getIntent().getStringExtra(getString(R.string.gcm_end_light)));
            water.setText(getIntent().getStringExtra(getString(R.string.gcm_water)));

            SharedPreferences.Editor editor = notification_data.edit();
            editor.putString(getString(R.string.gcm_start_temp),getIntent().getStringExtra(getString(R.string.gcm_start_temp)));
            editor.putString(getString(R.string.gcm_end_temp),getIntent().getStringExtra(getString(R.string.gcm_end_temp)));
            editor.putString(getString(R.string.gcm_start_light),getIntent().getStringExtra(getString(R.string.gcm_start_light)));
            editor.putString(getString(R.string.gcm_end_light),getIntent().getStringExtra(getString(R.string.gcm_end_light)));
            editor.putString(getString(R.string.gcm_water),getIntent().getStringExtra(getString(R.string.gcm_water)));
            editor.apply();
        }
        else{
            start_temp.setText(notification_data.getString(getString(R.string.gcm_start_temp),"23"));
            end_temp.setText(notification_data.getString(getString(R.string.gcm_end_temp),"25"));
            start_light.setText(notification_data.getString(getString(R.string.gcm_start_light),"350"));
            end_light.setText(notification_data.getString(getString(R.string.gcm_end_light),"450"));
            water.setText(notification_data.getString(getString(R.string.gcm_water),"4560"));
        }

    }

}

