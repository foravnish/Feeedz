package com.socialinfotech.feeedj.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.HeightWrappingViewPager;
import com.socialinfotech.feeedj.AppUtils.TextViewPlus;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ApplicationActivities.Home;
import com.socialinfotech.feeedj.ParsingModel.GetSearchResponse;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.PagerCategory;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class CategoriesFeedSearchRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesFeedSearchRecyclerViewAdapter.ViewHolder> {

    private final GetSearchResponse[] mValues;
    private List<ViewHolder> lstHolders;
    boolean isRatingLayoutVisible;
    private Handler mHandler = new Handler();
    Context mContext;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    PagerCategory pagerAdapter;

    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    int feedInFocusID;
    Handler offerViewedHandler = new Handler();
    Runnable offerViewedTaskRunnable = new Runnable() {
        @Override
        public void run() {
//            new OfferReportingTask(new int[]{0, feedInFocusID}).execute();
            Home.reportOfferViewed(feedInFocusID);
        }
    };

    public CategoriesFeedSearchRecyclerViewAdapter(GetSearchResponse[] getAllOffersResponces, Context cnt) {
        mValues = getAllOffersResponces;
        Log.i("search", "offerResponses" + getAllOffersResponces);
        mContext=cnt;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
        sPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sPref.edit();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_search_bottom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues[position];
        holder.txt_compnay_name.setText(mValues[position].getCompany().getCompanyName_Ar());
        holder.txt_compny_username.setText(mValues[position].getCompany().getCompanyName());

        holder.txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorMidPurple));
        holder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        holder.txt_add_title.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL);
        holder.txt_add_title.setAutoLinkText(mValues[position].getOfferTitle());

//        holder.txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

        holder.txt_add_title.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (autoLinkMode.equals(AutoLinkMode.MODE_HASHTAG)) {
                    Utility.search(mContext, matchedText.trim());
                } else if (autoLinkMode.equals(AutoLinkMode.MODE_URL)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(matchedText.trim()));
                    mContext.startActivity(browserIntent);
                }
            }
        });

//        holder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        Typeface typeface_txt_add_title = Typeface.createFromAsset(mContext.getAssets(), "demibold.ttf");
        holder.txt_add_title.setTypeface(typeface_txt_add_title);

        holder.sdvImage.setImageURI(Uri.parse(mValues[position].getCompany().getCompanyProfilePhoto()));
       // holder.sdv_add_iamge.setImageURI(Uri.parse(mValues[position].getOfferImage()));

        pagerAdapter = new PagerCategory(Arrays.asList(mValues), mContext,mValues[position].getOfferImages(),mValues[position].getOfferImage());
        holder.view_pager.setAdapter(pagerAdapter);
        //headerHolder.view_pager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (mValues[position].getMultiple()) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setViewPager(holder.view_pager);
        }
        else{
            holder.indicator.setVisibility(View.GONE);
        }

        if (mValues[position].getOfferImageCoord() != null) {
            String[] sImageDimensions = mValues[position].getOfferImageCoord().split("x");
            if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
              //  holder.sdv_add_iamge.setAspectRatio(1);
            } else {
             //   holder.sdv_add_iamge.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
            }
        }

//        holder.txt_date.setText(Utility.dateConvert(mValues[position].getOfferTimeEnd()));
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());
        holder.sdv_cateogory.setImageURI(setCopmnyImage(mValues[position].getCompanyTagId()));
