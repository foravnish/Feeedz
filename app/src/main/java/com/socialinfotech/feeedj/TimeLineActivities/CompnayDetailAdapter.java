package com.socialinfotech.feeedj.TimeLineActivities;

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
import android.widget.RadioButton;
import android.widget.RatingBar;
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
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.GetCompnayDetailResponse;
import com.socialinfotech.feeedj.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class CompnayDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final GetCompnayDetailResponse mValues;
    boolean isRatingLayoutVisible;
    Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    private List<VHItem> lstHolders;
    private Handler mHandler = new Handler();
    protected FeeedjAPIClass gitsubscribe;

    PagerDetail pagerAdapter;

    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (VHItem holder : lstHolders) {
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

    public CompnayDetailAdapter(GetCompnayDetailResponse getCompnayDetailResponse, ViewCompanyDetailsActivity viewCompanyDetailsActivity) {
    mValues = getCompnayDetailResponse;
        mContext = viewCompanyDetailsActivity;
        lstHolders = new ArrayList<>();
        sPref = PreferenceManager.getDefaultSharedPreferences(viewCompanyDetailsActivity);
        editor = sPref.edit();
        startUpdateTimer();
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
//        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_company_detail_items, parent, false);
            return new VHItem(view);
//        }
//        else if (viewType == TYPE_HEADER) {
//            View view1 = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.raw_header_compnay_details, parent, false);
//            return new VHHeader(view1);
//        }
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof VHItem) {
            ((VHItem) holder).mItem = mValues.getOffers().get(position);

            ((VHItem) holder).txt_add_title.setHashtagModeColor(mContext.getResources().getColor(R.color.colorMidPurple));
            ((VHItem) holder).txt_add_title.setUrlModeColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            ((VHItem) holder).txt_add_title.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL);
            ((VHItem) holder).txt_add_title.setAutoLinkText(mValues.getOffers().get(position).getOfferTitle());

            ((VHItem) holder).txt_add_title.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
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
//
//            ((VHItem) holder).txt_add_title.setUrlModeColor(ContextCompat.getColor(mContext, R.color.facebook_messenger_blue));

            Typeface typeface_txt_add_title = Typeface.createFromAsset(mContext.getAssets(), "demibold.ttf");
            ((VHItem) holder).txt_add_title.setTypeface(typeface_txt_add_title);

//            ((VHItem) holder).sdv_add_iamge.setImageURI(Uri.parse(mValues.getOffers().get(position).getOfferImage()));

        pagerAdapter = new PagerDetail(Arrays.asList(mValues), mContext,mValues.getOffers().get(position).getOfferImages(),mValues.getOffers().get(position).getOfferImage(),mValues.getMultiple(),mValues.getOffers().get(position).getAttachmentHTML());
        ((VHItem) holder).view_pager.setAdapter(pagerAdapter);
        ((VHItem) holder).view_pager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (mValues.getOffers().get(position).getOfferImages().size()>1) {
            ((VHItem) holder).indicator.setVisibility(View.VISIBLE);
            ((VHItem) holder).indicator.setViewPager(((VHItem) holder).view_pager);
        }
        else{
            ((VHItem) holder).indicator.setVisibility(View.GONE);
        }

        if (mValues.getOffers().get(position).getOfferImageCoord() != null) {
                String[] sImageDimensions = mValues.getOffers().get(position).getOfferImageCoord().split("x");
                if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
                  //  ((VHItem) holder).sdv_add_iamge.setAspectRatio(1);
                } else {
                   // ((VHItem) holder).sdv_add_iamge.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
                }
            }

            ((VHItem) holder).sdvImage.setImageURI(Uri.parse(mValues.getCompanyProfilePhoto()));
            ((VHItem) holder).sdv_cateogory.setImageURI(setCopmnyImage(mValues.getOffers().get(position).getCompanyTagId()));
            ((VHItem) holder).sdv_cateogory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CategoryTabActivity.class);
                    intent.putExtra("message", mValues.getCompanyTags().get(0).getTagId());
                    intent.putExtra("title", Utility.getCategoryTitleByID(mValues.getOffers().get(position).getCompanyTagId()));
                    mContext.startActivity(intent);
                }
            });

            int featureType = mValues.getOffers().get(position).getFeatureType();
            boolean isFeatured = mValues.getOffers().get(position).isFeatured();

            if (featureType > 1 || isFeatured) {
                if (featureType == 3) {
                    ((VHItem) holder).img_sponser.setVisibility(View.GONE);
                    ((VHItem) holder).iv_raw_offers_brochure.setVisibility(View.GONE);
                } else if (featureType == 4) {
                    ((VHItem) holder).iv_raw_offers_brochure.setVisibility(View.VISIBLE);
                    ((VHItem) holder).img_sponser.setVisibility(View.GONE);
                } else if (featureType == 2 || isFeatured) {
                    ((VHItem) holder).img_sponser.setVisibility(View.VISIBLE);
                    ((VHItem) holder).iv_raw_offers_brochure.setVisibility(View.GONE);
                }
            } else {
                ((VHItem) holder).img_sponser.setVisibility(View.GONE);
                ((VHItem) holder).iv_raw_offers_brochure.setVisibility(View.GONE);
            }

            String offerEndType = mValues.getOffers().get(position).getOfferEndType().trim();

            if (offerEndType.equalsIgnoreCase("Timer")) {
                ((VHItem) holder).llRawOffersTimer.setVisibility(View.VISIBLE);
                ((VHItem) holder).llRawOffersEndType.setVisibility(View.GONE);
            } else {
                ((VHItem) holder).llRawOffersTimer.setVisibility(View.GONE);
                ((VHItem) holder).llRawOffersEndType.setVisibility(View.VISIBLE);

                if (offerEndType.equalsIgnoreCase("LimitedQuantity")) {
                    ((VHItem) holder).tvRawOffersEndType.setText("العرض متوفر حتى انتهاء الكمية");
                } else if (offerEndType.equalsIgnoreCase("LimitedTime")) {
                    ((VHItem) holder).tvRawOffersEndType.setText("العرض متوفر لمدة محدودة");
                }
            }

            synchronized (lstHolders) {
                lstHolders.add((VHItem) holder);
            }
            ((VHItem) holder).updateTimeRemaining(System.currentTimeMillis());
