package com.socialinfotech.feeedj.SearchActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.JsonObject;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.ParsingModel.GetSearchResponse;
import com.socialinfotech.feeedj.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.socialinfotech.feeedj.ParsingModel.BaseFregment.git;

/**
 * Created by fi8er1 on 02/10/2016.
 */

public class SearchResultActivity extends AppCompatActivity {

    boolean isSearchResultFragmentVisible;
    ProgressDialog pDialog;
    EasyRecyclerView recyclerViewSearchResult;

    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.img_no_result)
    ImageView imgNoResult;
    @Bind(R.id.tv_search_result_title)
    TextView txtSearchTitle;
    Bundle bundle;
    String searchTermInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Search Results");

        isSearchResultFragmentVisible = true;

        setContentView(R.layout.fragment_search_result);
        Fresco.initialize(this);
        ButterKnife.bind(this);

        bundle = getIntent().getExtras();
        searchTermInput = bundle.getString("SearchTerm");
        txtSearchTitle.setText(searchTermInput);

        recyclerViewSearchResult = findViewById(R.id.list);
//        recyclerViewSearchResult.setNestedScrollingEnabled(false);
//        recyclerViewSearchResult.setHasFixedSize(true);
//        recyclerViewSearchResult.setItemViewCacheSize(10);
        recyclerViewSearchResult.setDrawingCacheEnabled(true);
        recyclerViewSearchResult.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewSearchResult.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResult.setRefreshListener(() -> search(searchTermInput));

        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        search(searchTermInput);
    }

    public void search(String s) {
        if (isSearchResultFragmentVisible) {
            pDialog.show();
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("TagNames", s);

        if (!obj.isJsonNull()) {
            Call<GetSearchResponse[]> callGetSearchResponse = git.getSearchResult(obj);
            callGetSearchResponse.enqueue(new Callback<GetSearchResponse[]>() {
                @Override
                public void onResponse(Call<GetSearchResponse[]> call, Response<GetSearchResponse[]> response) {
                    if (isSearchResultFragmentVisible) {
                        if (response.body() != null && response.body().length > 0) {
                            ll1.setVisibility(View.VISIBLE);
                            imgNoResult.setVisibility(View.GONE);
                            recyclerViewSearchResult.setAdapter(new MySearchFragmentRecyclerViewAdapter(response.body(), SearchResultActivity.this));
                            recyclerViewSearchResult.getViewTreeObserver().addOnGlobalLayoutListener(() -> pDialog.dismiss());
                        } else {
                            ll1.setVisibility(View.GONE);
                            imgNoResult.setVisibility(View.VISIBLE);
                            pDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetSearchResponse[]> call, Throwable t) {
                    if (isSearchResultFragmentVisible) {
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

    @OnClick(R.id.btn_setting)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        isSearchResultFragmentVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isSearchResultFragmentVisible = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
