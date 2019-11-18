package com.socialinfotech.feeedj.ExploreActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.JsonObject;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.ParsingModel.GetSearchResponse;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.SearchActivity.CategoriesFeedSearchRecyclerViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.socialinfotech.feeedj.ParsingModel.BaseFregment.git;

/**
 * Created by fi8er1 on 10/01/2018.
 */

public class CategoryFeedsFragment extends Fragment {

    boolean isCategoryFeedsFragmentVisible;
    ProgressDialog pDialog;
    EasyRecyclerView recyclerViewCategoryFeeds;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.img_no_result)
    ImageView imgNoResult;
    Bundle bundle;
    String feedsTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_feeds, container, false);
        Fresco.initialize(getActivity());
        ButterKnife.bind(this, view);
        Amplitude.getInstance().logEvent("Search Results");
        bundle = getActivity().getIntent().getExtras();
        feedsTitle = "#" + bundle.getString("title");

        recyclerViewCategoryFeeds = view.findViewById(R.id.list);
//        recyclerViewCategoryFeeds.setNestedScrollingEnabled(false);
//        recyclerViewCategoryFeeds.setHasFixedSize(true);
//        recyclerViewCategoryFeeds.setItemViewCacheSize(10);
        recyclerViewCategoryFeeds.setDrawingCacheEnabled(true);
        recyclerViewCategoryFeeds.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewCategoryFeeds.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCategoryFeeds.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeeds(feedsTitle);
            }
        });

        pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        getFeeds(feedsTitle);
        return view;
    }

    public void getFeeds(final String s) {
        pDialog.show();
        JsonObject obj = new JsonObject();
        obj.addProperty("TagNames", s);
        if (!obj.isJsonNull()) {
            Call<GetSearchResponse[]> getSearchResponseCall = git.getSearchResult(obj);
            getSearchResponseCall.enqueue(new Callback<GetSearchResponse[]>() {
                @Override
                public void onResponse(Call<GetSearchResponse[]> call, Response<GetSearchResponse[]> response) {
//                assert response.body() != null;
                Log.e("CategotyData",   ""+response.body());
                    if (response.body() != null && response.body().length > 0) {
                        ll1.setVisibility(View.VISIBLE);
                        imgNoResult.setVisibility(View.GONE);
                        recyclerViewCategoryFeeds.setAdapter(new CategoriesFeedSearchRecyclerViewAdapter(response.body(), getActivity()));
                        pDialog.dismiss();
                    } else {
                        ll1.setVisibility(View.GONE);
                        imgNoResult.setVisibility(View.VISIBLE);
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<GetSearchResponse[]> call, Throwable t) {
                    if (isCategoryFeedsFragmentVisible) {
                        ll1.setVisibility(View.GONE);
                        imgNoResult.setVisibility(View.VISIBLE);
                        pDialog.dismiss();
                    }
                }
            });
        } else {
            ll1.setVisibility(View.GONE);
            imgNoResult.setVisibility(View.VISIBLE);
            pDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isCategoryFeedsFragmentVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isCategoryFeedsFragmentVisible = true;
    }
}
