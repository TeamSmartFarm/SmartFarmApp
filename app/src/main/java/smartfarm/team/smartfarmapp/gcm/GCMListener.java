package smartfarm.team.smartfarmapp.gcm;

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

                //JSONObject jsonObject = new JSONObject(data.getString("msg"));
                handleReceivedRequest(data);
            } catch (Exception e) {
            }
        }
    }

    private void handleReceivedRequest(Bundle message) {
        try {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String start_light = message.getString(getString(R.string.gcm_start_light));
            String end_light = message.getString(getString(R.string.gcm_end_light));
            String start_temp = message.getString(getString(R.string.gcm_start_temp));
            String end_temp = message.getString(getString(R.string.gcm_end_temp));
            String water = message.getString("waterConsumption");
            //String moisture = message.getString("moisture");


            // Intent to go to app
            Intent notificationIntent = new Intent(this, NotificationActivity.class);;
            notificationIntent.putExtra(getString(R.string.gcm_start_light),start_light);
            notificationIntent.putExtra(getString(R.string.gcm_end_light),end_light);
            //notificationIntent.putExtra(getString(R.string.gcm_moisture),moisture);
            notificationIntent.putExtra(getString(R.string.gcm_start_temp),start_temp);
            notificationIntent.putExtra(getString(R.string.gcm_end_temp),end_temp);
            notificationIntent.putExtra(getString(R.string.gcm_water),water);
            Log.e("Request_received", "true");
            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create Notification Builder
            String messageTitle = "Irrigation Done";
            String messageBody = "Temperature: "+start_temp+"\tLight: " + start_light +
                    "\nWater: "+water;

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
                    .setContentIntent(contentIntent)
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Notification error", e.toString());
        }
    }
}
