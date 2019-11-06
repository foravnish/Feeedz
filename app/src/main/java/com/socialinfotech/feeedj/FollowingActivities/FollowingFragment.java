package com.socialinfotech.feeedj.FollowingActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.MainActivity;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.BaseFregment;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowingFragment extends BaseFregment {

    EasyRecyclerView recyclerView;
    @Bind(R.id.img_empty)
    ImageView imgEmpty;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    String TOKEN;
    TextView txt_hint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getActivity());
        Amplitude.getInstance().logEvent("Filtered Timeline");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_list, container, false);
        ButterKnife.bind(this, view);
        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sPref.edit();
        TOKEN = sPref.getString(Constant.ACCESS_TOKEN, "");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        txt_hint = view.findViewById(R.id.txt_hint);
        recyclerView.setRefreshListener(() -> {
            txt_hint.setVisibility(View.GONE);
            FetchFollowingData();
        });
        recyclerView.setRefreshingColor(Color.parseColor("#B82580"));
        recyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    // Recycle view scrolling up...
                } else if (dy > 0) {
                    // Recycle view scrolling down...
                    txt_hint.setVisibility(View.GONE);
                }
            }
        });

        // No longer required here now, checking in the settings fragment.

//        if (TOKEN.equals("")){
//            btnSetting.setVisibility(View.GONE);
//        }else{
//            btnSetting.setVisibility(View.VISIBLE);
//        }

        if (TOKEN.equals("")) {
            imgEmpty.setVisibility(View.VISIBLE);
            txt_hint.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
//        } else if (sPref.getString(Constant.FOLLOWING,"").equals("")) {
        } else {
//            imgEmpty.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            Gson gson = new Gson();
//            String json = sPref.getString(Constant.FOLLOWING, "");
//            GetAllOffersResponse[] obj = gson.fromJson(json, GetAllOffersResponse[].class);
//            recyclerView.setAdapterWithProgress(new FollowingViewAdapter(obj, getActivity()));

            FetchFollowingData();
        }
        return view;
    }

    private void FetchFollowingData() {
        Call<GetAllOffersResponse[]> getAllOffersResponseCall = git.getSubsribeOffer();
        getAllOffersResponseCall.enqueue(new Callback<GetAllOffersResponse[]>() {
            @Override
            public void onResponse(Call<GetAllOffersResponse[]> call, Response<GetAllOffersResponse[]> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                editor.putString(Constant.FOLLOWING, json).apply();
                if (response.body() != null &&
                        response.body().length > 0 &&
                        FollowingFragment.this.isVisible()) {
                        imgEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapterWithProgress(new FollowingViewAdapter(response.body(), getActivity()));
                } else {
                    if (FollowingFragment.this.isVisible()) {
                        editor.remove(Constant.FOLLOWING).commit();
                        imgEmpty.setVisibility(View.VISIBLE);
                        txt_hint.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllOffersResponse[]> call, Throwable t) {
                if (FollowingFragment.this.isVisible()) {
                    imgEmpty.setVisibility(View.VISIBLE);
                    txt_hint.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void ShowwarningDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_login_warning);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(v -> finalDialog.dismiss());
        btn_cnl.setOnClickListener(v -> {
            finalDialog.dismiss();
            startActivity(new Intent(getActivity(), SplashActivity.class));
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.img_empty)
    public void onClick() {
        if (TOKEN.equals("")) {
            ShowwarningDialog();
        } else {
            Activity activity123 = getActivity();
            if(activity123 instanceof MainActivity) {
                ((MainActivity) activity123).SelectFromMenu(4);
            }
        }
    }
}
