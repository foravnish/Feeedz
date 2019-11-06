package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Social Infotech on 6/17/2016.
 */
public class Home extends Application {

    protected static FeeedjAPIClass git;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
//        FacebookSdk.sdkInitialize(getApplicationContext());

        FirebaseApp.initializeApp(this);

        // Initializing twitter SDK
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("Ezdda4qifGO4TAMBM2CSFOwn6", "7rCYvnyJUfrBNYYVqDBuynGoBDCffXBjI6ToRkejaPGFomeIeB"))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)
                .build();
        Twitter.initialize(config);

        AppEventsLogger.activateApp(this);

        Fresco.initialize(this);

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
        Retrofit restAdapter = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FeeedjAPIClass.API).build();

        git = restAdapter.create(FeeedjAPIClass.class);
    }


    public static void reportOfferLikes(int offerID, int rating) {
        Call<JsonObject> reportOfferLikes = git.reportOfferLikes(offerID, rating);
        reportOfferLikes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferLikes", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferLikes", "FAILED");
            }
        });
    }


    public static void reportOfferViewed(int offerID) {
        Call<JsonObject> reportOfferViewed = git.reportOfferViewed(offerID);
        reportOfferViewed.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferViewed", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferViewed", "FAILED" + " " + t.getMessage());
            }
        });
    }

    public static void reportOfferCalled(int offerID) {
        Call<JsonObject> reportOfferCalled = git.reportOfferCalled(offerID);
        reportOfferCalled.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferCalled", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferCalled", "FAILED" + " " + t.getMessage());
            }
        });
    }


    public static void reportOfferLocation(int offerID) {
        Call<JsonObject> reportOfferLocation = git.reportOfferLocation(offerID);
        reportOfferLocation.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferLocation", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferLocation", "FAILED" + " " + t.getMessage());
            }
        });
    }

    public static void reportOfferShared(int offerID) {
        Call<JsonObject> reportOfferShared = git.reportOfferShared(offerID);
        reportOfferShared.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferShared", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferShared", "FAILED" + " " + t.getMessage());
            }
        });
    }

    public static void reportOfferImageViewed(int offerID) {
        Call<JsonObject> reportOfferImageViewed = git.reportOfferImageViewed(offerID);
        reportOfferImageViewed.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportOfferImageViewed", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportOfferImageViewed", "FAILED" + " " + t.getMessage());
            }
        });
    }

}
