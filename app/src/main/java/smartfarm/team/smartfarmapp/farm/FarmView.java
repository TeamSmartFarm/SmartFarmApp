package smartfarm.team.smartfarmapp.farm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import smartfarm.team.smartfarmapp.R;

public class FarmView extends View {
    Paint paint;
    Context context;
    SharedPreferences coordinates;

    public FarmView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
        paint.setStrokeWidth(5.0f);
        paint.setColor(Color.GREEN);
        coordinates = context.getSharedPreferences(context.getString(R.string.shared_farm_coordinates),Context.MODE_PRIVATE);

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawLine(coordinates.getFloat(context.getString(R.string.cross1_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross1_y_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross2_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross2_y_coord),0.0f),
                paint);
        canvas.drawLine(coordinates.getFloat(context.getString(R.string.cross1_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross1_y_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross3_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross3_y_coord),0.0f),
                paint);
        canvas.drawLine(coordinates.getFloat(context.getString(R.string.cross2_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross2_y_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross4_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross4_y_coord),0.0f),
                paint);
        canvas.drawLine(coordinates.getFloat(context.getString(R.string.cross3_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross3_y_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross4_x_coord),0.0f),
                coordinates.getFloat(context.getString(R.string.cross4_y_coord),0.0f),
                paint);

        //canvas.drawLine(20, 0, 0, 20, paint);
    }
}