//        holder.sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(mContext, ImageViewActivity.class);
////                intent.putExtra(Constant.ImageNAme,mValues[position].getOfferImage());
////                intent.putExtra(Constant.TextTile,mValues[position].getOfferTitle());
////                mContext.startActivity(intent);
//                String str = mValues[position].getAttachmentHTML();
//                if (str==null) {
//                    Intent intent = new Intent(mContext, ImageViewActivity.class);
//                    intent.putExtra(Constant.ImageNAme, mValues[position].getOfferImage());
//                    intent.putExtra(Constant.TextTile, mValues[position].getOfferTitle());
//                    intent.putExtra(Constant.OfferLocation, mValues[position].getOfferLocation());
//                    intent.putExtra(Constant.PhoneNumber, mValues[position].getPhoneNumber());
//                    intent.putExtra(Constant.OfferID, mValues[position].getOfferId());
//                    intent.putExtra(Constant.OFFER_RATING_STATUS, mValues[position].getOfferRating());
//
//                    int[] screenLocation = new int[2];
//                    holder.sdv_add_iamge.getLocationOnScreen(screenLocation);
//
//                    intent.putExtra("left", screenLocation[0]).
//                            putExtra("top", screenLocation[1]).
//                            putExtra("width", holder.sdv_add_iamge.getWidth()).
//                            putExtra("height", holder.sdv_add_iamge.getHeight());
//                    mContext.startActivity(intent);
//
//                }else {
//                    Intent intent = new Intent(mContext, PDFViewActivity.class);
//                    intent.putExtra("OFFER_ID", mValues[position].getOfferId());
//                    intent.putExtra("OFFER_IMAGE", mValues[position].getOfferImage());
//                    intent.putExtra("BROCHURE_PDF_URL", mValues[position].getAttachmentHTML());
//                    intent.putExtra("BROCHURE_PDF_TITLE", mValues[position].getCompany().getCompanyName_Ar());
//                    intent.putExtra("COMPANY_NAME", mValues[position].getCompany().getCompanyName_Ar());
//                    intent.putExtra("COMPANY_USER_NAME", mValues[position].getCompany().getCompanyName());
//                    intent.putExtra("COMPANY_VERIFIED", mValues[position].isCompanyVerified());
//                    intent.putExtra("COMPANY_LOCATION", mValues[position].getOfferLocation());
//                    intent.putExtra("COMPANY_PHONE_NUMBER", mValues[position].getPhoneNumber());
//                    intent.putExtra("OFFER_END_TYPE", mValues[position].getOfferEndType());
//                    intent.putExtra("COMPANY_ID", mValues[position].getCompanyId());
//                    intent.putExtra("COMPANY_TAG_ID", mValues[position].getCompanyTagId());
//                    intent.putExtra("COMPANY_PROFILE_PHOTO", mValues[position].getCompany().getCompanyProfilePhoto());
//                    intent.putExtra("OFFER_TIME_END", mValues[position].getOfferTimeEnd());
//                    mContext.startActivity(intent);
//                }
//            }
//        });

        if (mValues[position].isCompanyVerified()) {
            holder.iv_raw_company_verified.setVisibility(View.VISIBLE);
        } else {
            holder.iv_raw_company_verified.setVisibility(View.GONE);
        }

        int featureType = mValues[position].getFeatureType();
        boolean isFeatured = mValues[position].isFeatured();

        if (featureType > 1 || isFeatured) {
            if (featureType == 3) {
                holder.img_sponser.setVisibility(View.GONE);
                holder.iv_raw_offers_brochure.setVisibility(View.GONE);
            } else if (featureType == 4) {
                holder.iv_raw_offers_brochure.setVisibility(View.VISIBLE);
                holder.img_sponser.setVisibility(View.GONE);
            } else if (featureType == 2 || isFeatured) {
                holder.img_sponser.setVisibility(View.VISIBLE);
                holder.iv_raw_offers_brochure.setVisibility(View.GONE);
            }
        } else {
            holder.img_sponser.setVisibility(View.GONE);
            holder.iv_raw_offers_brochure.setVisibility(View.GONE);
        }

        String offerEndType = mValues[position].getOfferEndType().trim();

        if (offerEndType.equalsIgnoreCase("Timer")) {
            holder.llRawOffersTimer.setVisibility(View.VISIBLE);
            holder.llRawOffersEndType.setVisibility(View.GONE);
        } else {
            holder.llRawOffersTimer.setVisibility(View.GONE);
            holder.llRawOffersEndType.setVisibility(View.VISIBLE);

            if (offerEndType.equalsIgnoreCase("LimitedQuantity")) {
                holder.tvRawOffersEndType.setText("العرض متوفر حتى انتهاء الكمية");
            } else if (offerEndType.equalsIgnoreCase("LimitedTime")) {
                holder.tvRawOffersEndType.setText("العرض متوفر لمدة محدودة");
            }
        }

        holder.sdv_cateogory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CategoryTabActivity.class);
