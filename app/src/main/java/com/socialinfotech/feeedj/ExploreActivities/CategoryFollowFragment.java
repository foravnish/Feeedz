package com.socialinfotech.feeedj.ExploreActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.CompnayTagListingResponce;
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

/**
 * Created by fi8er1 on 12/01/2018.
 */

public class CategoryFollowFragment extends Fragment {

    @Bind(R.id.list)
    EasyRecyclerView recyclerView;
    protected FeeedjAPIClass git;
    SharedPreferences sPref;
    ProgressDialog pDialog;
    int myValue;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_follow_list, container, false);
        ButterKnife.bind(this, view);
        Fresco.initialize(getActivity());
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        Amplitude.getInstance().logEvent("Companies List");
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//                        String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
//                        String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
//                        if (api_key.length() > 0) {
//                            request.addHeader("Authorization", api_barear + " " + api_key);
//                        }
//                    }
//                })
//                .setEndpoint(FeeedjAPIClass.API).build();



        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
                String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            myValue = bundle.getInt("message");
            title = bundle.getString("title");
            getCompanyList();
        }
        return view;
    }

    public void getCompanyList() {
        Call<CompnayTagListingResponce> callCompanyTagListingResponse = git.getCompanyList(myValue);
        callCompanyTagListingResponse.enqueue(new Callback<CompnayTagListingResponce>() {
            @Override
            public void onResponse(Call<CompnayTagListingResponce> call, Response<CompnayTagListingResponce> response) {
                if (response.body().getCompanies().size() > 0) {
                    recyclerView.setAdapter(new CategoryFollowViewAdapter(response.body().getCompanies(), getActivity()));
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CompnayTagListingResponce> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        });
    }
}