//            ((VHItem) holder).sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(mContext, ImageViewActivity.class);
////                    intent.putExtra(Constant.ImageNAme,mValues.getOffers().get(position).getOfferImage());
////                    intent.putExtra(Constant.TextTile,mValues.getOffers().get(position).getOfferTitle());
////                    mContext.startActivity(intent);
//
//                    String str = mValues.getOffers().get(position).getAttachmentHTML();
//                    if (str == null) {
//                        Intent intent = new Intent(mContext, ImageViewActivity.class);
//                        intent.putExtra(Constant.ImageNAme, mValues.getOffers().get(position).getOfferImage());
//                        intent.putExtra(Constant.TextTile, mValues.getOffers().get(position).getOfferTitle());
//                        Log.e("phoneNumberLoc", "" + mValues.getOffers().get(position).getOfferLocation());
//                        intent.putExtra(Constant.OfferLocation, mValues.getOffers().get(position).getOfferLocation());
//                        intent.putExtra(Constant.PhoneNumber, mValues.getPhoneNumber());
//                        intent.putExtra(Constant.OfferID, mValues.getOffers().get(position).getOfferId());
//                        intent.putExtra(Constant.OFFER_RATING_STATUS, mValues.getOffers().get(position).getOfferRating());
//
//                        int[] screenLocation = new int[2];
//                        ((VHItem) holder).sdv_add_iamge.getLocationOnScreen(screenLocation);
//
//                        intent.putExtra("left", screenLocation[0]).
//                                putExtra("top", screenLocation[1]).
//                                putExtra("width", ((VHItem) holder).sdv_add_iamge.getWidth()).
//                                putExtra("height", ((VHItem) holder).sdv_add_iamge.getHeight());
//
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(mContext, PDFViewActivity.class);
//
//                        Log.e("companyID", "" + mValues.getCompanyId());
//                        intent.putExtra("OFFER_ID", mValues.getOffers().get(position).getOfferId());
//                        intent.putExtra("OFFER_IMAGE", mValues.getOffers().get(position).getOfferImage());
//                        intent.putExtra("BROCHURE_PDF_URL", mValues.getOffers().get(position).getAttachmentHTML());
//                        intent.putExtra("BROCHURE_PDF_TITLE", mValues.getCompanyName_Ar());
//                        intent.putExtra("COMPANY_NAME", mValues.getCompanyName_Ar());
//                        intent.putExtra("COMPANY_USER_NAME", mValues.getCompanyName());
//                        intent.putExtra("COMPANY_VERIFIED", mValues.getOffers().get(position).isCompanyVerified());
//                        intent.putExtra("COMPANY_LOCATION", mValues.getOffers().get(position).getOfferLocation());
//                        intent.putExtra("COMPANY_PHONE_NUMBER", mValues.getPhoneNumber());
//                        intent.putExtra("OFFER_END_TYPE", mValues.getOffers().get(position).getOfferEndType());
//                        intent.putExtra("COMPANY_ID", mValues.getOffers().get(position).getCompanyId());
//                        intent.putExtra("COMPANY_TAG_ID", mValues.getOffers().get(position).getCompanyTagId());
//                        intent.putExtra("COMPANY_PROFILE_PHOTO", mValues.getCompanyProfilePhoto());
//                        intent.putExtra("OFFER_TIME_END", mValues.getOffers().get(position).getOfferTimeEnd());
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//            ((VHItem) holder).main_ll_timer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                    intent.putExtra(Constant.SINGLEOFFERID, mValues.getOffers().get(position).getOfferId());
//                    mContext.startActivity(intent);
//                }
//            });
//            ((VHItem) holder).txt_add_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewOfferActivity.class);
//                    intent.putExtra(Constant.SINGLEOFFERID, mValues.getOffers().get(position).getOfferId());
//                    mContext.startActivity(intent);
//                }
//            });

                int ratingFromApi = mValues.getOffers().get(position).getOfferRating();
                Log.e("rating", "" + ratingFromApi);
                ((VHItem) holder).rBarRawOffersRating.setRating(ratingFromApi);
                if (ratingFromApi > 0) {
                    ((VHItem) holder).ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);
                } else {
                    ((VHItem) holder).ivRawOffersRate.setImageResource(R.mipmap.ic_timeline_feed_rate);
                }

            ((VHItem) holder).rBarRawOffersRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    final int ratingValue = Math.round(v);
                    if (ratingValue > 0 && b) {
                        ratingBar.setRating(ratingValue);
                        ((VHItem) holder).ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                        if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                            Utility.WarningDialog(mContext);
                            ((VHItem) holder).rBarRawOffersRating.setRating(0);
                        } else {
                            mValues.getOffers().get(position).setOfferRating(ratingValue);
                            ((VHItem) holder).ivRawOffersRate.setImageResource(R.mipmap.ic_rated_timeline);

                            Home.reportOfferLikes(mValues.getOffers().get(position).getOfferId(), ratingValue);
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

            if (isRatingLayoutVisible) {
                ((VHItem) holder).ll_raw_offers_rating_bar.setVisibility(View.GONE);
                isRatingLayoutVisible = false;
            }

            ((VHItem) holder).ll_raw_offers_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRatingLayoutVisible) {
                        ((VHItem) holder).ll_raw_offers_rating_bar.setVisibility(View.VISIBLE);
                        isRatingLayoutVisible = true;
                    } else {
                        ((VHItem) holder).ll_raw_offers_rating_bar.setVisibility(View.GONE);
                        isRatingLayoutVisible = false;
                    }
                }
            });

            ((VHItem) holder).ll_raw_offers_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mValues.getOffers().get(position).getOfferLocation() != null) {
                        Utility.initiateGoogleMapsIntent(mContext, mValues.getOffers().get(position).getOfferLocation());
//                        new OfferReportingTask(new int[]{3, mValues.getOffers().get(position).getOfferId()}).execute();
                        Home.reportOfferLocation(mValues.getOffers().get(position).getOfferId());
                    } else {
                        Toast.makeText(mContext, R.string.LocationIsNotAvailable, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((VHItem) holder).ll_raw_offers_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mValues.getPhoneNumber() != null) {
                        Utility.initiateCallIntent(mContext, mValues.getPhoneNumber());
//                        new OfferReportingTask(new int[]{2, mValues.getOffers().get(position).getOfferId()}).execute();
                        Home.reportOfferCalled(mValues.getOffers().get(position).getOfferId());
                    } else {
                        Toast.makeText(mContext, R.string.LocationIsNotAvailable, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((VHItem) holder).iv_raw_offers_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.shareImage(mValues.getOffers().get(position).getOfferTitle() ,mValues.getOffers().get(position).getOfferImage(), mContext);
                    Home.reportOfferShared(mValues.getOffers().get(position).getOfferId());
                }
            });

            feedInFocusID = mValues.getOffers().get(position).getOfferId();
            offerViewedHandler.removeCallbacks(offerViewedTaskRunnable);
            offerViewedHandler.postDelayed(offerViewedTaskRunnable, 1000);
