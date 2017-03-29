package smartfarm.team.smartfarmapp.farm;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
import smartfarm.team.smartfarmapp.util.Constant;
import smartfarm.team.smartfarmapp.util.ServerRequest;

public class CurrentCrop extends AppCompatActivity {

    TextView sownDate, waterConsumed,cropProgressTextView,totalMotes;
    ProgressBar cropProgress;
    Spinner cropListSpinner;
    Button done;
    String selectedCrop;
    LinearLayout notificationLink,moteWeightLink;
    SharedPreferences currentCropShared,notificationSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_crop);

        currentCropShared = getSharedPreferences(getString(R.string.shared_pref_name_current_crop),
                MODE_PRIVATE);

        notificationSharedPreferences = getSharedPreferences(getString(R.string.shared_previous_not),
                MODE_PRIVATE);

        if(!(currentCropShared.getBoolean(getString(R.string.shared_current_crop_sown_bool),false))){
            takeNewCrop();
        }

        setUpToolBar();
        initWidget();
        initValues();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCrop();
            }
        });
        moteWeightLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weight = new Intent(CurrentCrop.this,WeightSliderActivity.class);
                startActivity(weight);
            }
        });

        notificationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent not = new Intent(CurrentCrop.this, NotificationActivity.class);
                startActivity(not);
            }
        });


    }

    private void submitCrop() {

        selectedCrop = cropListSpinner.getSelectedItem().toString();
        SharedPreferences details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        String farm_id=details.getString(getString(R.string.shared_farm_id),"001");

        String url ="http://ec2-35-154-68-218.ap-south-1.compute.amazonaws.com:8000/sf/currentcrop?farmID="
                + farm_id +"&crop=" +selectedCrop;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.d(TAG, response.toString());
                try {
                    DateFormat datefm=new SimpleDateFormat("dd/MM/yyyy");
                    Date date=new Date();
                    String dateString=datefm.format(date);
                    Log.e("Date",dateString);
                    SharedPreferences.Editor editor= currentCropShared.edit();
                    editor.putString(getString(R.string.shared_current_crop_sown_bool),"true");
                    editor.putString(getString(R.string.shared_current_total_days),response.getString("days"));
                    editor.putString(getString(R.string.shared_current_crop),selectedCrop);
                    editor.putString(getString(R.string.shared_current_sown_date),dateString);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(jsonObjReq);




    }

    private void initValues() {
        sownDate.setText(currentCropShared.getString(getString(R.string.shared_current_sown_date),
                "13/03/2017"));

        int noOfDays = Math.abs(calculateDateDiff(currentCropShared.getString(getString(R.string.shared_current_sown_date),
                "13/03/2017")));

        int progress = (int) Math.ceil(noOfDays*100.0/
                currentCropShared.getInt(getString(R.string.shared_current_total_days),10));

        ObjectAnimator animator = ObjectAnimator.ofInt(cropProgress,"progress",0,progress);
        animator.start();
        cropProgressTextView.setText(progress + "% Grown");

        waterConsumed.setText(notificationSharedPreferences.getString(getString(R.string.gcm_water),"45450"));

        totalMotes.setText(notificationSharedPreferences.getString(getString(R.string.shared_farm_no_motes),"15")+" Motes Installed");
    }

    private void initWidget() {
        sownDate = (TextView) findViewById(R.id.current_sown_date);
        waterConsumed = (TextView) findViewById(R.id.current_last_not_water);
        cropProgressTextView = (TextView) findViewById(R.id.current_sown_progress_textview);
        totalMotes = (TextView) findViewById(R.id.current_total_motes);
        notificationLink = (LinearLayout) findViewById(R.id.current_last_not);
        moteWeightLink = (LinearLayout) findViewById(R.id.current_mote_weight);
        cropProgress = (ProgressBar) findViewById(R.id.current_sown_progress);
        done = (Button) findViewById(R.id.current_done);
    }

    private void setUpToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        final ImageView toolbarImage = (ImageView) findViewById(R.id.toolbar_image);

        String cropTitle = currentCropShared.getString(getString(R.string.shared_current_crop),"Current Crop");
        collapsingToolbarLayout.setTitle(cropTitle);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                toolbarImage.setImageBitmap(bitmap);

                //Bitmap imgBitmap = ((BitmapDrawable)currentCropImage.getDrawable()).getBitmap();
                Palette p = Palette.from(bitmap).generate();
                toolbar.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        toolbarImage.setTag(target);
        //Set Image View Accordingly
        Picasso.with(CurrentCrop.this)
                .load(Constant.url+"sf/cropimage/"+cropTitle+".jpg")
                .into(toolbarImage);

        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        //collapsingToolbarLayout.setExpandedTitleTextColor(R.color.colorPrimary);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void takeNewCrop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentCrop.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sown_new_crop, null);
        builder.setView(dialogView);
        builder.setTitle("Select Crop");

        cropListSpinner = (Spinner) dialogView.findViewById(R.id.croplist);
        getCropList(cropListSpinner);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView suggestion = (RecyclerView) dialogView.findViewById(R.id.new_suggestion_list);
        suggestion.setLayoutManager(layoutManager);
        final List<Crop> cropList =new ArrayList<>();
        String urlJsonArry = "http://ec2-35-154-68-218.ap-south-1.compute.amazonaws.com:8000/sf/cropdata";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonArry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Log.d(TAG, response.toString());

                try {
                    JSONArray obj = response.getJSONArray("Answer");
                    String imagepath = "http://ec2-35-154-68-218.ap-south-1.compute.amazonaws.com:8000/sf/cropimage/";
                    Crop crop;
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject jsonObject = (JSONObject) obj
                                .get(i);
                        crop = new Crop(jsonObject.getString("_id"),
                                imagepath+jsonObject.getString("_id")+".jpg",
                                jsonObject.getString("water_usage"),
                                jsonObject.getString("light"),
                                jsonObject.getString("temp"));
                        cropList.add(crop);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(jsonObjReq);

        getSuggestions(cropList);

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

    private void getSuggestions(List<Crop> cropList) {
        String url = Constant.url + "/sf/cropdetail";
        //String url = Constant.url + "/sf/suggestion";
        //ServerRequest.getInstance(CurrentCrop.this).addRequestQueue(arrayRequest);
    }

    private void getCropList(final Spinner cropListSpinner) {
        String url = Constant.url + "/sf/croplist";
        final ArrayList<String> cropListArrayList = new ArrayList<>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray obj = null;
                        try {
                            obj = response.getJSONArray("Answer");

                            for(int i=0;i<obj.length();i++) {
                                cropListArrayList.add(obj.getString(i));
                            }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        CurrentCrop.this, android.R.layout.simple_spinner_item,
                                        cropListArrayList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                cropListSpinner.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        ServerRequest.getInstance(CurrentCrop.this).addRequestQueue(jsonObjReq);
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
