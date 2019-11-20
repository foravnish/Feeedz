package com.socialinfotech.feeedj.TimeLineActivities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.MainActivity;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.GetCompnayDetailResponse;
import com.socialinfotech.feeedj.ParsingModel.GetSimilarCompaniesResponse;
import com.socialinfotech.feeedj.R;

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

public class ViewCompanyDetailsActivity extends AppCompatActivity {
    protected FeeedjAPIClass git;
    @Bind(R.id.list)
    EasyRecyclerView recyclerView;
    int companyID;
    String companyLocation = null;
    String companyPhoneNumber = null;
    String companyEmail = null;
    String companyWebsite = null;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.sdv_add_iamge_header)
    SimpleDraweeView sdvImageHeader;
    @Bind(R.id.sdv_compny_icon)
    SimpleDraweeView sdvCompanyIcon;
//    @Bind(R.id.txt_title)
//    TextView txtTitle;
//    @Bind(R.id.nestedScrollView)
//    NestedScrollView nestedScrollView;

    @Bind(R.id.chk_followed)
    CheckBox chk_followed;

    @Bind(R.id.txt_advrt_title)
    TextView txt_advrt_title;
    @Bind(R.id.txt_compny_tag)
    TextView txt_compny_tag;
    @Bind(R.id.txt_compny_advrt_title)
    TextView txt_compny_advrt_title;
    @Bind(R.id.tv_number_of_offers)
    TextView tv_number_of_offers;

    @Bind(R.id.tv_company_details_working_hours)
    TextView tv_company_details_working_hours;

    @Bind(R.id.ll_company_details_time)
    LinearLayout ll_company_details_time;

    @Bind(R.id.ll_company_details_empty)
    LinearLayout ll_company_details_empty;

    @Bind(R.id.tb_company_details_toolbar)
    Toolbar tb_company_details_toolbar;

    @Bind(R.id.iv_company_details_verified)
    ImageView iv_company_details_verified;

    @Bind(R.id.ll_company_details_action_button_location)
    LinearLayout ll_company_details_action_button_location;

    @Bind(R.id.ll_company_details_action_button_mail)
    LinearLayout ll_company_details_action_button_mail;

    @Bind(R.id.ll_company_details_action_button_web)
    LinearLayout ll_company_details_action_button_web;

    @Bind(R.id.ll_company_details_action_button_call)
    LinearLayout ll_company_details_action_button_call;

    @Bind(R.id.rl_company_details_hours)
    RelativeLayout rl_company_details_hours;

    @Bind(R.id.rl_company_details_similar_companies)
    RelativeLayout rl_company_details_similar_companies;

    @Bind(R.id.iv_company_details_similar_companies_arrow_button)
    ImageView iv_company_details_similar_companies_arrow_button;

    @Bind(R.id.iv_company_details_notification)
    ImageView iv_company_details_notification;

    @Bind(R.id.iv_company_details_notification_icon)
    ImageView iv_company_details_notification_icon;

    @Bind(R.id.iv_company_details_hours_arrow)
    ImageView iv_company_details_hours_arrow;

    @Bind(R.id.tv_company_details_is_open)
    TextView tv_company_details_is_open;

    @Bind(R.id.tv_company_details_working_hours_status)
    TextView tv_company_details_working_hours_status;

    @Bind(R.id.iv_company_details_notifications_no_offer)
    ImageView iv_company_details_notifications_no_offer;
    private final Runnable cancelUnSubscribe = () -> {
        iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
        iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_subscribed_no_offer);
    };
    @Bind(R.id.rv_company_details_similar_companies)
    EasyRecyclerView rv_company_details_similar_companies;
    @Bind(R.id.abl_company_details)
    AppBarLayout abl_company_details;
//    @Bind(R.id.txt_subscribe)
//    TextView txt_subscribe;

    //    @Bind(R.id.sdv_compny_category)
//    SimpleDraweeView sdv_compny_category;
    private GetCompnayDetailResponse mValues = null;
    private final Runnable unSubscribe = () -> unSubscribeForNotification(mValues.getCompanyId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x00000000);  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.addFlags(flags);
        }