//                intent.putExtra("message", mValues[position].getCompanyTagId());
//                intent.putExtra("title", Utility.getCategoryTitleByID(mValues[position].getCompanyId()));
//                mContext.startActivity(intent);
            }
        });
        holder.btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                intent.putExtra(Constant.ToolbarTitle,mValues[position].getCompany().getCompanyName_Ar());
                intent.putExtra(Constant.COMPANY_ID,mValues[position].getCompanyId());

                intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position].getCompany().getCompanyProfilePhoto());
                intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position].getCompany().getCompanyName_Ar());
                intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position].getCompany().getCompanyName());
                intent.putExtra(Constant.COMPANY_PROFILE_VERIFIED, mValues[position].isCompanyVerified());
                intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, mValues[position].getOfferLocation());
                intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues[position].getPhoneNumber());
                mContext.startActivity(intent);
            }
        });
        holder.main_ll_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                intent.putExtra(Constant.SINGLEOFFERID, mValues[position].getOfferId());
//                mContext.startActivity(intent);
            }
        });
        holder.txt_add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                intent.putExtra(Constant.SINGLEOFFERID, mValues[position].getOfferId());
//                mContext.startActivity(intent);
            }
        });

//        if (hashMapRating.containsKey(mValues[position].getOfferId())) {
//            holder.rBarRawOffersRating.setRating(hashMapRating.get(mValues[position].getOfferId()));
//            holder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
//        } else {
            int ratingFromApi = mValues[position].getOfferRating();
            Log.e("rating", "" + ratingFromApi);
            holder.rBarRawOffersRating.setRating(ratingFromApi);
            if (ratingFromApi > 0) {
                holder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
            } else {
                holder.ivRawOffersRate.setImageResource(R.mipmap.ic_timeline_feed_rate);
            }
//        }

        holder.rBarRawOffersRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            final int ratingValue = Math.round(v);
            if (ratingValue > 0 && b) {
                ratingBar.setRating(ratingValue);
                holder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                isRatingLayoutVisible = false;
                if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                    Utility.WarningDialog(mContext);
                    holder.rBarRawOffersRating.setRating(0);
                } else {
                    mValues[position].setOfferRating(ratingValue);
                    holder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                    Home.reportOfferLikes(mValues[position].getOfferId(), ratingValue);
//                        new OfferRatingTask(new String[]{String.valueOf(feedInFocusID), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
                    new Handler().postDelayed(() -> {
                        if (ratingValue < 3) {
                            Utility.ShowDislikeDialog(mContext);
                        } else {
                            if (sPref.getBoolean(Constant.LIKE_DIALOG, true)) {
                                Utility.ShowlikeDialog(mContext);
                                sPref.edit().putBoolean(Constant.LIKE_DIALOG, false).apply();
                            }

                        }
                    }, 250);
                }
            }
        });

        if (isRatingLayoutVisible) {
            holder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
            isRatingLayoutVisible = false;
        }

        holder.ll_raw_offers_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRatingLayoutVisible) {
                    holder.ll_raw_offers_rating_bar.setVisibility(View.VISIBLE);
                    isRatingLayoutVisible = true;
                } else {
                    holder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                }
            }
        });

        holder.ll_raw_offers_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mValues[position].getOfferLocation() != null) {
                    Utility.initiateGoogleMapsIntent(mContext, mValues[position].getOfferLocation());
//                    new OfferReportingTask(new int[]{3, mValues[position].getOfferId()}).execute();
                    Home.reportOfferLocation(mValues[position].getOfferId());
                }  else {
                    Toast.makeText(mContext, mContext.getString(R.string.LocationIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ll_raw_offers_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mValues[position].getPhoneNumber() != null) {
                    Utility.initiateCallIntent(mContext, mValues[position].getPhoneNumber());
//                    new OfferReportingTask(new int[]{2, mValues[position].getOfferId()}).execute();
                    Home.reportOfferCalled(mValues[position].getOfferId());
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.PhoneNumberIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.iv_raw_offers_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.shareImage(mValues[position].getOfferTitle() ,mValues[position].getOfferImage(), mContext);
                Home.reportOfferShared(mValues[position].getOfferId());
            }
        });

