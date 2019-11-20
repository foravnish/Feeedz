package com.socialinfotech.feeedj.ApplicationActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.amplitude.api.Amplitude;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.util.Locale;


public class LauncherActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent intent = null;
    int comId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this);

        if(sharedPreferences.getString(Constant.USERNAME, "").equals(""))
            Amplitude.getInstance().initialize(this, "1ff858747a29bd6f01fd5d2f7311d093").enableForegroundTracking(getApplication());
        else
            Amplitude.getInstance().initialize(this, "1ff858747a29bd6f01fd5d2f7311d093", sharedPreferences.getString(Constant.USERNAME, "")).enableForegroundTracking(getApplication());

        Amplitude.getInstance().logEvent("AppLaunched");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            String languageToLoad  = "ab"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

        new Handler().postDelayed(() -> {
            if (sharedPreferences.getString(Constant.ACCESS_TOKEN,"").equals("")){
                startActivity(new Intent(LauncherActivity.this, SplashActivity.class));
                finish();
            }else{
                if (getIntent().hasExtra(Constant.SEND_TO_DETAIL_SCREEN)){
                    Bundle bundle = getIntent().getExtras();
                    comId=bundle.getInt(Constant.COMPANY_ID);
                    intent = new Intent(LauncherActivity.this, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, "");
                    intent.putExtra(Constant.COMPANY_ID, comId);
                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, "");
                    intent.putExtra(Constant.COMPANY_PROFILE_NAME, "");
                    intent.putExtra(Constant.COMPANY_PROFILE_TAG, "");
                    intent.putExtra(Constant.COMPANY_PROFILE_VERIFIED, "");
                    intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, "");
                    intent.putExtra(Constant.COMPANY_PROFILE_PHONE, "");
                    intent.putExtra(Constant.FROM_PUSH_NOTIFICATION, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    finish();
                }
            }
        },2000);
    }
}
//
//[6/22/2016 9:56:55 AM] LianJing: 1- After login the status bar color should change to the same as the background
//        26- links and phone numbers in offer details page are not clickable
//        27- zooming problem, the image can go away and thus its a bad user experience