//        setContentView(R.layout.main);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        Amplitude.getInstance().logEvent("Company Offers");
        setContentView(R.layout.activity_view_company_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            companyID = bundle.getInt(Constant.COMPANY_ID);
            sdvCompanyIcon.setImageURI(bundle.getString(Constant.COMPANY_PROFILE_PHOTO));
            companyLocation = bundle.getString(Constant.COMPANY_PROFILE_LOCATION);
            companyPhoneNumber = bundle.getString(Constant.COMPANY_PROFILE_PHONE);

            if (bundle.getString(Constant.COMPANY_PROFILE_NAME) != null) {
                txt_advrt_title.setText(bundle.getString(Constant.COMPANY_PROFILE_NAME));
            }
            if (bundle.getString(Constant.COMPANY_PROFILE_TAG) != null) {
                txt_compny_tag.setText(bundle.getString(Constant.COMPANY_PROFILE_TAG));
                if (companyLocation == null) {
                    companyLocation = bundle.getString(Constant.COMPANY_PROFILE_TAG);
                    ll_company_details_action_button_location.setVisibility(View.VISIBLE);
                }
            }

            if (bundle.getBoolean(Constant.COMPANY_PROFILE_VERIFIED)) {
                iv_company_details_verified.setVisibility(View.VISIBLE);
            }
        }

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sPref.edit();

        iv_company_details_notification_icon.setOnClickListener(view -> {
            if (mValues.getNotifications()) {
                Utility.diableCompanyNotificationsDialog(this, getString(R.string.are_you_sure_you_want_to_un_subscribe_notifications), getString(R.string.yes_un_subscribe_notifications), getString(R.string.cancel), unSubscribe, cancelUnSubscribe);
            } else {
                subscribeForNotification(mValues.getCompanyId());
                iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
            }
        });

        tb_company_details_toolbar.setOnClickListener(view -> sdvImageHeader.callOnClick());

        sdvImageHeader.setOnClickListener(view -> {
            if (mValues != null) {
                Intent intent = new Intent(this, ImageViewActivity.class);
                intent.putExtra(Constant.ImageNAme, mValues.getCompanyHeaderPhoto());
                intent.putExtra(Constant.OfferID, 0);
                int[] screenLocation = new int[2];
                sdvImageHeader.getLocationOnScreen(screenLocation);

                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", sdvImageHeader.getWidth()).
                        putExtra("height", sdvImageHeader.getHeight());
                Log.i("height", "" + sdvImageHeader.getHeight());
                this.startActivity(intent);
            }
        });

        sdvCompanyIcon.setOnClickListener(view -> {
            if (mValues != null && mValues.getOffers().get(0).getFeatureType() == 4) {
                Intent intent = new Intent(this, PDFViewActivity.class);
                Log.e("companyID", "" + mValues.getCompanyId());
                intent.putExtra("OFFER_ID", mValues.getOffers().get(0).getOfferId());
                intent.putExtra("OFFER_IMAGE", mValues.getOffers().get(0).getOfferImage());
                intent.putExtra("BROCHURE_PDF_URL", mValues.getOffers().get(0).getAttachmentHTML());
                intent.putExtra("BROCHURE_PDF_TITLE", mValues.getCompanyName_Ar());
                intent.putExtra("COMPANY_NAME", mValues.getCompanyName_Ar());
                intent.putExtra("COMPANY_USER_NAME", mValues.getCompanyName());
//            intent.putExtra("COMPANY_VERIFIED", isVerified);
                intent.putExtra("COMPANY_LOCATION", mValues.getOffers().get(0).getOfferLocation());
                intent.putExtra("COMPANY_PHONE_NUMBER", mValues.getPhoneNumber());
                intent.putExtra("OFFER_END_TYPE", mValues.getOffers().get(0).getOfferEndType());
                intent.putExtra("COMPANY_ID", mValues.getOffers().get(0).getCompanyId());
                intent.putExtra("COMPANY_TAG_ID", mValues.getOffers().get(0).getCompanyTagId());
                intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues.getCompanyProfilePhoto());
                intent.putExtra("OFFER_TIME_END", mValues.getOffers().get(0).getOfferTimeEnd());
                if (!isTimelineHeaderViewed(String.valueOf(mValues.getOffers().get(0).getOfferId()))) {
                    putTimelineHeaderViewed(String.valueOf(mValues.getOffers().get(0).getOfferId()));
                }
                sdvCompanyIcon.setBackgroundResource(R.mipmap.iv_company_details_brochure_viewed);
                this.startActivity(intent);
            }
        });

        ll_company_details_time.setOnClickListener(view -> {
            if (rl_company_details_hours.getVisibility() == View.VISIBLE) {
                rl_company_details_hours.setVisibility(View.GONE);
                iv_company_details_hours_arrow.setRotation(0);
            } else {
                rl_company_details_hours.setVisibility(View.VISIBLE);
                iv_company_details_hours_arrow.setRotation(180);
                reportStatisticsWorkingHours(mValues.getCompanyId());
            }
        });

        iv_company_details_notifications_no_offer.setOnClickListener(view -> {
            if (mValues.getNotifications()) {
                Utility.diableCompanyNotificationsDialog(this, getString(R.string.are_you_sure_you_want_to_un_subscribe_notifications), getString(R.string.yes_un_subscribe_notifications), getString(R.string.cancel), unSubscribe, cancelUnSubscribe);
            } else {
                subscribeForNotification(mValues.getCompanyId());
                iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_subscribed_no_offer);
//                iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
            }
        });

        chk_followed.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                    WarningDialog();
                    chk_followed.setChecked(false);
                } else {
                    Subscribe(mValues.getCompanyId());
                }
            } else {
                UnSubscribe(mValues.getCompanyId());
            }
        });

        if (companyLocation != null) ll_company_details_action_button_location.setVisibility(View.VISIBLE);
        ll_company_details_action_button_location.setOnClickListener(view -> {
            reportStatisticsLocation(mValues.getCompanyId());
                    Utility.initiateGoogleMapsIntent(this, companyLocation);
        });

        ll_company_details_action_button_mail.setOnClickListener(view -> {
            reportStatisticsEmail(mValues.getCompanyId());
                    Utility.initiateEmailIntent(this, mValues.getEmail(), "", "Sent from Feeedz");
        });

        ll_company_details_action_button_web.setOnClickListener(view -> Utility.initiateURLIntent(this, mValues.getWebSite()));

        if (companyPhoneNumber != null) ll_company_details_action_button_call.setVisibility(View.VISIBLE);
        ll_company_details_action_button_call.setOnClickListener(view -> {
            reportStatisticsCompanyCall(mValues.getCompanyId());
            Utility.initiateCallIntent(this, mValues.getPhoneNumber());
        });

        iv_company_details_similar_companies_arrow_button.setOnClickListener(view -> {
            if (rl_company_details_similar_companies.getVisibility() == View.VISIBLE) {
                rl_company_details_similar_companies.setVisibility(View.GONE);
                iv_company_details_similar_companies_arrow_button.setRotation(0);
            } else {
                rl_company_details_similar_companies.setVisibility(View.VISIBLE);
                iv_company_details_similar_companies_arrow_button.setRotation(180);
                if (rv_company_details_similar_companies.getAdapter() == null) {
                    Call<GetSimilarCompaniesResponse[]> callSimilarCompaniesResponse = git.getSimilarCompanies(mValues.getCompanyId());
                    callSimilarCompaniesResponse.enqueue(new Callback<GetSimilarCompaniesResponse[]>() {

                        @Override
                        public void onResponse(Call<GetSimilarCompaniesResponse[]> call, Response<GetSimilarCompaniesResponse[]> response) {
                            if (response.isSuccessful() && response.body().length > 0) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ViewCompanyDetailsActivity.this, LinearLayoutManager.HORIZONTAL, true);
                                layoutManager.setReverseLayout(true);
                                rv_company_details_similar_companies.setLayoutManager(layoutManager);
                                rv_company_details_similar_companies.setAdapterWithProgress(new CompanyDetailsSimilarCompaniesHorizontalViewAdapter(response.body(), ViewCompanyDetailsActivity.this));
                                rl_company_details_similar_companies.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            }
                        }

                        @Override
                        public void onFailure(Call<GetSimilarCompaniesResponse[]> call, Throwable t) {

                        }
                    });
                }
            }
        });
