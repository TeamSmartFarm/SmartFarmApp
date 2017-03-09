package smartfarm.team.smartfarmapp.gcm;

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

        if(getIntent().hasExtra(getString(R.string.gcm_start_light))) {
            start_temp.setText(getIntent().getStringExtra(getString(R.string.gcm_start_temp)));
            end_temp.setText(getIntent().getStringExtra(getString(R.string.gcm_end_temp)));
            start_light.setText(getIntent().getStringExtra(getString(R.string.gcm_start_light)));
            end_light.setText(getIntent().getStringExtra(getString(R.string.gcm_end_light)));
            water.setText(getIntent().getStringExtra(getString(R.string.gcm_water)));
        }

    }

}

