package com.socialinfotech.feeedj.TimeLineActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.HeightWrappingViewPager;
import com.socialinfotech.feeedj.AppUtils.TextViewPlus;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ApplicationActivities.Home;
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ExploreActivities.CategoryTabActivity;
import com.socialinfotech.feeedj.ParsingModel.CompnayTagListingResponce;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.socialinfotech.feeedj.ParsingModel.BaseFregment.git;

class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean isRatingLayoutVisible;
    private final GetAllOffersResponse[] mValues;
    public static ArrayList<GetAllOffersResponse> mValuesBrochures;
    private List<HeaderViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    Context mContext;
    private static final int TYPE_BROCHURE = 0;
    private static final int TYPE_FEEDS = 1;
    private static final int TYPE_HORIZONTAL = 2;
    private static final int TYPE_FOOTER = 3;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    RelativeLayout rlBrochure;
    EasyRecyclerView brochureRecyclerView;
    View vBrochureSeparator;
    ImageView ivBrochureBadge;
    View vBrochureBottomSeparator;
    LinearLayoutManager horizontalLayoutManager;
    View viewLayout;

    Pager pagerAdapter;



    int feedInFocusID;
    Handler offerViewedHandler = new Handler();
    Runnable offerViewedTaskRunnable = new Runnable() {
        @Override
        public void run() {
//            new OfferReportingTask(new int[]{0, feedInFocusID}).execute();
            Home.reportOfferViewed(feedInFocusID);
        }
    };

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

    public MyItemRecyclerViewAdapter(GetAllOffersResponse[] getAllOffersResponses, Context cnt) {
        mValues = getAllOffersResponses;
        mValuesBrochures = new ArrayList<>();
        for (int i = 0; i < mValues.length; i++) {
            if (mValues[i].getType() == 4) {
                mValuesBrochures.add(mValues[i]);
            }
        }
        mContext = cnt;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
        sPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sPref.edit();

        horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
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
        if (viewType == TYPE_BROCHURE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_brochure_timeline, parent, false);
            return new BrochureViewHolder(view);
        }else if (viewType == TYPE_FEEDS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_following_item, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_HORIZONTAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recycler_horizontal_following_items, parent, false);
            return new HorizontalViewHolder(view);
        }  else if(viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from (parent.getContext ())
                    .inflate (R.layout.footer_item, parent, false);
            return new FooterViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof HeaderViewHolder || holder instanceof HorizontalViewHolder || holder instanceof BrochureViewHolder) {
            if (position == 0) {
//                git.getTimelineBrochures(new Callback<TimelineBrochureResponse[]>() {
//                    @Override
//                    public void success(TimelineBrochureResponse[] timelineBrochureResponses, Response response) {
//                        Log.i("brochureResponseLength", "" + timelineBrochureResponses.length);
                        if (mValuesBrochures.size() > 0) {
                            brochureRecyclerView.setAdapter(new TimelineBrochureAdapter(mValuesBrochures, mContext));
                            brochureRecyclerView.setLayoutManager(horizontalLayoutManager);
                            horizontalLayoutManager.setStackFromEnd(true);
                            holder.itemView.setVisibility(View.VISIBLE);
                            showBrochure();
                        } else {
                            holder.itemView.setVisibility(View.GONE);
                            hideBrochure();
//                        }
                    }
//                    @Override
//                    public void failure(RetrofitError error) {}
//                });
            } else if (position == 3) {
                final HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
                Call<CompnayTagListingResponce> callCompanyTagListingResponse = git.getCompanyList(5);
                callCompanyTagListingResponse.enqueue(new Callback<CompnayTagListingResponce>() {
                    @Override
                    public void onResponse(Call<CompnayTagListingResponce> call, Response<CompnayTagListingResponce> response) {
                        if (response.body().getCompanies().size() > 0) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
                            layoutManager.setReverseLayout(true);
                            horizontalViewHolder.horizontalRecyclerView.setLayoutManager(layoutManager);
                            horizontalViewHolder.horizontalRecyclerView.setAdapter(new TimelineHorizontalViewAdapter(response.body().getCompanies(), mContext));
                        }
                    }

                    @Override
                    public void onFailure(Call<CompnayTagListingResponce> call, Throwable t) {
                        if (t instanceof IOException) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (position - 1 == mValues.length) {
                // ignore footer position
            } else  {
                final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.mItem = mValues[position - 1];
                headerHolder.txt_compnay_name.setText(mValues[position - 1].getCompany().getCompanyName_Ar());
                headerHolder.txt_compny_username.setText(mValues[position - 1].getCompany().getCompanyName());
                headerHolder.txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorMidPurple));
                headerHolder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                headerHolder.txt_add_title.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL);
                headerHolder.txt_add_title.setAutoLinkText(mValues[position - 1].getOfferTitle());

                headerHolder.txt_add_title.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
                    if (autoLinkMode.equals(AutoLinkMode.MODE_HASHTAG)) {
                        Utility.search(mContext, matchedText.trim());
                    } else if (autoLinkMode.equals(AutoLinkMode.MODE_URL)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(matchedText.trim()));
                        mContext.startActivity(browserIntent);
                    }
                });