//        } else if (holder instanceof VHHeader) {
//            //cast holder to VHHeader and set data for header.
//            ((VHHeader) holder).sdv_add_iamge_header.setImageURI(Uri.parse(mValues.getCompanyHeaderPhoto()));
//            ((VHHeader) holder).sdv_compny_icon.setImageURI(Uri.parse(mValues.getCompanyProfilePhoto()));
//            ((VHHeader) holder).sdv_compny_category.setImageURI(setCopmnyImage(mValues.getCompanyTags().get(0).getTagId()));
//            ((VHHeader) holder).sdv_compny_category.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, CategoryTabActivity.class);
//                    intent.putExtra("message",mValues.getCompanyTags().get(0).getTagId());
//                    intent.putExtra("title",Utility.getCategoryTitleByID(mValues.getCompanyTags().get(0).getTagId()));
//                    mContext.startActivity(intent);
//                }
//            });
//
//            if (mValues.isIsFollowed()){
//                ((VHHeader)holder).chk_followed.setChecked(true);
//            }else{
//                ((VHHeader)holder).chk_followed.setChecked(false);
//            }
//            ((VHHeader) holder).txt_advrt_title.setText(mValues.getCompanyName_Ar());
//            ((VHHeader) holder).txt_compny_tag.setText(mValues.getCompanyName());
//            ((VHHeader) holder).txt_compny_advrt_title.setText(mValues.getCompanyDescription());
//            ((VHHeader) holder).txt_compny_website.setText(mValues.getWebSite());
//            ((VHHeader) holder).txt_offer.setText(mValues.getNumberOfOffers()+"");
//            ((VHHeader) holder).txt_subscribe.setText(mValues.getNumberOfSubscribers()+"");
//            ((VHHeader)holder).chk_followed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (((CheckBox)v).isChecked()){
//                        if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")){
//                            WarningDialog();
//                            ((VHHeader)holder).chk_followed.setChecked(false);
//                        }else{
//                        Subscribe(mValues.getCompanyId(),position);
//                        }
//                    }else{
//                    UnSubscribe(mValues.getCompanyId(),position);
//                    }
//                }
//            });
//        }
    }

