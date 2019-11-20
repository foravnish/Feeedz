package com.socialinfotech.feeedj.FollowingActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ExploreActivities.CategoryTabActivity;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.Pager;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class FollowingViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final GetAllOffersResponse[] mValues;
    private List<HeaderViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    Activity mContext;
    boolean isRatingLayoutVisible;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    HashMap<Integer, Integer> hashMapRating;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    Pager pagerAdapter;

    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (HeaderViewHolder holder : lstHolders) {
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
            Home.reportOfferViewed(feedInFocusID);
//            new OfferReportingTask(new int[]{0, feedInFocusID}).execute();
        }
    };

    public FollowingViewAdapter(GetAllOffersResponse[] getAllOffersResponses, Activity cnt) {
        mContext = cnt;
        mValues = getAllOffersResponses;
        hashMapRating = new HashMap<>();
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_following_item, parent, false);
            return new HeaderViewHolder(view);
        }
        else if(viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.footer_item, parent, false);
            return new FooterViewHolder (v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.mItem = mValues[position];
            headerHolder.txt_compnay_name.setText(mValues[position].getCompany().getCompanyName_Ar());
            headerHolder.txt_compny_username.setText(mValues[position].getCompany().getCompanyName());

            headerHolder.txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorMidPurple));
            headerHolder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            headerHolder.txt_add_title.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL);
            headerHolder.txt_add_title.setAutoLinkText(mValues[position].getOfferTitle());

            headerHolder.txt_add_title.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
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

//            headerHolder.txt_add_title.setUrlModeColor(ContextCompat.getColor(mContext, R.color.facebook_messenger_blue));

            Typeface typeface_txt_add_title = Typeface.createFromAsset(mContext.getAssets(), "demibold.ttf");
            headerHolder.txt_add_title.setTypeface(typeface_txt_add_title);

            headerHolder.sdvImage.setImageURI(Uri.parse(mValues[position].getCompany().getCompanyProfilePhoto()));

            if (mValues[position].getMultiple()) {
                pagerAdapter = new Pager(Arrays.asList(mValues), mContext,mValues[position].getOfferImages(),mValues[position].getOfferImage(),mValues[position].getAttachmentHTML(),mValues[position].getCompany(),true,mValues[position].getOfferImageCoord());
            }else{
                pagerAdapter = new Pager(Arrays.asList(mValues), mContext,mValues[position].getOfferImages(),mValues[position].getOfferImage(),mValues[position].getAttachmentHTML(),mValues[position].getCompany(),false,mValues[position].getOfferImageCoord());
            }
            headerHolder.view_pager.setAdapter(pagerAdapter);

            if (mValues[position].getMultiple()) {
                headerHolder.indicator.setVisibility(View.VISIBLE);
                headerHolder.indicator.setViewPager(headerHolder.view_pager);
            }
            else{
                headerHolder.indicator.setVisibility(View.GONE);
            }

//            headerHolder.sdv_add_iamge.setImageURI(Uri.parse(mValues[position].getOfferImage()));

//            pagerAdapter = new Pager(Arrays.asList(mValues), mContext,mValues[position].getOfferImages());
          //  headerHolder.view_pager.setAdapter(pagerAdapter);
