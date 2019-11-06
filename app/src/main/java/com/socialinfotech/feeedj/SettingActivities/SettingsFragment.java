package com.socialinfotech.feeedj.SettingActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.facebook.login.LoginManager;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.FeaturedCompany;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.BaseFregment;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.GetPersonalInforesponse;
import com.socialinfotech.feeedj.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsFragment extends BaseFregment implements View.OnClickListener {

    EditText etUsername;
    EditText etEmailid;
    SharedPreferences Spref;
    SharedPreferences.Editor editor;
    Button btnFirst;
    ImageView changePassword;
    ImageView btnContact;
    ImageView btnAds;
    ImageView btnLogout;
    LinearLayout llSettingsLoggedInView;
    private int counter;
    protected FeeedjAPIClass git;

    TextView tv_settings_flag;

    TextView tv_developed_by;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Amplitude.getInstance().logEvent("Settings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settingfragment, container, false);
//        ButterKnife.bind(this, view);
        etUsername = view.findViewById(R.id.et_username);
        etEmailid = view.findViewById(R.id.et_emailid_settings);
        btnFirst = view.findViewById(R.id.btn_first);
        changePassword = view.findViewById(R.id.btn_change_password);
        btnContact = view.findViewById(R.id.btn_contact);
        btnAds = view.findViewById(R.id.btn_ads);
        btnLogout = view.findViewById(R.id.btn_logout);
        tv_settings_flag = view.findViewById(R.id.tv_settings_flag);
        tv_settings_flag.setOnClickListener(this);
        tv_developed_by = view.findViewById(R.id.tv_developed_by);
        btnFirst.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnAds.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tv_settings_flag.setOnClickListener(this);


        llSettingsLoggedInView = view.findViewById(R.id.ll_settings_logged_in_view);

        Spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = Spref.edit();

        TOKEN = Spref.getString(Constant.ACCESS_TOKEN,"");
        if (TOKEN.equals("")){
            llSettingsLoggedInView.setVisibility(View.GONE);
            btnLogout.setBackgroundResource(R.drawable.registersettings);
            //btnLogout.setBackgroundResource(R.drawable.login_btn);//Make it register button
        }else{
            llSettingsLoggedInView.setVisibility(View.VISIBLE);
            btnLogout.setBackgroundResource(R.drawable.logout);
        }

//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//                        String api_key = Spref.getString(Constant.ACCESS_TOKEN, "");
//                        String api_barear = Spref.getString(Constant.TOKEN_TYPE, "");
//                        if (api_key.length() > 0) {
//                            request.addHeader("Authorization", api_barear+ " " + api_key);
//                        }
//                    }
//                })
//                .setEndpoint(FeeedjAPIClass.API).build();
//
//        git = restAdapter.create(FeeedjAPIClass.class);


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                String api_key = Spref.getString(Constant.ACCESS_TOKEN, "");
                String api_barear = Spref.getString(Constant.TOKEN_TYPE, "");
                if (api_key.length() > 0) {
                    requestBuilder.addHeader("Authorization", api_barear + " " + api_key);
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit restAdapter1 = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FeeedjAPIClass.API).build();

        git = restAdapter1.create(FeeedjAPIClass.class);

        btnFirst.setText("عدد المتَابعون: " + Spref.getInt(Constant.NUM_SUBSCRIBER,0));
        etEmailid.setText(Spref.getString(Constant.USER_EMAIL,""));
        etUsername.setText(Spref.getString(Constant.USERNAME,""));
        GetPersonalInfo();

        if (Spref.getBoolean(Constant.EXTERNAL_LOGIN, false)) {
            changePassword.setVisibility(View.GONE);
        } else {
            changePassword.setVisibility(View.VISIBLE);
        }
        return view;
    }

    String TOKEN;
    private void GetPersonalInfo() {
        Call<GetPersonalInforesponse> getPersonalInforesponseCall = git.GetPersonalInfo();
        getPersonalInforesponseCall.enqueue(new Callback<GetPersonalInforesponse>() {
            @Override
            public void onResponse(Call<GetPersonalInforesponse> call, Response<GetPersonalInforesponse> response) {
                if (response.isSuccessful()){
                    editor.putString(Constant.USER_EMAIL, response.body().getEmail()).apply();
                    editor.putString(Constant.USERNAME,response.body().getUsername()).apply();
                    editor.putInt(Constant.NUM_SUBSCRIBER,response.body().getNumberOfSubscribtions()).apply();
                    btnFirst.setText("عدد المتَابعون: " + response.body().getNumberOfSubscribtions());
                    etEmailid.setText(response.body().getEmail());
                    etUsername.setText(response.body().getUsername());
                }
            }

            @Override
            public void onFailure(Call<GetPersonalInforesponse> call, Throwable t) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
//                onBackPressed();
                break;
            case R.id.tv_settings_flag:
                counter++;
                if (counter > 4) {
                    tv_developed_by.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_first:
                startActivity(new Intent(getActivity(), FeaturedCompany.class));
                break;
            case R.id.btn_change_password:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.btn_contact:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@feeedz.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "تواصل مع فيييدز");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, "ارسل بريد الكتروني"));
                } catch (android.content.ActivityNotFoundException ex) {

                }
                break;
            case R.id.btn_ads:
                Intent adsIntent = new Intent(Intent.ACTION_SEND);
                adsIntent.setType("message/rfc822");
                adsIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ads@feeedz.com"});
                adsIntent.putExtra(Intent.EXTRA_SUBJECT, "أعلن مع فيييدز");
                adsIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(adsIntent, "ارسل بريد الكتروني"));
                } catch (android.content.ActivityNotFoundException ex) {

                }
                break;
            case R.id.btn_logout:
                editor.remove(Constant.TOKEN_TYPE).apply();
                editor.remove(Constant.ACCESS_TOKEN).apply();
                editor.remove(Constant.TIMELINE).apply();
                editor.remove(Constant.USERNAME).apply();
                editor.remove(Constant.FOLLOWING).apply();
                editor.remove(Constant.LIKE_DIALOG).apply();
                editor.remove(Constant.USER_EMAIL).apply();
                editor.remove(Constant.TIMELINE).apply();
                editor.remove(Constant.EXTERNAL_LOGIN).apply();
                editor.remove(Constant.PENDING_NOTIFICATION_REGISTRATION).apply();
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(getActivity(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                getActivity().finish();
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        counter = 0;
        tv_developed_by.setVisibility(View.GONE);
    }
}