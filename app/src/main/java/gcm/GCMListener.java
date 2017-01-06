package gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import log.LogActivity;
import smartfarm.team.smartfarmapp.R;

public class GCMListener extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    public static final int NOTIFICATION_ID = 1;
    int NOTIFICATION_NUM=0;


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e("GCM", data.toString());
        // Time to unparcel the bundle!

        if (!data.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(data.getString("msg"));
                handleReceivedRequest(jsonObject);
            } catch (JSONException e) {
            }
        }
    }

    private void handleReceivedRequest(JSONObject message) {
        try {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String light = message.getString("light");
            String temp = message.getString("temprature");
            String water = message.getString("waterConsumption");
            String moisture = message.getString("moisture");


            // Intent to go to app
            Intent notificationIntent = new Intent(this, LogActivity.class);;
            notificationIntent.putExtra(getString(R.string.gcm_light),light);
            notificationIntent.putExtra(getString(R.string.gcm_moisture),moisture);
            notificationIntent.putExtra(getString(R.string.gcm_temp),temp);
            notificationIntent.putExtra(getString(R.string.gcm_water),water);
            Log.e("Request_received", "true");
            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create Notification Builder
            String messageTitle = "Irrigation Done";
            String messageBody = "Temperature: "+temp+"\tLight: " + light +
                    "\nMoisture: "+moisture + "\tWater: "+water;

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(messageTitle)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setContentText(messageBody)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setNumber(++NOTIFICATION_NUM);

            // Publish Notification
            SharedPreferences userDetail =
                    getSharedPreferences(getString(R.string.user_shared_preef), Context.MODE_PRIVATE);
            Boolean sound = userDetail.getBoolean(getString(R.string.not_sound), true);
            Boolean vibrate = userDetail.getBoolean(getString(R.string.not_vibrate), true);

            if(sound && vibrate)
                mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            else if(sound)
                mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS );
            else if(vibrate)
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            else
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Notification error", e.toString());
        }
    }
}