//
//
//        chk_followed = findViewById(R.id.chk_followed);
//        sdv_compny_category = findViewById(R.id.sdv_compny_category);
//        sdv_compny_icon = findViewById(R.id.sdv_compny_icon);
//        sdv_add_iamge_header= findViewById(R.id.sdv_add_iamge_header);
//
//        txt_advrt_title = findViewById(R.id.txt_advrt_title);
//        txt_compny_tag = findViewById(R.id.txt_compny_tag);
//        txt_compny_advrt_title = findViewById(R.id.txt_compny_advrt_title);
//        txt_compny_website = findViewById(R.id.txt_compny_website);
//
//        txt_offer = findViewById(R.id.txt_offer);
//        txt_subscribe = findViewById(R.id.txt_subscribe);
//
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

//        btnBack.setOnClickListener(v -> onBackPressed());


        btnBack.setOnClickListener(v -> {
                onBackButtonPressToHome();

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewCompanyDetailsActivity.this));

        Call<GetCompnayDetailResponse> callGetCompanyDetailsResponse = git.getCompanyDetails(companyID);
        callGetCompanyDetailsResponse.enqueue(new Callback<GetCompnayDetailResponse>() {
            @Override
            public void onResponse(Call<GetCompnayDetailResponse> call, Response<GetCompnayDetailResponse> response) {
                if (response.isSuccessful()) {
                    mValues = response.body();

                    Log.e("COMPANY_ID", "" + mValues.getCompanyId());

                    if (mValues.getOffers().size() > 0) {
                        if (mValues.getOffers().get(0).getFeatureType() == 4) {
                            Log.e("featureType", "" + mValues.getOffers().get(0).getFeatureType());
                            if (isTimelineHeaderViewed(String.valueOf(mValues.getOffers().get(0).getOfferId()))) {
                                sdvCompanyIcon.setBackgroundResource(R.mipmap.iv_company_details_brochure_viewed);
                            } else {
                                sdvCompanyIcon.setBackgroundResource(R.mipmap.iv_company_details_brochure_un_viewed);
                            }
                        }

                        recyclerView.setAdapterWithProgress(new CompnayDetailAdapter(mValues, ViewCompanyDetailsActivity.this));
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        ll_company_details_empty.setVisibility(View.VISIBLE);
                    }

                    if (mValues.getNotifications()) {
                        iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_on);
                        iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
                        iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_subscribed_no_offer);

                    } else {
                        iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_off);
                        iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_disabled);
                        iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_un_subscribed_no_offer);
                    }

                    iv_company_details_notification_icon.setVisibility(View.VISIBLE);

                    // //cast holder to VHHeader and set data for header.
                    sdvImageHeader.setImageURI(Uri.parse(mValues.getCompanyHeaderPhoto()));
                    sdvCompanyIcon.setImageURI(Uri.parse(mValues.getCompanyProfilePhoto()));

                    txt_advrt_title.setText(mValues.getCompanyName_Ar());
                    txt_compny_tag.setText(mValues.getCompanyName());

                    companyWebsite = mValues.getWebSite();
                    if (companyWebsite != null) {
                        ll_company_details_action_button_web.setVisibility(View.VISIBLE);
                    }

                    companyEmail = mValues.getEmail();
                    if (companyEmail != null) {
                        ll_company_details_action_button_mail.setVisibility(View.VISIBLE);
                    }

                    companyPhoneNumber = mValues.getPhoneNumber();
                    if (companyPhoneNumber != null) {
                        ll_company_details_action_button_call.setVisibility(View.VISIBLE);
                    }

