package com.socialinfotech.feeedj;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.LauncherActivity;
import com.socialinfotech.feeedj.ApplicationActivities.MainActivity;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;


public class FireBaseService extends FirebaseMessagingService {

    public int NOTIFICATION_ID = 1;
    Context ctx;
    boolean searchNotification;

    String searchTerm="";

    String channelId = "feeedzid_01";
    CharSequence channelName = "Feeedz Channel";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        System.out.println(s);
        Log.e("newToken", "" + s);

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(Constant.FIREBASE_TOKEN, s).apply();
        editor.putBoolean(Constant.PENDING_NOTIFICATION_REGISTRATION, true).apply();

//        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("messageReceived", "" + remoteMessage.getData());

        ctx = this;
        String CompanyId="";
        String title="";
        String body="";

        if (remoteMessage.getData() != null) {
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                Log.d("JSONOBJECT_DATA", object.toString());
                CompanyId = object.get("Company").toString();
                title = object.get("title").toString();
                body = object.get("body").toString();
                notify(title,body,CompanyId);
                //rest of the code
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("messageData", "" + remoteMessage.getData().get("message"));

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("FireBaseBody", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        try {
            JSONObject json = (JSONObject) new JSONTokener(remoteMessage.getNotification().getBody()).nextValue();
            JSONObject json2 = json.getJSONObject("data");
            String message = json2.get("message").toString();
            String search = json2.get("search").toString();



//        String nhMessage = bundle.getString("message");
//        String nhSearch = bundle.getString("Search");
//        Log.e("nhMessage", "" + bundle.getString("message"));
//        Log.i("Bundle", "" + bundle.toString());
        if (MainActivity.isVisible) {
            MainActivity.mainActivity.ToastNotify(message);
        }
        if (!search.trim().isEmpty()) {
            searchNotification = true;
            searchTerm = search;
        } else {
            searchNotification = false;
            searchTerm = "";
        }
        notify("",message,CompanyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

////        sendNotification(nhMessage);
//        NOTIFICATION_ID = nhMessage.hashCode();
    }

    private void notify(String title,String body,String CompanyId) {
        Intent intent = null;
        int comId= Integer.parseInt(CompanyId);
        Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;
        if (CompanyId!=""){

            intent = new Intent(ctx, LauncherActivity.class);
            intent.putExtra(Constant.SEND_TO_DETAIL_SCREEN, true);
            intent.putExtra(Constant.COMPANY_ID, comId);
//            intent = new Intent(ctx, ViewCompanyDetailsActivity.class);
//            intent.putExtra(Constant.ToolbarTitle, "");
//            intent.putExtra(Constant.COMPANY_ID, comId);
//            intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, "");
//            intent.putExtra(Constant.COMPANY_PROFILE_NAME, "");
//            intent.putExtra(Constant.COMPANY_PROFILE_TAG, "");
//            intent.putExtra(Constant.COMPANY_PROFILE_VERIFIED, "");
//            intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, "");
//            intent.putExtra(Constant.COMPANY_PROFILE_PHONE, "");
//            intent.putExtra(Constant.FROM_PUSH_NOTIFICATION, true);


        }else {
            intent = new Intent(ctx, LauncherActivity.class);
            intent.putExtra(Constant.SEND_TO_HOME_SCREEN, true);

//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (searchNotification) {
           // intent.putExtra("Search", searchTerm);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(bitmap)
//                .setContentTitle(ctx.getString(R.string.app_name))
                .setContentTitle(title)
                .setContentIntent(contentIntent)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));

        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setSound(soundUri,audioAttributes);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        Long notificationId = SystemClock.currentThreadTimeMillis();
        mNotificationManager.notify(notificationId.intValue(), notificationBuilder.build());
    }

//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                        .setContentTitle(getString(R.string.fcm_message))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
}