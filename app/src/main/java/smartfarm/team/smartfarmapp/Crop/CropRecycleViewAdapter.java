package smartfarm.team.smartfarmapp.Crop;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
            cropImage= (ImageView) itemView.findViewById(R.id.background_image);
            cropTitle= (TextView) itemView.findViewById(R.id.image_text);

        }
        public void bind(final Crop crop, final OnCropClickListener listener){
            cropTitle.setText(crop.getCropName());
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    cropImage.setImageBitmap(bitmap);

                    //Bitmap imgBitmap = ((BitmapDrawable)currentCropImage.getDrawable()).getBitmap();
                    Palette p = Palette.from(bitmap).generate();
                    cropTitle.setBackgroundColor(p.getDominantColor(Color.TRANSPARENT));

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            cropImage.setTag(target);
            Picasso.with(itemView.getContext())
                    .load(crop.getCropImageUrl())
                    .into(target);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCropClick(crop);
                }
            });
            //.load(Constant.url+"/cropimg/"+curentCrop)
        }

            /*Picasso.with()
                    .load(crop.getCropImageUrl())
                    //.load()
                    .into(cropImage);*/

    }


    public CropRecycleViewAdapter(List<Crop> cropList,OnCropClickListener listener) {
        this.listener=listener;
        this.cropList=cropList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);
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