//    private void WarningDialog() {
//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.dialog_login_warning);
//
//        Button btn_ok = dialog.findViewById(R.id.btn_ok);
//        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
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
//        dialog.show();
//    }
//
//    public void Subscribe(int companyId,int pos) {
//        Call<ForgotPasswordResponse> callForgotPasswordResponse = gitsubscribe.Subscribe(companyId);
//        callForgotPasswordResponse.enqueue(new Callback<ForgotPasswordResponse>() {
//            @Override
//            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
//                if (response.isSuccessful()){
//                    editor.remove(Constant.FOLLOWING).commit();
//                    mValues.setIsFollowed(true);
//                    mValues.setNumberOfSubscribers(mValues.getNumberOfSubscribers()+1);
//                    notifyDataSetChanged();
//                    //Toast.makeText(mContext, "Subscribed successfully", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(mContext, "شكله صارت مشكله, حاول مرة ثانية", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) { }
//        });
//    }
//
//    public void UnSubscribe(int companyId, int pos) {
//        Call<ForgotPasswordResponse> callForgotPasswordResponse = gitsubscribe.UnSubscribe(companyId);
//        callForgotPasswordResponse.enqueue(new Callback<ForgotPasswordResponse>() {
//            @Override
//            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
//                if (response.isSuccessful()) {
//                    editor.remove(Constant.FOLLOWING).commit();
//                    mValues.setIsFollowed(false);
//                    mValues.setNumberOfSubscribers(mValues.getNumberOfSubscribers()-1);
//                    notifyDataSetChanged();
//                    //Toast.makeText(mContext, "Unsubscribed successfully", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(mContext, "شكله صارت مشكله, حاول مرة ثانية", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
//
//            }
//        });
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

