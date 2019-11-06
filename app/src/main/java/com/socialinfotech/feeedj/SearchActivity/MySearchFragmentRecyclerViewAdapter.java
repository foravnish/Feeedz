package com.socialinfotech.feeedj.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import com.socialinfotech.feeedj.AppUtils.TextViewPlus;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ApplicationActivities.Home;
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ExploreActivities.CategoryTabActivity;
import com.socialinfotech.feeedj.ParsingModel.GetSearchResponse;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MySearchFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final GetSearchResponse[] mValues;
    private List<ViewHolder> lstHolders;
    boolean isRatingLayoutVisible;
    private Handler mHandler = new Handler();
    Context mContext;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    private ArrayList<GetSearchResponse.CompanyBean> companiesList;

    private static final int TYPE_HORIZONTAL = 0;
    private static final int TYPE_FEEDS = 1;

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
            Home.reportOfferViewed(feedInFocusID);
//            new OfferReportingTask(new int[]{0, feedInFocusID}).execute();
        }
    };

    public MySearchFragmentRecyclerViewAdapter(GetSearchResponse[] getAllOffersResponces, Context cnt) {
        mValues = getAllOffersResponces;
        companiesList = new ArrayList<>();
        for (GetSearchResponse mValue : mValues) {
            boolean idExists = false;
            for (int j = 0; j < companiesList.size(); j++) {
                if (companiesList.get(j).getCompanyId() == mValue.getCompany().getCompanyId()) {
                    idExists = true;
                    break;
                }
            }
            if (!idExists) {
                companiesList.add(mValue.getCompany());
            }
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         if (viewType == TYPE_HORIZONTAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recycler_horizontal_following_items, parent, false);
            return new HorizontalViewHolder(view);
        } else if (viewType == TYPE_FEEDS) {
             View view = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.raw_search_bottom_item, parent, false);
             return new ViewHolder(view);
         }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position== 0) {
            final HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
            layoutManager.setReverseLayout(true);
            horizontalViewHolder.horizontalRecyclerView.setLayoutManager(layoutManager);
            horizontalViewHolder.horizontalRecyclerView.setAdapter(new MySearchHorizontalViewAdapter(companiesList, mContext));
        } else {
            final ViewHolder searchViewHolder = (ViewHolder) holder;
        searchViewHolder.mItem = mValues[position - 1];
        searchViewHolder.txt_compnay_name.setText(mValues[position - 1].getCompany().getCompanyName_Ar());
        searchViewHolder.txt_compny_username.setText(mValues[position - 1].getCompany().getCompanyName());

        searchViewHolder.txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorMidPurple));
        searchViewHolder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        searchViewHolder.txt_add_title.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL);
        searchViewHolder.txt_add_title.setAutoLinkText(mValues[position - 1].getOfferTitle());

//        searchViewHolder.txt_add_title.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.colorMidPurple));

        searchViewHolder.txt_add_title.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
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

//        searchViewHolder.txt_add_title.setUrlModeColor(ContextCompat.getColor(mContext, R.color.facebook_messenger_blue));
        Typeface typeface_txt_add_title = Typeface.createFromAsset(mContext.getAssets(), "demibold.ttf");
        searchViewHolder.txt_add_title.setTypeface(typeface_txt_add_title);

        searchViewHolder.sdvImage.setImageURI(Uri.parse(mValues[position - 1].getCompany().getCompanyProfilePhoto()));
        searchViewHolder.sdv_add_iamge.setImageURI(Uri.parse(mValues[position - 1].getOfferImage()));


            if (mValues[position - 1].getOfferImageCoord() != null) {
                String[] sImageDimensions = mValues[position - 1].getOfferImageCoord().split("x");
                if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
                    searchViewHolder.sdv_add_iamge.setAspectRatio(1);
                } else {
                    searchViewHolder.sdv_add_iamge.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
                }
            }

//        holder.txt_date.setText(Utility.dateConvert(mValues[position - 1].getOfferTimeEnd()));
        synchronized (lstHolders) {
            lstHolders.add(searchViewHolder);
        }
        searchViewHolder.updateTimeRemaining(System.currentTimeMillis());
        searchViewHolder.sdv_cateogory.setImageURI(setCopmnyImage(mValues[position - 1].getCompanyTagId()));
        searchViewHolder.sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ImageViewActivity.class);
