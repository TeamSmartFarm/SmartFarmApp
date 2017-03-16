package smartfarm.team.smartfarmapp.farm;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import smartfarm.team.smartfarmapp.Crop.Crop;
import smartfarm.team.smartfarmapp.Crop.CropDescriptionActivity;
import smartfarm.team.smartfarmapp.Crop.CropRecycleViewAdapter;
import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.gcm.NotificationActivity;

public class CurrentCrop extends AppCompatActivity {

    TextView sownDate, waterConsumed,cropProgressTextView;
    ProgressBar cropProgress;
    Button done;
    LinearLayout notificationLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_crop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences currentCropShared = getSharedPreferences(getString(R.string.shared_current_crop),
                MODE_PRIVATE);

        if(!(currentCropShared.getBoolean(getString(R.string.shared_current_crop_sown_bool),false))){
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrentCrop.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.sown_new_crop, null);
            builder.setView(dialogView);
            builder.setTitle("Select Crop");

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            RecyclerView suggestion = (RecyclerView) dialogView.findViewById(R.id.new_suggestion_list);
            suggestion.setLayoutManager(layoutManager);

            List<Crop> cropList =new ArrayList<>();
            String imagepath = "http://192.168.0.116/ak/crop.png";
            Crop crop = new Crop("Wheat", imagepath, "10", "12", "14");
            cropList.add(crop);

            crop = new Crop("Rice", imagepath, "10", "12", "14");
            cropList.add(crop);

            crop = new Crop("Chilly", imagepath, "10", "12", "14");
            cropList.add(crop);

            crop = new Crop("Peas", imagepath, "10", "12", "14");
            cropList.add(crop);

            crop = new Crop("Mustard", imagepath, "10", "12", "14");
            cropList.add(crop);
            CropRecycleViewAdapter recycleViewAdapter = new CropRecycleViewAdapter(cropList, new CropRecycleViewAdapter.OnCropClickListener() {
                @Override
                public void onCropClick(Crop crop) {
                    //Toast.makeText(getApplicationContext(),crop.getCropName()+" clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CurrentCrop.this, CropDescriptionActivity.class);
                    intent.putExtra("Crop", crop);
                    startActivity(intent);
                }
            });
            suggestion.setAdapter(recycleViewAdapter);

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Save Crop
                }
            });
            builder.show();
        }

        sownDate = (TextView) findViewById(R.id.current_sown_date);
        waterConsumed = (TextView) findViewById(R.id.current_last_not_water);
        cropProgressTextView = (TextView) findViewById(R.id.current_sown_progress_textview);
        notificationLink = (LinearLayout) findViewById(R.id.current_last_not);
        cropProgress = (ProgressBar) findViewById(R.id.current_sown_progress);
        done = (Button) findViewById(R.id.current_done);



        sownDate.setText(currentCropShared.getString(getString(R.string.shared_current_sown_date),
                "13/03/2017"));

        int noOfDays = Math.abs(calculateDateDiff(currentCropShared.getString(getString(R.string.shared_current_sown_date),
                "13/03/2017")));

        int progress = (int) Math.ceil(noOfDays*100.0/
        currentCropShared.getInt(getString(R.string.shared_current_total_days),10));

        ObjectAnimator animator = ObjectAnimator.ofInt(cropProgress,"progress",0,progress);
        animator.start();
        cropProgressTextView.setText(progress + "% Grown");

        SharedPreferences notification = getSharedPreferences(getString(R.string.shared_previous_not),
                MODE_PRIVATE);
        waterConsumed.setText(notification.getString(getString(R.string.gcm_water),"45450"));

        notificationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent not = new Intent(CurrentCrop.this, NotificationActivity.class);
                startActivity(not);
            }
        });


    }

    private int calculateDateDiff(String startDateString) {
        Calendar startDate = new GregorianCalendar();
        Calendar todayDate = Calendar.getInstance();

        startDateString = startDateString.replace("/","");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

        Date date = null;
        try {
            date = sdf.parse(startDateString);
            startDate.setTime(date);

            return (int) ((todayDate.getTime().getTime()-startDate.getTime().getTime())/86400000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
