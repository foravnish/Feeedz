package com.socialinfotech.feeedj.ApplicationActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeaturedResponse;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeaturedCompany extends AppCompatActivity {
    EasyRecyclerView recyclerView;
    protected FeeedjAPIClass git;
    SharedPreferences Spref;
    SharedPreferences.Editor editor;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.txt_empty)
    TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_company);
        ButterKnife.bind(this);
        Fresco.initialize(FeaturedCompany.this);
        Spref = PreferenceManager.getDefaultSharedPreferences(FeaturedCompany.this);
        editor = Spref.edit();
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//                        String api_key = Spref.getString(Constant.ACCESS_TOKEN, "");
//                        String api_barear = Spref.getString(Constant.TOKEN_TYPE, "");
//                        if (api_key.length() > 0) {
//                            request.addHeader("Authorization", api_barear + " " + api_key);
//                        }
//
//                    }
//                })
//                .setEndpoint(FeeedjAPIClass.API).build();


        Interceptor interceptor = chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            String api_key = Spref.getString(Constant.ACCESS_TOKEN, "");
            String api_barear = Spref.getString(Constant.TOKEN_TYPE, "");
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
                .client(okHttpClient).baseUrl(FeeedjAPIClass.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        git = restAdapter1.create(FeeedjAPIClass.class);

        recyclerView = findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(FeaturedCompany.this));

        btnBack.setOnClickListener(v -> onBackPressed());

        Call<FeaturedResponse[]> callFeaturedResponse = git.getFeatured();
        callFeaturedResponse.enqueue(new Callback<FeaturedResponse[]>() {
            @Override
            public void onResponse(Call<FeaturedResponse[]> call, Response<FeaturedResponse[]> response) {
                if (response.body().length > 0) {
                    recyclerView.setAdapterWithProgress(new FeatureViewadapter(response.body(), FeaturedCompany.this));
                    txtEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    txtEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FeaturedResponse[]> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(FeaturedCompany.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                }
                Log.e("asd", t.toString());
            }
        });
    }
}
