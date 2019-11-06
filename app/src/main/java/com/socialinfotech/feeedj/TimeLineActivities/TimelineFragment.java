package com.socialinfotech.feeedj.TimeLineActivities;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.CustomEditText;
import com.socialinfotech.feeedj.AppUtils.CustomGridView;
import com.socialinfotech.feeedj.AppUtils.EditTextImeBackListener;
import com.socialinfotech.feeedj.AppUtils.TextViewPlus;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.BaseFregment;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.ParsingModel.GetFrequentSearchPhrasesResponse;
import com.socialinfotech.feeedj.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineFragment extends BaseFregment implements View.OnClickListener {

    boolean launch;
    EasyRecyclerView recyclerView;
    SharedPreferences sPref;
    private CustomEditText etSearch;
    SharedPreferences.Editor editor;
    String TOKEN;
    TextView txt_hint;
    ProgressBar pbTimelineSearchSection;
    LinearLayoutManager verticalLayoutManager;
    private LinearLayout llTimelineSearch;
    private RelativeLayout rlTimelineMain;
    private RelativeLayout rlTimelineSearchSectionButtonOne;
    private RelativeLayout rlTimelineSearchSectionButtonTwo;
    private RelativeLayout rlTimelineSearchSectionButtonThree;
    private TextViewPlus tvTimelineSearchSectionButtonOne;
    private TextViewPlus tvTimelineSearchSectionButtonTwo;
    private TextViewPlus tvTimelineSearchSectionButtonThree;
    private CustomGridView gvTimelineSearchSectionTags;
    private ScrollView scTimelineSearchSection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Success");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Utility.brochureInitialized = false;
        editor = sPref.edit();
        TOKEN = sPref.getString(Constant.ACCESS_TOKEN, "");
        launch = sPref.getBoolean(Constant.MAIN_Launcher, true);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        txt_hint = view.findViewById(R.id.txt_hint);
        etSearch = view.findViewById(R.id.et_search);
        etSearch.setOnClickListener(this);
        etSearch.setCursorVisible(false);

        llTimelineSearch = view.findViewById(R.id.ll_timeline_search);

        rlTimelineMain = view.findViewById(R.id.rl_timeline_main);
        rlTimelineSearchSectionButtonOne = view.findViewById(R.id.rl_timeline_search_section_btn_one);
        rlTimelineSearchSectionButtonOne.setOnClickListener(this);
        rlTimelineSearchSectionButtonTwo = view.findViewById(R.id.rl_timeline_search_section_btn_two);
        rlTimelineSearchSectionButtonTwo.setOnClickListener(this);
        rlTimelineSearchSectionButtonThree = view.findViewById(R.id.rl_timeline_search_section_btn_three);
        rlTimelineSearchSectionButtonThree.setOnClickListener(this);

        tvTimelineSearchSectionButtonOne = view.findViewById(R.id.tv_timeline_search_section_btn_one);
        tvTimelineSearchSectionButtonTwo = view.findViewById(R.id.tv_timeline_search_section_btn_two);
        tvTimelineSearchSectionButtonThree = view.findViewById(R.id.tv_timeline_search_section_btn_three);

        scTimelineSearchSection = view.findViewById(R.id.sc_timeline_search_section);

        pbTimelineSearchSection = view.findViewById(R.id.pb_timeline_search_section);

        gvTimelineSearchSectionTags = view.findViewById(R.id.grid_timeline_search);
        gvTimelineSearchSectionTags.setExpanded(true);

        etSearch.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(CustomEditText ctrl, String text) {
                if (llTimelineSearch.getVisibility() == View.VISIBLE) {
                    etSearch.setText("");
                    etSearch.setCursorVisible(false);
                    llTimelineSearch.setVisibility(View.GONE);
                    gvTimelineSearchSectionTags.setVisibility(View.GONE);
                }
            }
        });

        verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String notificationSearch = bundle.getString("Search");
            if (notificationSearch != null && !notificationSearch.equals("")) {
                Utility.search(getActivity(), notificationSearch);
            }
        }

        recyclerView.setRefreshListener(() -> {
            txt_hint.setVisibility(View.GONE);
            getAllFeeds();
        });

        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (etSearch.getText().toString().trim().length() > 0) {
                    llTimelineSearch.setVisibility(View.GONE);
                    gvTimelineSearchSectionTags.setVisibility(View.GONE);
                    scTimelineSearchSection.scrollTo(0,0);
                    Utility.hideSoftKeybaord(getActivity(), etSearch);
                    Utility.search(getActivity(), etSearch.getText().toString());
                    etSearch.setText("");
                    etSearch.setCursorVisible(false);
                    return true;
                }
            }
            return false;
        });

        recyclerView.setRefreshingColor(Color.parseColor("#B82580"));//Change this to read the primary colorS
        recyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        if (launch) {
            ShowDialog();
        }
        if (sPref.getString(Constant.TIMELINE, "").equals("")) {
            getAllFeeds();
        } else {
            txt_hint.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            String json = sPref.getString(Constant.TIMELINE, "");
            GetAllOffersResponse[] obj = gson.fromJson(json, GetAllOffersResponse[].class);
            recyclerView.setAdapterWithProgress(new MyItemRecyclerViewAdapter(obj, getActivity()));
            try {
                if (getArguments() != null) {
                    Log.d("OnAPPLaunchLoading", "ArgumentExists");
                    if (getArguments().getString("firstAppLaunch") == "YES") {
                        getAllFeeds();
                        Log.d("OnAPPLaunchLoading", "SUCCESS");
                    }
                }
            } catch (Exception ex) {
                Log.d("Error", "Couldn't get arguments");
            }
        }
        Log.i("onCreateView", "Success");
        return view;
    }

    private void getAllFeeds() {
        Call<GetAllOffersResponse[]> callGetAllOffersResponse = git.getAllOffers();
        callGetAllOffersResponse.enqueue(new Callback<GetAllOffersResponse[]>() {
            @Override
            public void onResponse(Call<GetAllOffersResponse[]> call, Response<GetAllOffersResponse[]> response) {
                if (response.body().length > 0) {
                    Log.i("GetAllFeeds", "Success");
                    Log.d("Response",""+response);
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    editor.putString(Constant.TIMELINE,json).apply();
                    if (TimelineFragment.this.isVisible()) {
                        recyclerView.setAdapterWithProgress(new MyItemRecyclerViewAdapter(response.body(), getActivity()));
                    }
                    editor.putString(Constant.FIRSTOFFER, response.body()[0].getCompany().getCompanyName_Ar()).apply();
                }
            }

            @Override
            public void onFailure(Call<GetAllOffersResponse[]> call, Throwable t) {}
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
        btn_ok.setOnClickListener(v -> {
            finalDialog.dismiss();
            startActivity(new Intent(getActivity(), SplashActivity.class));

        });
        btn_cnl.setOnClickListener(v -> finalDialog.dismiss());
        dialog.show();
    }

    private void ShowDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_on_mainscreen_launch);
        TextView txt_read = dialog.findViewById(R.id.txt);
        final Dialog finalDialog = dialog;
        txt_read.setOnClickListener(v -> {
            finalDialog.dismiss();
            editor.putBoolean(Constant.MAIN_Launcher, false).commit();
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        etSearch.setText("");
        etSearch.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.et_search:
                if (llTimelineSearch.getVisibility() == View.GONE) {
                    scTimelineSearchSection.scrollTo(0,0);
                    gvTimelineSearchSectionTags.setVisibility(View.GONE);
                    pbTimelineSearchSection.setVisibility(View.VISIBLE);
                    llTimelineSearch.setVisibility(View.VISIBLE);
                    etSearch.setCursorVisible(true);
                    getFrequentlySearchedPhrases();
                }
                break;
            case R.id.rl_timeline_search_section_btn_one:
                etSearch.setText("");
                etSearch.setCursorVisible(false);
                llTimelineSearch.setVisibility(View.GONE);
                gvTimelineSearchSectionTags.setVisibility(View.GONE);
                scTimelineSearchSection.scrollTo(0,0);
                Utility.hideSoftKeybaord(getActivity(), etSearch);
                Utility.search(getActivity(), "#" + tvTimelineSearchSectionButtonOne.getText().toString());
                break;
            case R.id.rl_timeline_search_section_btn_two:
                etSearch.setText("");
                etSearch.setCursorVisible(false);
                llTimelineSearch.setVisibility(View.GONE);
                gvTimelineSearchSectionTags.setVisibility(View.GONE);
                scTimelineSearchSection.scrollTo(0,0);
                Utility.hideSoftKeybaord(getActivity(), etSearch);
                Utility.search(getActivity(), "#" + tvTimelineSearchSectionButtonTwo.getText().toString());
                break;
            case R.id.rl_timeline_search_section_btn_three:
                etSearch.setText("");
                etSearch.setCursorVisible(false);
                llTimelineSearch.setVisibility(View.GONE);
                gvTimelineSearchSectionTags.setVisibility(View.GONE);
                scTimelineSearchSection.scrollTo(0,0);
                Utility.hideSoftKeybaord(getActivity(), etSearch);
                Utility.search(getActivity(), "#" + tvTimelineSearchSectionButtonThree.getText().toString());
                break;
        }
    }

    private void getFrequentlySearchedPhrases() {
        Call<GetFrequentSearchPhrasesResponse[]> getFrequentSearchPhrasesResponseCall = git.GetFrequentSearchPhrase();
        getFrequentSearchPhrasesResponseCall.enqueue(new Callback<GetFrequentSearchPhrasesResponse[]>() {
            @Override
            public void onResponse(Call<GetFrequentSearchPhrasesResponse[]> call, Response<GetFrequentSearchPhrasesResponse[]> response) {
                pbTimelineSearchSection.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().length > 0) {
                    ArrayList<String> tagPhrases = new ArrayList<>();
                    for (GetFrequentSearchPhrasesResponse aGetFrequentSearchPhrasesResponse : response.body()) {
                        tagPhrases.add(aGetFrequentSearchPhrasesResponse.getWord());
                        Log.e("tags", "" + aGetFrequentSearchPhrasesResponse.getWord());
                    }
                    gvTimelineSearchSectionTags.setAdapter(new TagAdapter(tagPhrases));
                    gvTimelineSearchSectionTags.setVisibility(View.VISIBLE);
                    Log.e("tags", "" + tagPhrases);
                }
            }

            @Override
            public void onFailure(Call<GetFrequentSearchPhrasesResponse[]> call, Throwable t) {}
        });
    }

    public class TagAdapter extends BaseAdapter {

        ArrayList<String> tagList;

        TagAdapter(ArrayList<String> list) {
            tagList = list;
        }

        @Override
        public int getCount() {
            return tagList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_row_timeline_search_frequent_phrases, null);
            } else {}
            TextViewPlus tvTag = convertView.findViewById(R.id.tv_timeline_search_section_tag);
            tvTag.setText(tagList.get(position));
            tvTag.setOnClickListener(view -> {
                etSearch.setText("");
                etSearch.setCursorVisible(false);
                llTimelineSearch.setVisibility(View.GONE);
                Utility.hideSoftKeybaord(getActivity(), etSearch);
                Utility.search(getActivity(), tagList.get(position));
            });
            return convertView;
        }
    }
}