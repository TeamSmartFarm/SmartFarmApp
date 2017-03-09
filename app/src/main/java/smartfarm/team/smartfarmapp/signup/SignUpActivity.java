package smartfarm.team.smartfarmapp.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.Map;

import smartfarm.team.smartfarmapp.HomePage.MainActivity;
import smartfarm.team.smartfarmapp.R;
import smartfarm.team.smartfarmapp.gcm.RegistrationIntentService;
import smartfarm.team.smartfarmapp.util.Constant;
import smartfarm.team.smartfarmapp.util.ServerRequest;


public class SignUpActivity extends AppCompatActivity {

    TextView farm_id,name,aadhar,contact,location;
    Button go;
    ProgressDialog load,barcode_load;
    SharedPreferences details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        farm_id = (TextView) findViewById(R.id.farm_id);
        name = (TextView) findViewById(R.id.name);
        aadhar = (TextView) findViewById(R.id.aadhar);
        contact = (TextView) findViewById(R.id.contact);
        location = (TextView) findViewById(R.id.location);
        go = (Button) findViewById(R.id.go);

        barcode_load = new ProgressDialog(SignUpActivity.this);
        barcode_load.setTitle("Setting It Up");
        barcode_load.setCancelable(false);
        barcode_load.setMessage("Initializing Your App");
        barcode_load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barcode_load.show();

        barcodeScan();

        details = getSharedPreferences(getString(R.string.shared_main_name),MODE_PRIVATE);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get GPS Location
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_account();
            }
        });

    }

    void barcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String id = data.getStringExtra("SCAN_RESULT");
            Log.d("Farm Id", id);

            SharedPreferences.Editor editor = details.edit();
            editor.putString(getString(R.string.shared_farm_id), id);
            editor.putBoolean(getString(R.string.shared_first_time), false);
            editor.apply();

            farm_id.setText(details.getString(getString(R.string.shared_farm_id),"005"));

            barcode_load.cancel();

        }
    }

    private void make_account() {

        String url= Constant.url +"/sf/signup";

        StringRequest newac = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                            load.dismiss();

                            //Register IID
                            Intent intent = new Intent(SignUpActivity.this, RegistrationIntentService.class);
                            Bundle arg = new Bundle();
                            arg.putString("id",details.getString(getString(R.string.shared_farm_id),"001"));
                            intent.putExtras(arg);
                            startService(intent);

                            //going to profile page
                            Intent mnsr = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(mnsr);
                            finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        load.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Some Error Occurred....Try Latter", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("FarmID",details.getString(getString(R.string.shared_farm_id),"001"));
                param.put("Name", name.getText().toString());
                param.put("Aadhar", aadhar.getText().toString());
                param.put("Contact", contact.getText().toString());
                param.put("Location",location.getText().toString());

                return param;
            }
        };

        load=new ProgressDialog(SignUpActivity.this);
        load.setTitle("Creating Account");
        load.setCancelable(false);
        load.setMessage("Just a Moment");
        load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        load.show();

        ServerRequest.getInstance(getApplicationContext()).getRequestQueue().add(newac);

    }
}
