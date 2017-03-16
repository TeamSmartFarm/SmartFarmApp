package smartfarm.team.smartfarmapp.Crop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartfarm.team.smartfarmapp.R;

/**
 * Created by AK on 02-Jan-17.
 */
public class CropRecycleViewAdapter extends RecyclerView.Adapter<CropRecycleViewAdapter.MyViewHolder>{

    private List<Crop> cropList;
    private OnCropClickListener listener;

    public interface OnCropClickListener {
        void onCropClick(Crop crop);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cropTitle;
        public ImageView cropImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            cropImage= (ImageView) itemView.findViewById(R.id.cropimage);
            cropTitle= (TextView) itemView.findViewById(R.id.cropname);

        }
        public void bind(final Crop crop, final OnCropClickListener listener){
            cropTitle.setText(crop.getCropName());
            Picasso.with(itemView.getContext())
                    //.load(crop.getCropImageUrl())
                    .load(R.drawable.crop)
                    .into(cropImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCropClick(crop);
                }
            });
        }
    }

    public CropRecycleViewAdapter(List<Crop> cropList,OnCropClickListener listener) {
        this.listener=listener;
        this.cropList=cropList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crop_list_row_gj, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(cropList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }
}
