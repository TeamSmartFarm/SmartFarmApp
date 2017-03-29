package smartfarm.team.smartfarmapp.farm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import smartfarm.team.smartfarmapp.R;

public class FarmTypeActivity extends AppCompatActivity {

    ListView cropShapeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_type);

        cropShapeList = (ListView) findViewById(R.id.farmshapelist);

        cropShapeList.setAdapter(new FarmShapeAdapter(FarmTypeActivity.this,4));


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
            convertView = inflater.inflate(R.layout.crop_list_row_gj, parent, false);
            holder = new ViewHolder();
            holder.shape = (ImageView) convertView.findViewById(R.id.cropimage);
            holder.shapeTitle = (TextView) convertView.findViewById(R.id.cropname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.shapeTitle.setText(getShapeTitle(position));
        holder.shape.setTag(position);


        return convertView;
    }



    private String getShapeTitle(int position) {
        switch (position){
            case 0:
                return "Circular";
            case 1:
                return "Rectangle";
            case 2:
                return "SemiCircle";
            case 3:
                return "Custom";
            default:
                return "Custom";
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
