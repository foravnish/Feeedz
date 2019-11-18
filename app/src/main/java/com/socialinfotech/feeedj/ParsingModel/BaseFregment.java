package com.socialinfotech.feeedj.ParsingModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.socialinfotech.feeedj.AppUtils.Constant;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Social Infotech on 6/13/2016.
 */
public abstract class BaseFregment extends Fragment {
    private SharedPreferences sPref;
    public static FeeedjAPIClass  git;
    private static final String ARGS_INSTANCE = "com.ncapdevi.sample.argsInstance";
    private int mInt = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());



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
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
