package smartfarm.team.smartfarmapp.farm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.signup.SoilActivity;
import smartfarm.team.smartfarmapp.util.Constant;
import smartfarm.team.smartfarmapp.util.ServerRequest;

public class MyFarmActivity extends AppCompatActivity {

    SharedPreferences details, currentCrop;
    EditText name, aadhar, contact, city, numMotes,farmArea;
    TextView currentCropText, soilText;
    ImageView currentCropImage, soilImage;
    CardView motesWeight;
    Boolean editMode = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        currentCrop = getSharedPreferences(getString(R.string.shared_pref_name_current_crop), MODE_PRIVATE);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        collapsingToolbarLayout.setTitle("Farm ID: " + details.getString(getString(R.string.shared_farm_id), "001"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initWidget();
        motesWeight.setOnClickListener(weightIntent());
        initDetails();
        initCurrentCrop();
        initSoilType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soilImage.invalidate();
        soilText.invalidate();
    }

    private void initSoilType() {
        String soilName = details.getString(getString(R.string.shared_farm_soil_type), "Null");
        soilText.setText(soilName);
        int id = getSoilImageID(soilName);
        soilImage.setImageResource(id);

        Bitmap bitmap = ((BitmapDrawable) soilImage.getDrawable()).getBitmap();
        Palette p = Palette.from(bitmap).generate();
        soilText.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));
    }

    private void initCurrentCrop() {

        String curentCrop = currentCrop.getString(getString(R.string.shared_current_crop), "Null");

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                currentCropImage.setImageBitmap(bitmap);

                //Bitmap imgBitmap = ((BitmapDrawable)currentCropImage.getDrawable()).getBitmap();
                Palette p = Palette.from(bitmap).generate();
                currentCropText.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        currentCropImage.setTag(target);
        Picasso.with(MyFarmActivity.this)
                .load(Constant.url+"/sf/cropimage/"+curentCrop+".jpg")
                .into(target);
        currentCropText.setText(curentCrop);

        //.load(Constant.url+"/cropimg/"+curentCrop)

    }

    private void initDetails() {
        name.setText(details.getString(getString(R.string.shared_farm_name), "Name"));
        contact.setText(details.getString(getString(R.string.shared_farm_contact), "Contact"));
        city.setText(details.getString(getString(R.string.shared_farm_city), "City"));
        aadhar.setText(details.getString(getString(R.string.shared_farm_aadhar), "Aadhar"));
        numMotes.setText(details.getString(getString(R.string.shared_farm_no_motes), "Motes"));
        farmArea.setText(details.getString(getString(R.string.shared_farm_area), "Farm Area"));
    }

    private View.OnClickListener weightIntent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weight = new Intent(MyFarmActivity.this, WeightSliderActivity.class);
                startActivity(weight);
            }
        };
    }

    private void initWidget() {
        name = (EditText) findViewById(R.id.my_farm_name);
        contact = (EditText) findViewById(R.id.my_farm_contact);
        aadhar = (EditText) findViewById(R.id.my_farm_aadhar);
        city = (EditText) findViewById(R.id.my_farm_city);
        numMotes = (EditText) findViewById(R.id.my_farm_no_motes);
        farmArea = (EditText) findViewById(R.id.my_farm_area);

        currentCropText = (TextView) findViewById(R.id.my_farm_crop_text);
        soilText = (TextView) findViewById(R.id.my_farm_soil_text);

        currentCropImage = (ImageView) findViewById(R.id.my_farm_crop_image);
        soilImage = (ImageView) findViewById(R.id.my_farm_soil_image);

        motesWeight = (CardView) findViewById(R.id.my_farm_mote);
    }

    private int getSoilImageID(String type) {
        switch (type) {
            case "Alluvium Soil":
                return R.drawable.alluvium_soil;
            case "Black Soil":
                return R.drawable.black_soil;
            case "Red Soil":
                return R.drawable.red_soil;
            case "Latrite Soil":
                return R.drawable.laterite_soil;
            case "Mountain Soil":
                return R.drawable.mountain_soil;
            default:
                return R.drawable.desert_soil;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!editMode)
            getMenuInflater().inflate(R.menu.setup_farm_menu_edit, menu);
        else
            getMenuInflater().inflate(R.menu.setup_farm_menu_done, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (editMode) {
            SharedPreferences.Editor editor = details.edit();
            editor.putString(getString(R.string.shared_farm_contact), contact.getText().toString());
            editor.putString(getString(R.string.shared_farm_aadhar), aadhar.getText().toString());
            editor.putString(getString(R.string.shared_farm_city), city.getText().toString());
            editor.putString(getString(R.string.shared_farm_name), name.getText().toString());
            editor.putString(getString(R.string.shared_farm_no_motes), numMotes.getText().toString());
            editor.putString(getString(R.string.shared_farm_area), farmArea.getText().toString());

            editor.apply();
            editMode = false;

            saveDetails();
            // soilImage.setOnClickListener(null);
            invalidateOptionsMenu();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        } else {
            editMode = true;
            name.setFocusableInTouchMode(true);
            aadhar.setFocusableInTouchMode(true);
            city.setFocusableInTouchMode(true);
            contact.setFocusableInTouchMode(true);
            numMotes.setFocusableInTouchMode(true);
            farmArea.setFocusableInTouchMode(true);

            soilImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent soil = new Intent(MyFarmActivity.this, SoilActivity.class);
                    startActivity(soil);
                }
            });
            invalidateOptionsMenu();
            toolbar = (Toolbar) findViewById(R.id.toolbar);

        }

        return true;
    }

    private void saveDetails() {
        String url = Constant.url + "/sf/signup";

        StringRequest newac = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Some Error Occurred....Try Latter", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();

                param.put("FarmID", details.getString(getString(R.string.shared_farm_id), "001"));
                param.put("Name", name.getText().toString());
                param.put("Aadhar", aadhar.getText().toString());
                param.put("Contact", contact.getText().toString());
                param.put("Location", city.getText().toString());
                param.put("NumberMotes", numMotes.getText().toString());
                param.put("Area", farmArea.getText().toString());
                param.put("SoilType", details.getString(getString(R.string.shared_farm_soil_type),"Black Soil"));

                return param;
            }
        };

        ServerRequest.getInstance(getApplicationContext()).getRequestQueue().add(newac);

    }
}
