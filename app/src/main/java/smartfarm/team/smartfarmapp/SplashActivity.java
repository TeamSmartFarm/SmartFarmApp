package smartfarm.team.smartfarmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import smartfarm.team.smartfarmapp.HomePage.MainActivity;
import smartfarm.team.smartfarmapp.signup.SignUpActivity;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, 5959);

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);

                    details = getSharedPreferences(getString(R.string.shared_main_name)
                            , MODE_PRIVATE);

                    Intent intent;
                    if (details.getBoolean(getString(R.string.shared_first_time), true)) {
                        intent = new Intent(SplashActivity.this, SignUpActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (!(grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) ){

            Toast.makeText(SplashActivity.this,"Permission Needed to Work",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