//                headerHolder.txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                Typeface typeface_txt_add_title = Typeface.createFromAsset(mContext.getAssets(), "demibold.ttf");
                headerHolder.txt_add_title.setTypeface(typeface_txt_add_title);

//            if (offerTitle.contains("#")) {
//                    SpannableString ss = new SpannableString(offerTitle);
//                    ClickableSpan span1 = new ClickableSpan() {
//                        @Override
//                        public void onClick(View textView) {
//                            Log.e("span1", "clicked");
//                        }
//                    };
//
//                    ClickableSpan span2 = new ClickableSpan() {
//                        @Override
//                        public void onClick(View textView) {
//                            Log.e("span2", "clicked");
//                        }
//                    };
//
//                    int hashStartIndex = offerTitle.indexOf("#");
//
//                try {
//                    ss.setSpan(span1, hashStartIndex, offerTitle.indexOf(" ", hashStartIndex), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                } catch (IndexOutOfBoundsException e) {
//                    Log.e("outOfBound", "" + e);
//                    ss.setSpan(span1, hashStartIndex, offerTitle.indexOf(offerTitle.length(), hashStartIndex), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
////                  ss.setSpan(span2, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    headerHolder.txt_add_title.setText(ss);
//                    headerHolder.txt_add_title.setMovementMethod(LinkMovementMethod.getInstance());
//
////                SpannableString ss = new SpannableString(offerTitle);
////
////                Pattern p = Pattern.compile("[#@][\\u0621-\\u064A\\u0660-\\u0669a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[\\u0621-\\u064A\\u0660-\\u0669a-zA-Z0-9][\\u0621-\\u064A\\u0660-\\u0669a-zA-Z0-9\\-]{0,64}(\\.[\\u0621-\\u064A\\u0660-\\u0669a-zA-Z0-9][\\u0621-\\u064A\\u0660-\\u0669a-zA-Z0-9\\-]{0,25})+"); //match letters or numbers after a # or @
////                Matcher m = p.matcher(offerTitle); //get matcher, applying the pattern to caption string
////                while (m.find()) { // Find each match in turn
////                    ClickableSpan clickableSpan = new ClickableSpan() {
////                        @Override
////                        public void onClick(View textView) {
////                            //Clicked word
////                            Log.e("clicked#", "" + textView.toString());
////                        }
////                    };
////                    ss.setSpan(clickableSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                }
////
////                headerHolder.txt_add_title.setText(ss);
////                headerHolder.txt_add_title.setMovementMethod(LinkMovementMethod.getInstance());
//
//            } else {
//                headerHolder.txt_add_title.setText(offerTitle);
//            }

                headerHolder.sdvImage.setImageURI(Uri.parse(mValues[position - 1].getCompany().getCompanyProfilePhoto()));


                pagerAdapter = new Pager(Arrays.asList(mValues), mContext,mValues[position-1].getOfferImages());
                headerHolder.view_pager.setAdapter(pagerAdapter);
                headerHolder.view_pager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (mValues[position-1].getOfferImages().size()>1) {
                    headerHolder.indicator.setViewPager(headerHolder.view_pager);
                }

//                ViewGroup.LayoutParams layoutParams = viewLayout.getLayoutParams();
//                layoutParams.height =  ViewGroup.LayoutParams.WRAP_CONTENT;
//                viewLayout.setLayoutParams(layoutParams);

