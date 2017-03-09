package smartfarm.team.smartfarmapp.Suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import smartfarm.team.smartfarmapp.Crop.Crop;
import smartfarm.team.smartfarmapp.Crop.CropDescriptionActivity;
import smartfarm.team.smartfarmapp.Crop.CropRecycleViewAdapter;
import smartfarm.team.smartfarmapp.R;

public class SuggestionActivity extends AppCompatActivity {

    private List<Crop> cropList;
    private RecyclerView recyclerView;
    private CropRecycleViewAdapter recycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        cropList= new ArrayList<>();
        recycleViewAdapter = new CropRecycleViewAdapter(cropList, new CropRecycleViewAdapter.OnCropClickListener() {
            @Override
            public void onCropClick(Crop crop) {
                //Toast.makeText(getApplicationContext(),crop.getCropName()+" clicked", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(SuggestionActivity.this,CropDescriptionActivity.class);
                intent.putExtra("Crop",crop);
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
        //crop list for suggestions.


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


        recycleViewAdapter.notifyDataSetChanged();
    }
}
