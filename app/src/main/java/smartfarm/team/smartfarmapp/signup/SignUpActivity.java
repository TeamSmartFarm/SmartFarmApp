package smartfarm.team.smartfarmapp.signup;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

public class SignUpActivity extends AppCompatActivity {

    TextView farm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        farm_id = (TextView) findViewById(R.id.farm_id);

        SharedPreferences details = getSharedPreferences(getString(R.string.shared_pref_name),MODE_PRIVATE);

        farm_id.setText(details.getString(getString(R.string.shared_farm_id),"Farm ID"));


    }
}
