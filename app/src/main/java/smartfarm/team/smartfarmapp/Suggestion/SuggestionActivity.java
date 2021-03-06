package smartfarm.team.smartfarmapp.Suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartfarm.team.smartfarmapp.Crop.Crop;
import smartfarm.team.smartfarmapp.Crop.CropDescriptionActivity;
import smartfarm.team.smartfarmapp.Crop.CropRecycleViewAdapter;
import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.util.ServerRequest;

public class SuggestionActivity extends AppCompatActivity {

    private List<Crop> cropList;
    private RecyclerView recyclerView;
    private CropRecycleViewAdapter recycleViewAdapter;

    // json array response url
    private String urlJsonArry = "http://ec2-35-154-68-218.ap-south-1.compute.amazonaws.com:8000/sf/cropdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        cropList = new ArrayList<>();
        recycleViewAdapter = new CropRecycleViewAdapter(cropList, new CropRecycleViewAdapter.OnCropClickListener() {
            @Override
            public void onCropClick(Crop crop) {
                //Toast.makeText(getApplicationContext(),crop.getCropName()+" clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SuggestionActivity.this, CropDescriptionActivity.class);
                intent.putExtra("Crop", crop);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleViewAdapter);
        prepareddata();
    }

    private void prepareddata() {
      /*  JsonArrayRequest req = new JSONObjectRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {*/
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonArry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json array response
                    // loop through each json object
                    JSONArray obj = response.getJSONArray("Answer");
                    String imagepath = "http://ec2-35-154-68-218.ap-south-1.compute.amazonaws.com:8000/sf/cropimage/";
                    Crop crop;
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject jsonObject = (JSONObject) obj
                                .get(i);
                        crop = new Crop(jsonObject.getString("_id"),
                                imagepath + jsonObject.getString("_id") + ".jpg",
                                jsonObject.getString("water_usage"),
                                jsonObject.getString("light"),
                                jsonObject.getString("temp"));
                        cropList.add(crop);
                    }
                    recycleViewAdapter.notifyDataSetChanged();

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(jsonObjReq);
    }
}