//                    companyLocation = mValues.getCompa();
//                    if (companyLocation != null) {
//                        ll_company_details_action_button_location.setVisibility(View.VISIBLE);
//                    }

//            sdv_compny_category.setImageURI(setCopmnyImage(mValues.getCompanyTags().get(0).getTagId()));
//            sdv_compny_category.setOnClickListener(v -> {
//                Intent intent = new Intent(ViewCompanyDetailsActivity.this, CategoryTabActivity.class);
//                intent.putExtra("message", mValues.getCompanyTags().get(0).getTagId());
//                intent.putExtra("title", Utility.getCategoryTitleByID(mValues.getCompanyTags().get(0).getTagId()));
//                ViewCompanyDetailsActivity.this.startActivity(intent);
//            });

                    if (mValues.isIsFollowed()) {
                        chk_followed.setChecked(true);
                        iv_company_details_similar_companies_arrow_button.setImageResource(R.mipmap.ic_company_details_arrow_button_followed);
                    } else {
                        chk_followed.setChecked(false);
                        iv_company_details_similar_companies_arrow_button.setImageResource(R.mipmap.ic_company_details_arrow_button_un_followed);
                    }
                    txt_compny_advrt_title.setText(mValues.getCompanyDescription());
                    if (mValues.getNumberOfOffers() > 0) {
                        tv_number_of_offers.setText(mValues.getNumberOfOffers() + " " + getString(R.string.offers));
                        tv_number_of_offers.setVisibility(View.VISIBLE);
                    }

                    if (mValues.getIsOpen() != null) {
                        if (mValues.getIsOpen()) {
                            if (mValues.getWorkingHours().size() > 0) {
                                tv_company_details_is_open.setText(getString(R.string.open_now));
                                tv_company_details_is_open.setVisibility(View.VISIBLE);
                            }
                            tv_company_details_is_open.setTextColor(getResources().getColor(R.color.company_details_open_now));
                            ll_company_details_time.setVisibility(View.VISIBLE);
                        } else {
                            if (mValues.getWorkingHours().size() > 0) {
                                tv_company_details_is_open.setText(getString(R.string.closed_now));
                                tv_company_details_is_open.setVisibility(View.VISIBLE);
                            }
                            tv_company_details_is_open.setTextColor(getResources().getColor(R.color.company_details_closed_now));
                            ll_company_details_time.setVisibility(View.VISIBLE);
                        }
                    }

                    if (mValues.getWorkingHoursStatus() != null) {
                        tv_company_details_working_hours_status.setText(mValues.getWorkingHoursStatus());
                        tv_company_details_working_hours_status.setVisibility(View.VISIBLE);
                    }

                    Log.e("WorkingHours", "" + mValues.getWorkingHours());

                    if (mValues.getWorkingHours().size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mValues.getWorkingHours().size(); i++) {
                            sb.append(mValues.getWorkingHours().get(i));
                            if (i < mValues.getWorkingHours().size() - 1) {
                                sb.append("\n");
                            }
                        }
                        tv_company_details_working_hours.setText(sb);
                        iv_company_details_hours_arrow.setVisibility(View.VISIBLE);
                    } else {
                        ll_company_details_time.setClickable(false);
                    }

