package smartfarm.team.smartfarmapp.Crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        cropImage= (ImageView) findViewById(R.id.cropDesImage);

        name.setText(crop.cropName);
        water.setText(crop.waterConsumption);
        tempreature.setText(crop.temperature);
        moisture.setText(crop.moisture);

        //name.setText(crop.cropName);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                cropImage.setImageBitmap(bitmap);

                //Bitmap imgBitmap = ((BitmapDrawable)currentCropImage.getDrawable()).getBitmap();
                Palette p = Palette.from(bitmap).generate();
                name.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        cropImage.setTag(target);

        Picasso.with(CropDescriptionActivity.this)
                .load(crop.getCropImageUrl())
                .into(target);


    }


}