//            if (mValues[position].getOfferImages().size()>1) {
//                headerHolder.indicator.setViewPager(headerHolder.view_pager);
//            }

            if (mValues[position].getOfferImageCoord() != null) {
                String[] sImageDimensions = mValues[position].getOfferImageCoord().split("x");
                if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
                  //  headerHolder.sdv_add_iamge.setAspectRatio(1);
                } else {
                  //  headerHolder.sdv_add_iamge.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
                }
            }
            //headerHolder.sdv_add_iamge.setAspectRatio(1);

            synchronized (lstHolders) {
                lstHolders.add(headerHolder);
            }

            if (mValues[position].getCompany().isCompanyVerified()) {
                headerHolder.iv_raw_company_verified.setVisibility(View.VISIBLE);
            } else {
                headerHolder.iv_raw_company_verified.setVisibility(View.INVISIBLE);
            }

            int featureType = mValues[position].getType();
            boolean isFeatured = mValues[position].isFeatured();

            if (featureType > 1 || isFeatured) {
                if (featureType == 3) {
                    headerHolder.img_sponser.setVisibility(View.GONE);
                    headerHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
                } else if (featureType == 4) {
                    headerHolder.iv_raw_offers_brochure.setVisibility(View.VISIBLE);
                    headerHolder.img_sponser.setVisibility(View.GONE);
                } else if (featureType == 2 || isFeatured) {
                    headerHolder.img_sponser.setVisibility(View.VISIBLE);
                    headerHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
                }
            } else {
                headerHolder.img_sponser.setVisibility(View.GONE);
                headerHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
            }

            String offerEndType = "Timer";

            if (mValues[position].getOfferEndType() != null) {
                offerEndType = mValues[position].getOfferEndType().trim();
            }


            if (offerEndType.equalsIgnoreCase("Timer")) {
                headerHolder.llRawOffersTimer.setVisibility(View.VISIBLE);
                headerHolder.llRawOffersEndType.setVisibility(View.GONE);
            } else {
                headerHolder.llRawOffersTimer.setVisibility(View.GONE);
                headerHolder.llRawOffersEndType.setVisibility(View.VISIBLE);

                if (offerEndType.equalsIgnoreCase("LimitedQuantity")) {
                    headerHolder.tvRawOffersEndType.setText("العرض متوفر حتى انتهاء الكمية");
                } else if (offerEndType.equalsIgnoreCase("LimitedTime")) {
                    headerHolder.tvRawOffersEndType.setText("العرض متوفر لمدة محدودة");
                }
            }

            headerHolder.updateTimeRemaining(System.currentTimeMillis());
            headerHolder.sdv_cateogory.setImageURI(setCopmnyImage(mValues[position].getCompanyTagId()));

            headerHolder.sdv_cateogory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CategoryTabActivity.class);
                    intent.putExtra("message", mValues[position].getCompanyTagId());
                    intent.putExtra("title", Utility.getCategoryTitleByID(mValues[position].getCompanyTagId()));
                    mContext.startActivity(intent);
                }
            });

//            headerHolder.sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Imageclick", mValues[position].getAttachmentHTML()+" ");
//                    String str = mValues[position].getAttachmentHTML();
//                    if (str == null) {
//                        Intent intent = new Intent(mContext, ImageViewActivity.class);
//                        intent.putExtra(Constant.ImageNAme, mValues[position].getOfferImage());
//                        intent.putExtra(Constant.TextTile, mValues[position].getOfferTitle());
//                        intent.putExtra(Constant.OfferLocation, mValues[position].getOfferLocation());
//                        intent.putExtra(Constant.PhoneNumber, mValues[position].getPhoneNumber());
//                        intent.putExtra(Constant.OfferID, mValues[position].getOfferId());
//                        int[] screenLocation = new int[2];
//                        headerHolder.sdv_add_iamge.getLocationOnScreen(screenLocation);
//
//                        intent.putExtra("left", screenLocation[0]).
//                                putExtra("top", screenLocation[1]).
//                                putExtra("width", headerHolder.sdv_add_iamge.getWidth()).
//                                putExtra("height", headerHolder.sdv_add_iamge.getHeight());
//                        Log.i("height", "" + headerHolder.sdv_add_iamge.getHeight());
//                        mContext.startActivity(intent);
//
//                    } else {
//                        Intent intent = new Intent(mContext, PDFViewActivity.class);
//
//                        intent.putExtra("OFFER_ID", mValues[position].getOfferId());
//                        intent.putExtra("OFFER_IMAGE", mValues[position].getOfferImage());
//                        intent.putExtra("BROCHURE_PDF_URL", mValues[position].getAttachmentHTML());
//                        intent.putExtra("BROCHURE_PDF_TITLE", mValues[position].getCompany().getCompanyName_Ar());
//                        intent.putExtra("COMPANY_NAME", mValues[position].getCompany().getCompanyName_Ar());
//                        intent.putExtra("COMPANY_USER_NAME", mValues[position].getCompany().getCompanyName());
//                        intent.putExtra("COMPANY_VERIFIED", mValues[position].getCompany().isCompanyVerified());
//                        intent.putExtra("COMPANY_LOCATION", mValues[position].getOfferLocation());
//                        intent.putExtra("COMPANY_PHONE_NUMBER", mValues[position].getPhoneNumber());
//                        intent.putExtra("OFFER_END_TYPE", mValues[position].getOfferEndType());
//                        intent.putExtra("COMPANY_ID", mValues[position].getCompanyId());
//                        intent.putExtra("COMPANY_TAG_ID", mValues[position].getCompanyTagId());
//                        intent.putExtra("COMPANY_PROFILE_PHOTO", mValues[position].getCompany().getCompanyProfilePhoto());
//                        intent.putExtra("OFFER_TIME_END", mValues[position].getOfferTimeEnd());
//                        mContext.startActivity(intent);
//                    }
//                }
//            });

            headerHolder.btn_navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, mValues[position].getCompany().getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_ID, mValues[position].getCompanyId());


                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position].getCompany().getCompanyProfilePhoto());
                    intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position].getCompany().getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position].getCompany().getCompanyName());

                    intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, mValues[position].getOfferLocation());
                    intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues[position].getPhoneNumber());
                    mContext.startActivity(intent);
                }
            });
            headerHolder.main_ll_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                    intent.putExtra(Constant.SINGLEOFFERID, mValues[position].getOfferId());
