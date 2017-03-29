package smartfarm.team.smartfarmapp.signup;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import smartfarm.team.smartfarmapp.R;

import static android.content.Context.MODE_PRIVATE;


public class SoilActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ListView cropShapeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil);

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.ctl);
        collapsingToolbarLayout.setTitle("Soil Type");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cropShapeList = (ListView) findViewById(R.id.soiltypelist);
        int[] colors = {0, 0, 0};
        cropShapeList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        cropShapeList.setDividerHeight(15);

        cropShapeList.setAdapter(new FarmShapeAdapter(SoilActivity.this,6));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cropShapeList.setNestedScrollingEnabled(true);
        }

    }
}

class FarmShapeAdapter extends ArrayAdapter<String>{

    private Context context;

    class ViewHolder {
        ImageView shape;
        TextView shapeTitle;
    }


    public FarmShapeAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (convertView == null) {

            /*
            convertView = inflater.inflate(R.layout.crop_list_row_soil_type, parent, false);
            holder = new ViewHolder();
            holder.shape = (ImageView) convertView.findViewById(R.id.cropimage);
            holder.shapeTitle = (TextView) convertView.findViewById(R.id.cropname);
            convertView.setTag(holder);

            */
            convertView = inflater.inflate(R.layout.test, parent, false);
            holder = new ViewHolder();
            holder.shape = (ImageView) convertView.findViewById(R.id.background_image);
            holder.shapeTitle = (TextView) convertView.findViewById(R.id.image_text);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.shape.setImageResource(getShapeImageID(position));
        String title = getShapeTitle(position);
        holder.shapeTitle.setText(title);
        holder.shape.setTag(title);
        holder.shapeTitle.setTag(title);

        //Option 2
        // holder.shapeTitle.setTextColor(Color.WHITE);


        Bitmap bitmap = ((BitmapDrawable)holder.shape.getDrawable()).getBitmap();
        Palette p = Palette.from(bitmap).generate();
        holder.shapeTitle.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));


        holder.shape.setOnClickListener(saveCropName());


        return convertView;
    }

    private View.OnClickListener saveCropName() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences details = context.getSharedPreferences(
                        context.getString(R.string.shared_main_name),MODE_PRIVATE);
                SharedPreferences.Editor editor = details.edit();
                editor.putString(context.getString(R.string.shared_farm_soil_type), view.getTag().toString());
                editor.apply();

                ((Activity)context).onBackPressed();
            }
        };
    }

    private int getShapeImageID(int position) {
        switch (position){
            case 0:
                return R.drawable.alluvium_soil;
            case 1:
                return R.drawable.black_soil;
            case 2:
                return R.drawable.red_soil;
            case 3:
                return R.drawable.laterite_soil;
            case 4:
                return R.drawable.mountain_soil;
            default:
                return R.drawable.desert_soil;
        }
    }

    private String getShapeTitle(int position) {
        switch (position){
            case 0:
                return "Alluvium Soil";
            case 1:
                return "Black Soil";
            case 2:
                return "Red Soil";
            case 3:
                return "Laterite Soil";
            case 4:
                return "Mountain Soil";
            default:
                return "Desert Soil";
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}