//                intent.putExtra(Constant.ImageNAme,mValues[position - 1].getOfferImage());
//                intent.putExtra(Constant.TextTile,mValues[position - 1].getOfferTitle());
//                mContext.startActivity(intent);
                String str = mValues[position - 1].getAttachmentHTML();
                if (str==null) {
                    Intent intent = new Intent(mContext, ImageViewActivity.class);
                    intent.putExtra(Constant.ImageNAme, mValues[position - 1].getOfferImage());
                    intent.putExtra(Constant.TextTile, mValues[position - 1].getOfferTitle());
                    intent.putExtra(Constant.OfferLocation, mValues[position - 1].getOfferLocation());
                    intent.putExtra(Constant.PhoneNumber, mValues[position - 1].getPhoneNumber());
                    intent.putExtra(Constant.OfferID, mValues[position - 1].getOfferId());
                    intent.putExtra(Constant.OFFER_RATING_STATUS, mValues[position - 1].getOfferRating());

                    int[] screenLocation = new int[2];
                    searchViewHolder.sdv_add_iamge.getLocationOnScreen(screenLocation);

                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", searchViewHolder.sdv_add_iamge.getWidth()).
                            putExtra("height", searchViewHolder.sdv_add_iamge.getHeight());
                    mContext.startActivity(intent);

                }else {
                    Intent intent = new Intent(mContext, PDFViewActivity.class);
                    intent.putExtra("OFFER_ID", mValues[position - 1].getOfferId());
                    intent.putExtra("OFFER_IMAGE", mValues[position - 1].getOfferImage());
                    intent.putExtra("BROCHURE_PDF_URL", mValues[position - 1].getAttachmentHTML());
                    intent.putExtra("BROCHURE_PDF_TITLE", mValues[position - 1].getCompany().getCompanyName_Ar());
                    intent.putExtra("COMPANY_NAME", mValues[position - 1].getCompany().getCompanyName_Ar());
                    intent.putExtra("COMPANY_USER_NAME", mValues[position - 1].getCompany().getCompanyName());
                    intent.putExtra("COMPANY_VERIFIED", mValues[position - 1].isCompanyVerified());
                    intent.putExtra("COMPANY_LOCATION", mValues[position - 1].getOfferLocation());
                    intent.putExtra("COMPANY_PHONE_NUMBER", mValues[position - 1].getPhoneNumber());
                    intent.putExtra("OFFER_END_TYPE", mValues[position - 1].getOfferEndType());
                    intent.putExtra("COMPANY_ID", mValues[position - 1].getCompanyId());
                    intent.putExtra("COMPANY_TAG_ID", mValues[position - 1].getCompanyTagId());
                    intent.putExtra("COMPANY_PROFILE_PHOTO", mValues[position - 1].getCompany().getCompanyProfilePhoto());
                    intent.putExtra("OFFER_TIME_END", mValues[position - 1].getOfferTimeEnd());
                    mContext.startActivity(intent);
                }
            }
        });

        if (mValues[position - 1].isCompanyVerified()) {
            searchViewHolder.iv_raw_company_verified.setVisibility(View.VISIBLE);
        } else {
            searchViewHolder.iv_raw_company_verified.setVisibility(View.GONE);
        }

        int featureType = mValues[position - 1].getFeatureType();
        boolean isFeatured = mValues[position - 1].isFeatured();

        if (featureType > 1 || isFeatured) {
            if (featureType == 3) {
                searchViewHolder.img_sponser.setVisibility(View.GONE);
                searchViewHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
            } else if (featureType == 4) {
                searchViewHolder.iv_raw_offers_brochure.setVisibility(View.VISIBLE);
                searchViewHolder.img_sponser.setVisibility(View.GONE);
            } else if (featureType == 2 || isFeatured) {
                searchViewHolder.img_sponser.setVisibility(View.VISIBLE);
                searchViewHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
            }
        } else {
            searchViewHolder.img_sponser.setVisibility(View.GONE);
            searchViewHolder.iv_raw_offers_brochure.setVisibility(View.GONE);
        }

            String offerEndType = "Timer";

            if (mValues[position - 1].getOfferEndType() != null) {
                offerEndType = mValues[position - 1].getOfferEndType().trim();
            }

        if (offerEndType.equalsIgnoreCase("Timer")) {
            searchViewHolder.llRawOffersTimer.setVisibility(View.VISIBLE);
            searchViewHolder.llRawOffersEndType.setVisibility(View.GONE);
        } else {
            searchViewHolder.llRawOffersTimer.setVisibility(View.GONE);
            searchViewHolder.llRawOffersEndType.setVisibility(View.VISIBLE);

            if (offerEndType.equalsIgnoreCase("LimitedQuantity")) {
                searchViewHolder.tvRawOffersEndType.setText("العرض متوفر حتى انتهاء الكمية");
            } else if (offerEndType.equalsIgnoreCase("LimitedTime")) {
                searchViewHolder.tvRawOffersEndType.setText("العرض متوفر لمدة محدودة");
            }
        }

        searchViewHolder.sdv_cateogory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryTabActivity.class);
                intent.putExtra("message", mValues[position - 1].getCompanyTagId());
                intent.putExtra("title", Utility.getCategoryTitleByID(mValues[position - 1].getCompanyTagId()));
                mContext.startActivity(intent);
            }
        });
        searchViewHolder.btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                intent.putExtra(Constant.ToolbarTitle,mValues[position - 1].getCompany().getCompanyName_Ar());
                intent.putExtra(Constant.COMPANY_ID,mValues[position - 1].getCompanyId());

                intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position - 1].getCompany().getCompanyProfilePhoto());
                intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position - 1].getCompany().getCompanyName_Ar());
                intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position - 1].getCompany().getCompanyName());
                intent.putExtra(Constant.COMPANY_PROFILE_VERIFIED, mValues[position - 1].isCompanyVerified());
                intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, mValues[position - 1].getOfferLocation());
                intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues[position - 1].getPhoneNumber());
                mContext.startActivity(intent);
            }
        });
        searchViewHolder.main_ll_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                intent.putExtra(Constant.SINGLEOFFERID, mValues[position - 1].getOfferId());
