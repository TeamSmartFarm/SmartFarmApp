package smartfarm.team.smartfarmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;

import smartfarm.team.smartfarmapp.log.LogActivity;
import smartfarm.team.smartfarmapp.signup.SignUpActivity;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);

                   details = getSharedPreferences(getString(R.string.shared_main_name)
                            ,MODE_PRIVATE);

                    if(details.getBoolean(getString(R.string.shared_first_time),true)){
                        barcodeScan();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LogActivity.class);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String id = data.getStringExtra("SCAN_RESULT");
            Log.d("Farm Id",id);

            SharedPreferences.Editor editor = details.edit();
            editor.putString(getString(R.string.shared_farm_id),id);
            editor.putBoolean(getString(R.string.shared_first_time),false);
            editor.apply();

            Intent detail = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(detail);
        }
    }

    void barcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.initiateScan();
    }

}