//                headerHolder.view_pager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.max(ViewGroup.LayoutParams.WRAP_CONTENT, 1)));

                //headerHolder.sdv_add_iamge.setImageURI(Uri.parse(mValues[position - 1].getOfferImage()));
                if (mValues[position - 1].getOfferImageCoord() != null) {
                    String[] sImageDimensions = mValues[position - 1].getOfferImageCoord().split("x");
                    if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
                        //headerHolder.sdv_add_iamge.setAspectRatio(1);
                    } else {
                       // headerHolder.sdv_add_iamge.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
                    }
                }

//            headerHolder.txt_date.setText(Utility.dateConvert(mValues[position - 1].getOfferTimeEnd()));
                synchronized (lstHolders) {
                    lstHolders.add(headerHolder);
                }

                if (mValues[position - 1].getCompany().isCompanyVerified()) {
                    headerHolder.iv_raw_company_verified.setVisibility(View.VISIBLE);
                } else {
                    headerHolder.iv_raw_company_verified.setVisibility(View.GONE);
                }

                int featureType = mValues[position - 1].getType();
                boolean isFeatured = mValues[position - 1].isFeatured();

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

                if (mValues[position - 1].getOfferEndType() != null) {
                    offerEndType = mValues[position - 1].getOfferEndType().trim();
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
                headerHolder.sdv_cateogory.setImageURI(setCopmnyImage(mValues[position - 1].getCompanyTagId()));

                headerHolder.sdv_cateogory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CategoryTabActivity.class);
                        intent.putExtra("message", mValues[position - 1].getCompanyTagId());
                        intent.putExtra("title", Utility.getCategoryTitleByID(mValues[position - 1].getCompanyTagId()));
                        mContext.startActivity(intent);
                    }
                });

//                headerHolder.sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("co-ords", "" + mValues[position - 1].getOfferImageCoord());
//                        String str = mValues[position - 1].getAttachmentHTML();
//                        if (str == null) {
//                            Intent intent = new Intent(mContext, ImageViewActivity.class);
//                            intent.putExtra(Constant.ImageNAme, mValues[position - 1].getOfferImage());
//                            intent.putExtra(Constant.TextTile, mValues[position - 1].getOfferTitle());
//                            intent.putExtra(Constant.OfferLocation, mValues[position - 1].getOfferLocation());
//                            intent.putExtra(Constant.PhoneNumber, mValues[position - 1].getPhoneNumber());
//                            intent.putExtra(Constant.OfferID, mValues[position - 1].getOfferId());
//                            intent.putExtra(Constant.OFFER_RATING_STATUS, mValues[position - 1].getOfferRating());
//                            int[] screenLocation = new int[2];
//                            headerHolder.sdv_add_iamge.getLocationOnScreen(screenLocation);
//
//                            intent.putExtra("left", screenLocation[0]).
//                                    putExtra("top", screenLocation[1]).
//                                    putExtra("width", headerHolder.sdv_add_iamge.getWidth()).
//                                    putExtra("height", headerHolder.sdv_add_iamge.getHeight());
//                            mContext.startActivity(intent);
//
//                        } else {
//                            Intent intent = new Intent(mContext, PDFViewActivity.class);
//                            intent.putExtra("OFFER_ID", mValues[position - 1].getOfferId());
//                            intent.putExtra("OFFER_IMAGE", mValues[position - 1].getOfferImage());
//                            intent.putExtra("BROCHURE_PDF_URL", mValues[position - 1].getAttachmentHTML());
//                            intent.putExtra("BROCHURE_PDF_TITLE", mValues[position - 1].getCompany().getCompanyName_Ar());
//                            intent.putExtra("COMPANY_NAME", mValues[position - 1].getCompany().getCompanyName_Ar());
//                            intent.putExtra("COMPANY_USER_NAME", mValues[position - 1].getCompany().getCompanyName());
//                            intent.putExtra("COMPANY_VERIFIED", mValues[position - 1].getCompany().isCompanyVerified());
//                            intent.putExtra("COMPANY_LOCATION", mValues[position - 1].getOfferLocation());
//                            intent.putExtra("COMPANY_PHONE_NUMBER", mValues[position - 1].getPhoneNumber());
//                            intent.putExtra("OFFER_END_TYPE", mValues[position - 1].getOfferEndType());
//                            intent.putExtra(Constant.COMPANY_ID, mValues[position - 1].getCompanyId());
//                            intent.putExtra("COMPANY_TAG_ID", mValues[position - 1].getCompanyTagId());
//                            intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position - 1].getCompany().getCompanyProfilePhoto());
//                            intent.putExtra("OFFER_TIME_END", mValues[position - 1].getOfferTimeEnd());
//                            mContext.startActivity(intent);
//                        }
//                    }
//                });

                headerHolder.btn_navigate.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, mValues[position - 1].getCompany().getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_ID, mValues[position - 1].getCompanyId());
                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position - 1].getCompany().getCompanyProfilePhoto());
                    intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position - 1].getCompany().getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position - 1].getCompany().getCompanyName());
                    intent.putExtra(Constant.COMPANY_PROFILE_VERIFIED, mValues[position - 1].getCompany().isCompanyVerified());
                    intent.putExtra(Constant.COMPANY_PROFILE_LOCATION, mValues[position - 1].getOfferLocation());
                    intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues[position - 1].getPhoneNumber());

                    // TODO: Add more extras
                    mContext.startActivity(intent);
                });