//        holder.rb_dislike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")){
//                    WarningDialog();
//                    holder.rb_dislike.setChecked(false);
//                }else{
//                ShowDislikeDialog();
//                }
//            }
//        });
//        holder.rb_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")){
//                        WarningDialog();
//                        holder.rb_like.setChecked(false);
//                    }else{
//                        if (sPref.getBoolean(Constant.LIKEDILAOG,true)){
//                            ShowlikeDialog();
//                            editor.putBoolean(Constant.LIKEDILAOG,false).apply();
//                        }
//                    }
//            }
//        });

        feedInFocusID = mValues[position].getOfferId();
        offerViewedHandler.removeCallbacks(offerViewedTaskRunnable);
        offerViewedHandler.postDelayed(offerViewedTaskRunnable, 1000);
    }

    //    private void WarningDialog() {
//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.dialog_login_warning);
//
//        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
//        Button btn_cnl = (Button) dialog.findViewById(R.id.btn_cnl);
//        final Dialog finalDialog = dialog;
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finalDialog.dismiss();
//                //startActivity(new Intent(ViewOfferActivity.this, MainActivity.class));
//                //finish();
//            }
//        });
//        btn_cnl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finalDialog.dismiss();
//                mContext.startActivity(new Intent(mContext, SplashActivity.class));
//                //mContext.finish();
//                finalDialog.dismiss();
//            }
//        });
//
//
//        dialog.show();
//    }
//    private void ShowDislikeDialog() {
//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.dialog_dislike);
//        TextView txt_road = (TextView) dialog.findViewById(R.id.txt);
//        final Dialog finalDialog = dialog;
//        txt_road.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finalDialog.dismiss();
//            }
//        });
//
//
//        dialog.show();
//    }
//    private void ShowlikeDialog() {
//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.dialog_like);
//        TextView txt_road = (TextView) dialog.findViewById(R.id.txt);
//        final Dialog finalDialog = dialog;
//        txt_road.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finalDialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    private Uri setCopmnyImage(int companyId) {
        String path = "res:/" + R.drawable.tweleve_icon;
        if (companyId==1){
            path = "res:/" + R.drawable.one_industry_icon;

        }else if (companyId==2){
            path = "res:/" + R.drawable.two_airlinesicon;
        }else if (companyId==3){
            path = "res:/" + R.drawable.three_bank_icon;
        }else if (companyId==4){
            path = "res:/" + R.drawable.four_icon_blue;
        }else if (companyId==5){
            path = "res:/" + R.drawable.five_travel_icon;
        }else if (companyId==6){
            path = "res:/" + R.drawable.six_cars_icon;
        }else if (companyId==7){
            path = "res:/" + R.drawable.seven_restaurants_icon;
        }else if (companyId==8){
            path = "res:/" + R.drawable.eight_shopping_icon;
        }else if (companyId==9){
            path = "res:/" + R.drawable.nine_furniture_icon;
        }else if (companyId==10){
            path = "res:/" + R.drawable.ten_clothes_icon;
        }else if (companyId==11){
            path = "res:/" + R.drawable.eleven_health_icon;
        }else if (companyId==12){
            path = "res:/" + R.drawable.tweleve_icon;
        }
        return Uri.parse(path);
    }
    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compny_username;
        public final TextView txt_compnay_name;
        public final SimpleDraweeView sdvImage;
        AutoLinkTextView txt_add_title;
        SimpleDraweeView sdv_cateogory;
        SimpleDraweeView sdv_add_iamge;
        HeightWrappingViewPager view_pager;
        CircleIndicator indicator;
        //        TextView txt_date;
        TextView txt_days;
        TextView txt_hrs;
        TextView txt_scnd;
        TextView txt_minut;
        RadioButton rb_dislike;
        RelativeLayout btn_navigate;
        ImageView img_sponser;
        RadioButton rb_like;
        public GetSearchResponse mItem;
        LinearLayout main_ll_timer;

        LinearLayout ll_raw_offers_rating_bar;
        RatingBar rBarRawOffersRating;
        LinearLayout ll_raw_offers_rate;
        ImageView ivRawOffersRate;
        LinearLayout ll_raw_offers_location;
        LinearLayout ll_raw_offers_call;
        LinearLayout llRawOffersTimer;
        LinearLayout llRawOffersEndType;
        TextViewPlus tvRawOffersEndType;

        ImageView iv_raw_offers_share;
        ImageView iv_raw_offers_brochure;
        ImageView iv_raw_company_verified;
        RelativeLayout main_rl;


        public void updateTimeRemaining(long currentTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(mItem.getOfferTimeEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long startDate = date.getTime();

            long timeDiff = startDate - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int dats =  (int) ((timeDiff / (1000 * 60 * 60)) /24);
                txt_days.setText(dats+"");
                txt_scnd.setText(seconds+"");
                txt_minut.setText(minutes+"");
                txt_hrs.setText(hours+"");

            }
        }
        public ViewHolder(View view) {
            super(view);
            mView = view;
            txt_compny_username = view.findViewById(R.id.txt_compny_username);
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            txt_add_title = view.findViewById(R.id.txt_add_title);
//            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_days = view.findViewById(R.id.txt_days);
            txt_hrs = view.findViewById(R.id.txt_hrs);
            img_sponser = view.findViewById(R.id.img_sponser);
            btn_navigate = view.findViewById(R.id.btn_navigate);
            txt_scnd = view.findViewById(R.id.txt_scnd);
            main_rl= view.findViewById(R.id.main_rl);
            main_ll_timer = view.findViewById(R.id.ll_raw_offers_timer_and_buttons_layout);
            txt_minut = view.findViewById(R.id.txt_minut);
            sdvImage = view.findViewById(R.id.sdvImage);
            sdv_cateogory = view.findViewById(R.id.sdv_cateogory);
//            sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);
            view_pager = view.findViewById(R.id.view_pager);
            indicator = view.findViewById(R.id.indicator);

            ll_raw_offers_rating_bar = view.findViewById(R.id.ll_timeline_rating);
            rBarRawOffersRating = view.findViewById(R.id.r_bar_timline_rating);
            ll_raw_offers_rate = view.findViewById(R.id.ll_raw_offers_rate);
            ivRawOffersRate = view.findViewById(R.id.iv_raw_search_offer_rate);
            ll_raw_offers_location = view.findViewById(R.id.ll_raw_offers_location);
            ll_raw_offers_call = view.findViewById(R.id.ll_raw_offers_call);
            iv_raw_offers_share = view.findViewById(R.id.iv_raw_offers_share);
            iv_raw_offers_brochure = view.findViewById(R.id.iv_raw_offers_brochure);
            iv_raw_company_verified = view.findViewById(R.id.iv_raw_search_offer_verified);
            llRawOffersTimer = view.findViewById(R.id.ll_raw_offers_timer);
            llRawOffersEndType = view.findViewById(R.id.ll_raw_feed_offer_end_type);
//            rb_dislike = (RadioButton)view.findViewById(R.id.rb_dislike);
//            rb_like = (RadioButton)view.findViewById(R.id.rb_like);
            tvRawOffersEndType = view.findViewById(R.id.tv_raw_feed_offer_end_type);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