//    @Override
//    public int getItemViewType(int position) {
//        if (isPositionHeader(position))
//            return TYPE_HEADER;
//
//        return TYPE_ITEM;
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
//
//    private boolean isPositionHeader(int position) {
//        return position == 0;
//    }

    @Override
    public int getItemCount() {
//        return mValues.getOffers().size() + 1;
        return mValues.getOffers().size();
    }

    public class VHItem extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView sdvImage;
        AutoLinkTextView txt_add_title;
        SimpleDraweeView sdv_cateogory;
        SimpleDraweeView sdv_add_iamge;
        TextView txt_days;
        TextView txt_hrs;
        TextView txt_scnd;
        TextView txt_minut;
        RadioButton rb_dislike;
        RadioButton rb_like;
        ImageView img_sponser;
        ImageView iv_raw_offers_share;
        ImageView iv_raw_offers_brochure;
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
        HeightWrappingViewPager view_pager;
        CircleIndicator indicator;
        GetCompnayDetailResponse.OffersBean mItem;
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

        public VHItem(View view) {
            super(view);
            mView = view;
            txt_add_title = view.findViewById(R.id.txt_add_title);
            txt_days = view.findViewById(R.id.txt_days);
            txt_hrs = view.findViewById(R.id.txt_hrs);
            txt_scnd = view.findViewById(R.id.txt_scnd);
            txt_minut = view.findViewById(R.id.txt_minut);
            sdvImage = view.findViewById(R.id.sdvImage);
            img_sponser = view.findViewById(R.id.img_sponser);
            sdv_cateogory = view.findViewById(R.id.sdv_cateogory);
           // sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);
            rb_dislike = view.findViewById(R.id.rb_dislike);
            rb_like = view.findViewById(R.id.rb_like);

            main_ll_timer = view.findViewById(R.id.ll_raw_offers_timer_and_buttons_layout);
            ll_raw_offers_rating_bar = view.findViewById(R.id.ll_timeline_rating);
            rBarRawOffersRating = view.findViewById(R.id.r_bar_company_detail_rating);
            ll_raw_offers_rate = view.findViewById(R.id.ll_raw_offers_rate);
            ivRawOffersRate = view.findViewById(R.id.iv_raw_company_detail_rate);
            ll_raw_offers_location = view.findViewById(R.id.ll_raw_offers_location);
            ll_raw_offers_call = view.findViewById(R.id.ll_raw_offers_call);
            iv_raw_offers_share = view.findViewById(R.id.iv_raw_offers_share);
            iv_raw_offers_brochure = view.findViewById(R.id.iv_raw_offers_brochure);
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
//    public class VHHeader extends RecyclerView.ViewHolder {
//        SimpleDraweeView sdv_add_iamge_header;
//        SimpleDraweeView sdv_compny_icon;
//
//        CheckBox chk_followed;
//
//        SimpleDraweeView sdv_compny_category;
//
//        TextView txt_advrt_title,txt_compny_tag,txt_compny_advrt_title,txt_compny_website,txt_offer,txt_subscribe;
//
//        public VHHeader(View itemView) {
//            super(itemView);
//            chk_followed = itemView.findViewById(R.id.chk_followed);
//            sdv_compny_category = itemView.findViewById(R.id.sdv_compny_category);
//            sdv_compny_icon = itemView.findViewById(R.id.sdv_compny_icon);
//            sdv_add_iamge_header= itemView.findViewById(R.id.sdv_add_iamge_header);
//
//            txt_advrt_title = itemView.findViewById(R.id.txt_advrt_title);
//            txt_compny_tag = itemView.findViewById(R.id.txt_compny_tag);
//            txt_compny_advrt_title = itemView.findViewById(R.id.txt_compny_advrt_title);
//            txt_compny_website = itemView.findViewById(R.id.txt_compny_website);
//
//            txt_offer = itemView.findViewById(R.id.txt_offer);
//            txt_subscribe = itemView.findViewById(R.id.txt_subscribe);
//        }
//    }
}