//            txt_subscribe.setText(mValues.getNumberOfSubscribers() + "");
                }
            }

            @Override
            public void onFailure(Call<GetCompnayDetailResponse> call, Throwable t) {

            }
        });
    }

    private void onBackButtonPressToHome() {
        if (getIntent().hasExtra(Constant.FROM_PUSH_NOTIFICATION)){
            startActivity(new Intent(ViewCompanyDetailsActivity.this, MainActivity.class));
        }else{
            finish();
        }
    }

    private void WarningDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_login_warning);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(v -> {
            finalDialog.dismiss();
            //startActivity(new Intent(ViewOfferActivity.this, MainActivity.class));
            //finish();
        });
        btn_cnl.setOnClickListener(v -> {
            finalDialog.dismiss();
            ViewCompanyDetailsActivity.this.startActivity(new Intent(ViewCompanyDetailsActivity.this, SplashActivity.class));
            //mContext.finish();
            finalDialog.dismiss();
        });
        dialog.show();
    }

    private void notificationsEnabledConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_company_notifications_enabled);
        dialog.show();

        new Handler().postDelayed(dialog::dismiss, 1500);
    }

    public void Subscribe(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> callSubscribeCompany = git.Subscribe(String.valueOf(companyID));
        callSubscribeCompany.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("onResponseSUb", "");
                Log.e("callSUB", "" + response.raw().request().url());
                if (response.isSuccessful()) {
                    Log.e("onResponseSUb", "isSuccess");
                    editor.remove(Constant.FOLLOWING).commit();
                    mValues.setIsFollowed(true);
                    mValues.setNumberOfSubscribers(mValues.getNumberOfSubscribers() + 1);
                    iv_company_details_similar_companies_arrow_button.setImageResource(R.mipmap.ic_company_details_arrow_button_followed);

//                    notifyDataSetChanged();
                } else {
                    Log.e("failedSub", "" + response.message() + " " + response.code() + " " );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("failureSubs", "");
            }
        });
    }

    public void UnSubscribe(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> callUnSubscribeCompany = git.UnSubscribe(String.valueOf(companyID));
        callUnSubscribeCompany.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    editor.remove(Constant.FOLLOWING).commit();
                    mValues.setIsFollowed(false);
                    mValues.setNumberOfSubscribers(mValues.getNumberOfSubscribers() - 1);
                    iv_company_details_similar_companies_arrow_button.setImageResource(R.mipmap.ic_company_details_arrow_button_un_followed);
                    if (rl_company_details_similar_companies.getVisibility() == View.VISIBLE) {
                        iv_company_details_similar_companies_arrow_button.setRotation(180);
                    }
//                    notifyDataSetChanged();
                    Log.i("unSub", "Successful" + " " + response.message());
                } else {
                    Log.e("failedUnSub", "" + response.message() + " " + response.code() + " " );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void subscribeForNotification(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> subscribeToCompanyNotifications = git.subscribeToCompanyNotifications(obj);
        subscribeToCompanyNotifications.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    mValues.setNotifications(true);
                    iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
                    iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_on);
                    iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_subscribed_no_offer);
                    notificationsEnabledConfirmationDialog();
                    if (response.body() != null) {
                        Log.i("subForNotification", "" + response.body());
                    }
                } else {
                    iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_disabled);
                    iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_off);
                    iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_un_subscribed_no_offer);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("subForNotification", "FAILED");
                iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_disabled);
                iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_off);
                iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_un_subscribed_no_offer);
            }
        });
    }

    public void unSubscribeForNotification(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> unsubscribeFromCompanyNotifications = git.unsubscribeFromCompanyNotifications(obj);
        unsubscribeFromCompanyNotifications.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    mValues.setNotifications(false);
                    iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_off);
                    iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_disabled);
                    iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_un_subscribed_no_offer);
                    if (response.body() != null) {
                        Log.i("unSubForNotification", "" + response.body());
                    }
                } else {
                    iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
                    iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_on);
                    iv_company_details_notifications_no_offer.setImageResource(R.mipmap.iv_company_details_notifications_subscribed_no_offer);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("unSubForNotification", "FAILED");
                iv_company_details_notification_icon.setImageResource(R.mipmap.ic_company_details_notifications_enabled);
                iv_company_details_notification.setImageResource(R.mipmap.ic_company_profile_notification_on);
            }
        });
    }

    private boolean isTimelineHeaderViewed(String offerID) {
        String dataFromSharedPreferences = sPref.getString(Constant.TIMELINE_HEADERS_VIEWED, "");
        if (dataFromSharedPreferences.equalsIgnoreCase("")) {
            return false;
        }
        String[] mainArrayFromPreferences = dataFromSharedPreferences.split(",");
        for (String mainArrayFromPreference : mainArrayFromPreferences) {
            String[] childArrayFromPreferences = mainArrayFromPreference.split(":");
            if (childArrayFromPreferences[0].equalsIgnoreCase(offerID) && Boolean.parseBoolean(childArrayFromPreferences[1])) {
                return true;
            }
        }
        return false;
    }

    private void putTimelineHeaderViewed(String offerID) {
        String dataFromSharedPreferences = sPref.getString(Constant.TIMELINE_HEADERS_VIEWED, "");
        dataFromSharedPreferences = dataFromSharedPreferences + offerID + ":true,";
        editor.putString(Constant.TIMELINE_HEADERS_VIEWED, dataFromSharedPreferences).apply();
    }



    private void reportStatisticsCompanyShare(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> callReportShare = git.reportStatisticsCompanyShare(obj);
        callReportShare.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportShare", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportShare", "FAILED");

            }
        });
    }

    private void reportStatisticsCompanyCall(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> callReportCall = git.reportStatisticsCompanyCall(obj);
        callReportCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportCall", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportCall", "FAILED");
            }
        });
    }

    private void reportStatisticsLocation(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> callReportLocation = git.reportStatisticsCompanyLocation(obj);
        callReportLocation.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportLocation", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportLocation", "FAILED");

            }
        });
    }

    private void reportStatisticsWorkingHours(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> callReportWorkingHours = git.reportStatisticsCompanyWorkingHours(obj);
        callReportWorkingHours.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportWorkingHours", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportWorkingHours", "FAILED");

            }
        });
    }

    private void reportStatisticsEmail(int companyID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<JsonObject> callReportEmail = git.reportStatisticsCompanyEmail(obj);
        callReportEmail.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("reportEmail", "" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("reportEmail", "FAILED");

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onBackButtonPressToHome();
    }
}