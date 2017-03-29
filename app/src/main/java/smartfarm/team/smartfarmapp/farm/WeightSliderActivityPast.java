package smartfarm.team.smartfarmapp.farm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

public class WeightSliderActivityPast extends AppCompatActivity {

    Spinner nodeId;
    TextView nill, less, medium, moderate, excess, full;
    ImageView slider;
    SharedPreferences details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_slider);

        nodeId = (Spinner) findViewById(R.id.node_id);
        nill = (TextView) findViewById(R.id.nill);
        less = (TextView) findViewById(R.id.less);
        medium = (TextView) findViewById(R.id.medium);
        moderate = (TextView) findViewById(R.id.moderate);
        excess = (TextView) findViewById(R.id.excess);
        full = (TextView) findViewById(R.id.full);

        details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        int numMotes = details.getInt("No of Motes", 16);

        String[] motes = new String[numMotes];
        for (int i = 1; i <= numMotes; i++) {
            motes[i - 1] = "Mote " + i;
        }


        nodeId.setAdapter(new ArrayAdapter<String>(WeightSliderActivityPast.this,
                android.R.layout.simple_dropdown_item_1line, motes));

        nodeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                float moteSavedWeight = details.getFloat("Mote"+(position+1),1.0f);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setup_farm_menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int selectedMote = nodeId.getSelectedItemPosition();
        SharedPreferences.Editor editor = details.edit();
        editor.putFloat("Mote"+selectedMote, (Float) slider.getTag());
        editor.apply();

        return true;
    }

}