//                    mContext.startActivity(intent);
                }
            });
//            headerHolder.txt_add_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                    intent.putExtra(Constant.SINGLEOFFERID, mValues[position].getOfferId());
//                    mContext.startActivity(intent);
//                }
//            });

            if (hashMapRating.containsKey(mValues[position].getOfferId())) {
                headerHolder.rBarRawOffersRating.setRating(hashMapRating.get(mValues[position].getOfferId()));
                headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
            } else {
                int ratingFromApi = mValues[position].getOfferRating();
                Log.e("rating", "" + ratingFromApi);
                headerHolder.rBarRawOffersRating.setRating(ratingFromApi);
                if (ratingFromApi > 0) {
                    headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                } else {
                    headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_timeline_feed_rate);
                }
            }

            headerHolder.rBarRawOffersRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    final int ratingValue = (int) Math.ceil(v);
                    if (ratingValue > 0) {
                        ratingBar.setRating(ratingValue);
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                        if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                            Utility.WarningDialog(mContext);
                            headerHolder.rBarRawOffersRating.setRating(0);
                        } else {
                            hashMapRating.put(mValues[position].getOfferId(), ratingValue);
                            headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                            Home.reportOfferLikes(mValues[position].getOfferId(), ratingValue);
//                            new OfferRatingTask(new String[]{String.valueOf(feedInFocusID), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ratingValue < 3) {
                                            Utility.ShowDislikeDialog(mContext);
                                    } else {
                                        if (sPref.getBoolean(Constant.LIKE_DIALOG, true)) {
                                            Utility.ShowlikeDialog(mContext);
                                            sPref.edit().putBoolean(Constant.LIKE_DIALOG, false).apply();
                                        }

                                    }
                                }
                            }, 250);
                        }
                    }
                }
            });

            headerHolder.ll_raw_offers_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRatingLayoutVisible) {
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.VISIBLE);
                        isRatingLayoutVisible = true;
                    } else {
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                    }
                }
            });

            headerHolder.ll_raw_offers_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mValues[position].getOfferLocation() != null) {
                        Utility.initiateGoogleMapsIntent(mContext, mValues[position].getOfferLocation());
//                        new OfferReportingTask(new int[]{3, mValues[position].getOfferId()}).execute();
                        Home.reportOfferLocation(mValues[position].getOfferId());
                    }  else {
                        Toast.makeText(mContext, mContext.getString(R.string.LocationIsNotAvailable), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            headerHolder.ll_raw_offers_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mValues[position].getPhoneNumber() != null) {
                        Utility.initiateCallIntent(mContext, mValues[position].getPhoneNumber());
//                        new OfferReportingTask(new int[]{2, mValues[position].getOfferId()}).execute();
                        Home.reportOfferCalled(mValues[position].getOfferId());
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.PhoneNumberIsNotAvailable), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            headerHolder.iv_raw_offers_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.shareImage(mValues[position].getOfferTitle() ,mValues[position].getOfferImage(), mContext);
                }
            });

            feedInFocusID = mValues[position].getOfferId();
            offerViewedHandler.removeCallbacks(offerViewedTaskRunnable);
            offerViewedHandler.postDelayed(offerViewedTaskRunnable, 1000);
        }
    }

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
        return mValues.length + 1;
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionFooter (position)) {
            return TYPE_FOOTER;
        }
        return TYPE_HEADER;
    }

    private boolean isPositionFooter (int position) {
        return position == mValues.length;
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compny_username;
        public final TextView txt_compnay_name;
        public final SimpleDraweeView sdvImage;
        AutoLinkTextView txt_add_title;
        SimpleDraweeView sdv_cateogory;
        SimpleDraweeView sdv_add_iamge;
        TextView txt_days;
        TextView txt_hrs;
        TextView txt_scnd;
        TextView txt_minut;
        LinearLayout ll_raw_offers_rating_bar;
        RatingBar rBarRawOffersRating;
        LinearLayout ll_raw_offers_rate;
        ImageView ivRawOffersRate;
        LinearLayout ll_raw_offers_location;
        LinearLayout ll_raw_offers_call;
        LinearLayout llRawOffersTimer;
        LinearLayout llRawOffersEndType;
        TextViewPlus tvRawOffersEndType;

        ImageView img_sponser;
        ImageView iv_raw_offers_share;
        ImageView iv_raw_offers_brochure;
        ImageView iv_raw_company_verified;
        LinearLayout main_ll_timer;
        RelativeLayout btn_navigate;
        public GetAllOffersResponse mItem;
        RelativeLayout main_rl;
        ViewPager view_pager;
        CircleIndicator indicator;

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

        public HeaderViewHolder(View view) {
            super(view);
            mView = view;

            txt_compny_username = (TextViewPlus) view.findViewById(R.id.txt_compny_username);
            txt_compnay_name = (TextViewPlus) view.findViewById(R.id.txt_compnay_name);
            txt_add_title = view.findViewById(R.id.txt_add_title);
            txt_days = (TextViewPlus) view.findViewById(R.id.txt_days);
            txt_hrs = (TextViewPlus) view.findViewById(R.id.txt_hrs);
            btn_navigate = view.findViewById(R.id.btn_navigate);
            txt_scnd = (TextViewPlus) view.findViewById(R.id.txt_scnd);
            main_rl= view.findViewById(R.id.main_rl);
            img_sponser = view.findViewById(R.id.img_sponser);
            main_ll_timer = view.findViewById(R.id.ll_raw_offers_timer_and_buttons_layout);
            txt_minut = (TextViewPlus) view.findViewById(R.id.txt_minut);
            sdvImage = view.findViewById(R.id.sdvImage);
            sdv_cateogory = view.findViewById(R.id.sdv_cateogory);
          //  sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);
            ll_raw_offers_rating_bar = view.findViewById(R.id.ll_timeline_rating);
            rBarRawOffersRating = view.findViewById(R.id.r_bar_timline_rating);
            ll_raw_offers_rate = view.findViewById(R.id.ll_raw_offers_rate);
            ivRawOffersRate = view.findViewById(R.id.iv_raw_timeline_feed_rate);
            ll_raw_offers_location = view.findViewById(R.id.ll_raw_offers_location);
            ll_raw_offers_call = view.findViewById(R.id.ll_raw_offers_call);
            iv_raw_offers_share = view.findViewById(R.id.iv_raw_offers_share);
            iv_raw_offers_brochure = view.findViewById(R.id.iv_raw_offers_brochure);
            iv_raw_company_verified = view.findViewById(R.id.iv_raw_timeline_feed_verified);
            llRawOffersTimer = view.findViewById(R.id.ll_raw_offers_timer);
            llRawOffersEndType = view.findViewById(R.id.ll_raw_feed_offer_end_type);
            tvRawOffersEndType = view.findViewById(R.id.tv_raw_feed_offer_end_type);

            view_pager = view.findViewById(R.id.view_pager);
            indicator = view.findViewById(R.id.indicator);

        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder (View itemView) {
            super (itemView);
        }
    }
}
