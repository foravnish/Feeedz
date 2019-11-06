package com.socialinfotech.feeedj.ApplicationActivities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ExploreActivities.ExploreFragment;
import com.socialinfotech.feeedj.FollowingActivities.FollowingFragment;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.NotificationResponse;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.SettingActivities.SettingsFragment;
import com.socialinfotech.feeedj.TimeLineActivities.TimelineFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_home)
    RelativeLayout btnHome;
    @Bind(R.id.btn_following)
    RelativeLayout btnFollowing;
    @Bind(R.id.btn_explor)
    RelativeLayout btnExplor;
    @Bind(R.id.btn_main_settings)
    RelativeLayout btnSettings;
    @Bind(R.id.main_bottom)
    LinearLayout mainBottom;
    @Bind(R.id.img_bottom)
    ImageView imgBottom;

    //Push notification related
    public static MainActivity mainActivity;
    public static Boolean isVisible = false;
//    private GoogleCloudMessaging gcm;
    private SharedPreferences sPref;
    SharedPreferences.Editor editor;
    public static FeeedjAPIClass  git;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i("mainactivityintent", "" + getIntent().getStringExtra("Search"));

        Bundle bundle = new Bundle();
        bundle.putString("firstAppLaunch", "YES");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        TimelineFragment f11 = new TimelineFragment();
        f11.setArguments(bundle);
        fragmentTransaction.add(R.id.container, f11).commit();

        mainActivity = this;
//        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, MyHandler.class);
//        registerForPushNotifications();
        Log.i("TAG", "loaded");

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sPref.edit();

        Interceptor interceptor = chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
            String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
            if (api_key.length() > 0) {
                requestBuilder.addHeader("Authorization", api_barear + " " + api_key);
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit restAdapter1 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(FeeedjAPIClass.API).addConverterFactory(GsonConverterFactory.create()).build();

        git = restAdapter1.create(FeeedjAPIClass.class);

        if (sPref.getBoolean(Constant.PENDING_NOTIFICATION_REGISTRATION, true)) {
            registerForPushNotifications();
        }

        Amplitude.getInstance().logEvent("Timeline");

    }

    @OnClick({R.id.btn_home, R.id.btn_following, R.id.btn_explor, R.id.btn_main_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                SelectFromMenu(1);
                break;
            case R.id.btn_following:
                SelectFromMenu(2);
                break;
            case R.id.btn_explor:
                SelectFromMenu(3);
                break;
            case R.id.btn_main_settings:
                SelectFromMenu(4);
                break;
        }
    }

    public void SelectFromMenu(int i) {
        if (i == 1) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            TimelineFragment f11 = new TimelineFragment();
            fragmentTransaction.replace(R.id.container, f11);
                fragmentTransaction.addToBackStack("TimelineFragment");
            fragmentTransaction.commit();
            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_one));
        } else if (i == 2) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FollowingFragment f11 = new FollowingFragment();
            fragmentTransaction.replace(R.id.container, f11);
                fragmentTransaction.addToBackStack("FollowingFragment");
            fragmentTransaction.commit();
            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_two));
        } else if (i == 3) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            ExploreFragment f11 = new ExploreFragment();
            fragmentTransaction.replace(R.id.container, f11);
            fragmentTransaction.addToBackStack("ExploreFragment");
            fragmentTransaction.commit();
            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_three));
        } else if (i == 4) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            SettingsFragment f11 = new SettingsFragment();
            fragmentTransaction.replace(R.id.container, f11);

            fragmentTransaction.addToBackStack("SettingsFragment");
            fragmentTransaction.commit();

            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_four));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if(fragments != null){
                for(Fragment fragment : fragments){
                    if(fragment != null && fragment.isVisible())
                        if (fragment instanceof TimelineFragment){
                            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_one));
                        } else if(fragment instanceof FollowingFragment){
                            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_two));
                        } else if(fragment instanceof ExploreFragment){
                            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_three));
                        } else if(fragment instanceof SettingsFragment){
                            imgBottom.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.tabbar_four));
                        }
                }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i(TAG, "This device is not supported by Google Play Services.");
                //ToastNotify("This device is not supported by Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerForPushNotifications() {
        JsonObject obj = new JsonObject();
        obj.addProperty("Handle", sPref.getString(Constant.FIREBASE_TOKEN, ""));
        obj.addProperty("Platform", "gcm");

        Call<NotificationResponse> callNotificationRegistration = git.registerForNotification(obj);
        callNotificationRegistration.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                Log.e("notificationResponse", response.body().getSuccess().toString());
                if (response.isSuccessful()) {
                    editor.putBoolean(Constant.PENDING_NOTIFICATION_REGISTRATION, false).apply();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
            TextView helloText = findViewById(R.id.txt);
            helloText.setText(notificationMessage);
        });
    }

}