//                mContext.startActivity(intent);
            }
        });
        searchViewHolder.txt_add_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                intent.putExtra(Constant.SINGLEOFFERID, mValues[position - 1].getOfferId());
//                mContext.startActivity(intent);
            }
        });

            int ratingFromApi = mValues[position - 1].getOfferRating();
            Log.e("rating", "" + ratingFromApi);
            searchViewHolder.rBarRawOffersRating.setRating(ratingFromApi);
            if (ratingFromApi > 0) {
                searchViewHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
            } else {
                searchViewHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_timeline_feed_rate);
            }

        searchViewHolder.rBarRawOffersRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                final int ratingValue = Math.round(v);
                if (ratingValue > 0 && b) {
                    ratingBar.setRating(ratingValue);
                    searchViewHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                    if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                        Utility.WarningDialog(mContext);
                        searchViewHolder.rBarRawOffersRating.setRating(0);
                    } else {
//                        hashMapRating.put(mValues[position - 1].getOfferId(), ratingValue);
                        mValues[position - 1].setOfferRating(ratingValue);
                        searchViewHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);

                        Home.reportOfferLikes(mValues[position - 1].getOfferId(), ratingValue);
//                        new OfferRatingTask(new String[]{String.valueOf(feedInFocusID), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
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

        if (isRatingLayoutVisible) {
            searchViewHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
            isRatingLayoutVisible = false;
        }

        searchViewHolder.ll_raw_offers_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRatingLayoutVisible) {
                    searchViewHolder.ll_raw_offers_rating_bar.setVisibility(View.VISIBLE);
                    isRatingLayoutVisible = true;
                } else {
                    searchViewHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                }
            }
        });

        searchViewHolder.ll_raw_offers_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mValues[position - 1].getOfferLocation() != null) {
                    Utility.initiateGoogleMapsIntent(mContext, mValues[position - 1].getOfferLocation());
//                    new OfferReportingTask(new int[]{3, mValues[position - 1].getOfferId()}).execute();
                    Home.reportOfferLocation(mValues[position - 1].getOfferId());
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.LocationIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchViewHolder.ll_raw_offers_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mValues[position - 1].getPhoneNumber() != null) {
                    Utility.initiateCallIntent(mContext, mValues[position - 1].getPhoneNumber());
//                    new OfferReportingTask(new int[]{2, mValues[position - 1].getOfferId()}).execute();
                    Home.reportOfferCalled(mValues[position - 1].getOfferId());
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.PhoneNumberIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchViewHolder.iv_raw_offers_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.shareImage(mValues[position - 1].getOfferTitle() ,mValues[position - 1].getOfferImage(), mContext);
                Home.reportOfferShared(mValues[position - 1].getOfferId());
            }
        });

        feedInFocusID = mValues[position - 1].getOfferId();
        offerViewedHandler.removeCallbacks(offerViewedTaskRunnable);
        offerViewedHandler.postDelayed(offerViewedTaskRunnable, 1000);
        }

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
        return mValues.length + 1;
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 0) {
            return TYPE_HORIZONTAL;
        } else {
            return TYPE_FEEDS;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compny_username;
        public final TextView txt_compnay_name;
        public final SimpleDraweeView sdvImage;
        AutoLinkTextView txt_add_title;
        SimpleDraweeView sdv_cateogory;
        SimpleDraweeView sdv_add_iamge;
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
                txt_days.setText(dats + "");
                txt_scnd.setText(seconds + "");
                txt_minut.setText(minutes + "");
                txt_hrs.setText(hours + "");
            }
        }

        ViewHolder(View view) {
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
            sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);


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
            tvRawOffersEndType = view.findViewById(R.id.tv_raw_feed_offer_end_type);
//            rb_dislike = (RadioButton)view.findViewById(R.id.rb_dislike);
//            rb_like = (RadioButton)view.findViewById(R.id.rb_like);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }


    private class HorizontalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView horizontalRecyclerView;
        HorizontalViewHolder(View view) {
            super(view);
            horizontalRecyclerView = view.findViewById(R.id.horizontal_list);
        }
    }

}
