package smartfarm.team.smartfarmapp.farm;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import smartfarm.team.smartfarmapp.R;

public class WeightSliderActivity extends AppCompatActivity {

    ListView nodeList;
    Button submit;
    SharedPreferences details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_slider);

        attachToWiFi();
        nodeList = (ListView) findViewById(R.id.weight_list);
        details = getSharedPreferences(getString(R.string.shared_main_name), MODE_PRIVATE);
        int numMotes = Integer.parseInt(details.getString(getString(R.string.shared_farm_no_motes), "16"));

        WeightAdapter adapter = new WeightAdapter(WeightSliderActivity.this,numMotes);
        nodeList.setAdapter(adapter);

        submit = (Button) findViewById(R.id.submit_mote);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoteWeight();
            }
        });
    }

    private void attachToWiFi() {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", "SmartFarm1332908");
        wifiConfig.preSharedKey = String.format("\"%s\"", "A3B4NJshb*903dfnHx");
        //wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        // WifiSetup.setIpAssignment("STATIC", wifiConfig); //or "DHCP" for dynamic setting
        // WifiSetup.setIpAddress(InetAddress.getByName("192.168.0.102"), 24, wifiConfig);

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();


    }

    StringBuilder stringToSend;
    WifiManager wifiManager;
    private void sendMoteWeight() {
        Log.e("WEIGHT","........");

        try {
            int numMotes = Integer.parseInt(details.getString(getString(R.string.shared_farm_no_motes), "10"));
            stringToSend = new StringBuilder("-25:"+numMotes+":");
            for(int i=0;i<numMotes;i++)
                stringToSend.append(details.getFloat("Mote"+(i+1),1.0f)+":");
            stringToSend.deleteCharAt(stringToSend.length()-1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        Log.e("IP Adress", Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));

                        DatagramPacket packet = new DatagramPacket(stringToSend.toString().getBytes(),
                                stringToSend.length(), InetAddress.getByName("192.168.0.1"), 9999);

                        DatagramSocket socket = new DatagramSocket(5050);
                        socket.send(packet);
                        socket.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error....3",e.getMessage());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setup_farm_menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        onBackPressed();
        return true;
    }

}

class WeightAdapter extends ArrayAdapter<String>{

    private Context context;
    private int numMotes;
    private SharedPreferences details;

    private class ViewHolder {
        TextView moteName;
        SeekBar moteWeight;
    }


    public WeightAdapter(Context context, int numMotes) {
        super(context, numMotes);
        this.context = context;
        this.numMotes = numMotes;
        details = context.getSharedPreferences(context.getString(R.string.shared_main_name),Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.weight_list_card, parent, false);
            holder = new ViewHolder();
            holder.moteName = (TextView) convertView.findViewById(R.id.mote_name);
            holder.moteWeight = (SeekBar) convertView.findViewById(R.id.mote_weight_seekbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.moteName.setText("Mote "+ (position+1));
        holder.moteWeight.setTag(position);

        float moteWeight = details.getFloat("Mote"+(position+1),1.0f);

        holder.moteWeight.setProgress((int) (moteWeight*100));

        holder.moteWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor =details.edit();
                editor.putFloat("Mote"+((int) seekBar.getTag()+1),progress/100.0f);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return numMotes;
    }
}
