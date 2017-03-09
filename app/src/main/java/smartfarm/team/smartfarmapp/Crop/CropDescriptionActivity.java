package smartfarm.team.smartfarmapp.Crop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

public class CropDescriptionActivity extends AppCompatActivity {
    Crop crop;
    private TextView water,tempreature,moisture,name;
    private ImageView cropImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_description);
        Intent intent = getIntent();
        crop= (Crop) intent.getSerializableExtra("Crop");

        water= (TextView) findViewById(R.id.water);
        tempreature= (TextView) findViewById(R.id.temperature);
        moisture= (TextView) findViewById(R.id.moisture);
       name= (TextView) findViewById(R.id.cropDesName);
        cropImage= (ImageView) findViewById(R.id.cropimage);

        //name.setText(crop.cropName);
        /*Picasso.with(getApplicationContext())
                .load(crop.getCropImageUrl())
                .into(cropImage);*/
        name.setText(crop.cropName);
        water.setText(crop.waterConsumption);
        tempreature.setText(crop.temperature);
        moisture.setText(crop.moisture);


    }
}
