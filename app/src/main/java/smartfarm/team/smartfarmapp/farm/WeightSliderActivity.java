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

public class WeightSliderActivity extends AppCompatActivity {

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
        slider = (ImageView) findViewById(R.id.seekBar);

        details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        int numMotes = details.getInt("No of Motes", 16);

        String[] motes = new String[numMotes];
        for (int i = 1; i <= numMotes; i++) {
            motes[i - 1] = "Mote " + i;
        }


        nodeId.setAdapter(new ArrayAdapter<String>(WeightSliderActivity.this,
                android.R.layout.simple_dropdown_item_1line, motes));

        nodeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                float moteSavedWeight = details.getFloat("Mote"+(position+1),1.0f);
                switch (""+moteSavedWeight){
                    case "0":
                        slider.setImageResource(R.drawable.slider_nill);
                        break;
                    case "0.2":
                        slider.setImageResource(R.drawable.slider_less);
                        break;
                    case "0.4":
                        slider.setImageResource(R.drawable.slider_medium);
                        break;
                    case "0.6":
                        slider.setImageResource(R.drawable.slider_moderate);
                        break;
                    case "0.8":
                        slider.setImageResource(R.drawable.slider_excess);
                        break;
                    case "1":
                        slider.setImageResource(R.drawable.slider_full);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_nill);
                slider.setTag(0f);
            }
        });
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_less);
                slider.setTag(0.2f);
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_medium);
                slider.setTag(0.4f);
            }
        });
        moderate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_moderate);
                slider.setTag(0.6f);
            }
        });
        excess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_excess);
                slider.setTag(0.8f);
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setImageResource(R.drawable.slider_full);
                slider.setTag(1.0f);
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