//                headerHolder.main_ll_timer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                        intent.putExtra(Constant.SINGLEOFFERID, mValues[position - 1].getOfferId());
//                        mContext.startActivity(intent);
//                    }
//                });
//            headerHolder.txt_add_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                    intent.putExtra(Constant.SINGLEOFFERID, mValues[position - 1].getOfferId());
//                    mContext.startActivity(intent);
//                }
//            });

//                if (hashMapRating.containsKey(mValues[position - 1].getOfferId())) {
//                    headerHolder.rBarRawOffersRating.setRating(hashMapRating.get(mValues[position - 1].getOfferId()));
//                    headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
//                } else {
                    int ratingFromApi = mValues[position - 1].getOfferRating();
                    headerHolder.rBarRawOffersRating.setRating(ratingFromApi);
                    if (ratingFromApi > 0) {
                        headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                    } else {
                        headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_timeline_feed_rate);
                    }
//                }

                headerHolder.rBarRawOffersRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
                    final int ratingValue = Math.round(v);
                    if (ratingValue > 0 && b) {
                        ratingBar.setRating(ratingValue);
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                        if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                            Utility.WarningDialog(mContext);
                            headerHolder.rBarRawOffersRating.setRating(0);
                        } else {
                            headerHolder.ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                            mValues[position - 1].setOfferRating(ratingValue);
//                            new OfferRatingTask(new String[]{String.valueOf(feedInFocusID), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
                            Home.reportOfferLikes(mValues[position - 1].getOfferId(), ratingValue);
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
                });

                if (isRatingLayoutVisible) {
                    headerHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                }

                headerHolder.ll_raw_offers_rate.setOnClickListener(view -> {
                    if (!isRatingLayoutVisible) {
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.VISIBLE);
                        isRatingLayoutVisible = true;
                    } else {
                        headerHolder.ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                    }
                });

                headerHolder.ll_raw_offers_location.setOnClickListener(view -> {
                    if (mValues[position - 1].getOfferLocation() != null) {
                        Utility.initiateGoogleMapsIntent(mContext, mValues[position - 1].getOfferLocation());
//                        new OfferReportingTask(new int[]{3, mValues[position - 1].getOfferId()}).execute();
                        Home.reportOfferLocation(mValues[position - 1].getOfferId());
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.LocationIsNotAvailable), Toast.LENGTH_SHORT).show();
                    }
                });

                headerHolder.ll_raw_offers_call.setOnClickListener(view -> {
                    if (mValues[position -1].getPhoneNumber() != null) {
                        Utility.initiateCallIntent(mContext, mValues[position - 1].getPhoneNumber());
//                        new OfferReportingTask(new int[]{2, mValues[position - 1].getOfferId()}).execute();
                        Home.reportOfferCalled(mValues[position - 1].getOfferId());
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.PhoneNumberIsNotAvailable), Toast.LENGTH_SHORT).show();
                    }
                });

                headerHolder.iv_raw_offers_share.setOnClickListener(view -> {
//                    Home.reportStatisticsCompanyShare(mValues[position - 1].getCompanyId());
                    Home.reportOfferShared(mValues[position - 1].getOfferId());
                    Utility.shareImage(mValues[position - 1].getOfferTitle() ,mValues[position - 1].getOfferImage(), mContext);
                });

                feedInFocusID = mValues[position - 1].getOfferId();
                offerViewedHandler.removeCallbacks(offerViewedTaskRunnable);
                offerViewedHandler.postDelayed(offerViewedTaskRunnable, 1000);
            }
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
        return mValues.length + 2;
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 0) {
            return TYPE_BROCHURE;
        } else if (position == 3) {
            return TYPE_HORIZONTAL;
        } else if (isPositionFooter (position)) {
            return TYPE_FOOTER;
        }
        return TYPE_FEEDS;
    }

    private boolean isPositionFooter (int position) {
        return position == mValues.length + 2;
    }

    private class BrochureViewHolder extends RecyclerView.ViewHolder {
        BrochureViewHolder(View view) {
            super(view);
            brochureRecyclerView = view.findViewById(R.id.rv_brochure_timeline);
            brochureRecyclerView.setOverScrollMode(EasyRecyclerView.OVER_SCROLL_NEVER);
            brochureRecyclerView.setHorizontalScrollBarEnabled(false);

            rlBrochure = view.findViewById(R.id.rl_brochure_timeline);
            vBrochureSeparator = view.findViewById(R.id.v_raw_brochure_separator);
            ivBrochureBadge = view.findViewById(R.id.iv_brochure_title);
            vBrochureBottomSeparator = view.findViewById(R.id.v_brochure_bottom_separator);



            if (!Utility.brochureInitialized) {
                hideBrochure();
                Utility.brochureInitialized = true;
            }
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        public final TextViewPlus txt_compny_username;
        public final TextViewPlus txt_compnay_name;
        public final SimpleDraweeView sdvImage;
        AutoLinkTextView txt_add_title;
        SimpleDraweeView sdv_cateogory;
        HeightWrappingViewPager view_pager;
        CircleIndicator indicator;
        SimpleDraweeView sdv_add_iamge;
        TextViewPlus txt_date;
        TextViewPlus txt_days;
        TextViewPlus txt_hrs;
        TextViewPlus txt_scnd;
        TextViewPlus txt_minut;
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

        HeaderViewHolder(View view) {
            super(view);
            mView = view;
            txt_compny_username = view.findViewById(R.id.txt_compny_username);
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            txt_add_title = view.findViewById(R.id.txt_add_title);
            txt_days = view.findViewById(R.id.txt_days);
            btn_navigate = view.findViewById(R.id.btn_navigate);
            txt_scnd = view.findViewById(R.id.txt_scnd);
            txt_minut = view.findViewById(R.id.txt_minut);
            txt_hrs = view.findViewById(R.id.txt_hrs);
            main_rl= view.findViewById(R.id.main_rl);
            img_sponser = view.findViewById(R.id.img_sponser);
            main_ll_timer = view.findViewById(R.id.ll_raw_offers_timer_and_buttons_layout);
            sdvImage = view.findViewById(R.id.sdvImage);
            sdv_cateogory = view.findViewById(R.id.sdv_cateogory);
//            sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);
            view_pager = view.findViewById(R.id.view_pager);
            indicator = view.findViewById(R.id.indicator);
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
            viewLayout = view.findViewById(R.id.sdv_add_iamge);
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

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder (View itemView) {
            super (itemView);
        }
    }

    private void hideBrochure() {
        brochureRecyclerView.setVisibility(View.GONE);
        vBrochureSeparator.setVisibility(View.GONE);
        ivBrochureBadge.setVisibility(View.GONE);
        vBrochureBottomSeparator.setVisibility(View.GONE);
        rlBrochure.setVisibility(View.GONE);
    }

    private void showBrochure() {
        brochureRecyclerView.setVisibility(View.VISIBLE);
        vBrochureSeparator.setVisibility(View.VISIBLE);
        ivBrochureBadge.setVisibility(View.VISIBLE);
        vBrochureBottomSeparator.setVisibility(View.VISIBLE);
        rlBrochure.setVisibility(View.VISIBLE);
    }

}