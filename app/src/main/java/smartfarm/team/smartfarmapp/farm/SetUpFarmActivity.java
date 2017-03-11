package smartfarm.team.smartfarmapp.farm;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import smartfarm.team.smartfarmapp.R;

public class SetUpFarmActivity extends AppCompatActivity {

    ImageView cross1, cross2, cross3, cross4;
    RelativeLayout farm;
    SharedPreferences coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_farm);

        coordinates = getSharedPreferences(getString(R.string.shared_farm_coordinates), MODE_PRIVATE);
        farm = (RelativeLayout) findViewById(R.id.farm);

        setup();
    }

    private void setup() {
        if (coordinates.getBoolean(getString(R.string.set_up_done), false)) {
            FarmView farmView = new FarmView(SetUpFarmActivity.this);
            farmView.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            farmView.setLayoutParams(params);
            farm.removeAllViews();
            farm.addView(farmView);
//            Toast.makeText(SetUpFarmActivity.this,"s",Toast.LENGTH_SHORT).show();
        } else {
            farm = (RelativeLayout) findViewById(R.id.farm);

            cross1 = (ImageView) findViewById(R.id.cross1);
            cross2 = (ImageView) findViewById(R.id.cross2);
            cross3 = (ImageView) findViewById(R.id.cross3);
            cross4 = (ImageView) findViewById(R.id.cross4);

            cross1.setOnTouchListener(move());
            cross2.setOnTouchListener(move());
            cross3.setOnTouchListener(move());
            cross4.setOnTouchListener(move());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (coordinates.getBoolean(getString(R.string.set_up_done), false))
            getMenuInflater().inflate(R.menu.setup_farm_menu_edit, menu);
        else
            getMenuInflater().inflate(R.menu.setup_farm_menu_done, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (!(coordinates.getBoolean(getString(R.string.set_up_done), false))) {
            SharedPreferences.Editor editor = coordinates.edit();

            editor.putFloat(getString(R.string.cross1_x_coord), cross1.getX());
            editor.putFloat(getString(R.string.cross1_y_coord), cross1.getY());
            editor.putFloat(getString(R.string.cross2_x_coord), cross2.getX());
            editor.putFloat(getString(R.string.cross2_y_coord), cross2.getY());
            editor.putFloat(getString(R.string.cross3_x_coord), cross3.getX());
            editor.putFloat(getString(R.string.cross3_y_coord), cross3.getY());
            editor.putFloat(getString(R.string.cross4_x_coord), cross4.getX());
            editor.putFloat(getString(R.string.cross4_y_coord), cross4.getY());
            editor.putBoolean(getString(R.string.set_up_done), true);
            editor.apply();
            invalidateOptionsMenu();
            setup();
            Toast.makeText(SetUpFarmActivity.this, "Done", Toast.LENGTH_SHORT).show();


        }
        else{
            //farm.removeViewAt(0);
            setContentView(R.layout.activity_set_up_farm);
            SharedPreferences.Editor editor = coordinates.edit();
            editor.putBoolean(getString(R.string.set_up_done), false);
            editor.apply();
            invalidateOptionsMenu();
            setup();

        }
        return true;
    }

    private View.OnTouchListener move() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int xDelta = 0, yDelta = 0;

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
//                        Toast.makeText(SetUpFarmActivity.this,
//                                "thanks for new location!", Toast.LENGTH_SHORT)
//                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        break;
                }
                farm.invalidate();
                return true;
            }
        };
    }
}
