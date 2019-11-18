package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.facebook.drawee.view.SimpleDraweeView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.TextViewPlus;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ExploreActivities.CategoryTabActivity;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PDFViewActivity extends AppCompatActivity implements View.OnClickListener {

    //PDFView pdfView;
    WebView pdfWebView;
    //DownloadPDFTask taskDownloadPDF;
    int responseCode;
    public ProgressDialog progressDialog;
    boolean isDownloadPDFTaskRunning;
    LinearLayout llPDFViewToolbarShare;
    boolean isRatingLayoutVisible;
    RelativeLayout rlPDFViewTitleBar;
    RelativeLayout rlPDFViewBottomBar;

    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
                long currentTime = System.currentTimeMillis();
                    updateTimeRemaining(currentTime);
        }
    };

    SimpleDraweeView sdv_cateogory;
    SimpleDraweeView sdvCompanyProfilePhoto;

    LinearLayout llBrochureRatingBar;
    LinearLayout llBrochureRate;

    LinearLayout llPDFViewBottomToolbarTimer;
    LinearLayout llPDFViewBottomToolbarOfferEndType;
    LinearLayout llPDFViewBottomToolbarLocation;
    LinearLayout llPDFViewBottomToolbarCall;

    TextViewPlus tvPDFViewBottomToolbarOfferEndType;

    ImageView ivPDFViewIsCompanyVerified;

    TextViewPlus tvPDFViewTopBarCompanyName;
    TextViewPlus tvPDFViewTopBarCompanyUserName;

    ImageView ivPDFViewRate;

    TextViewPlus txt_days;
    TextViewPlus txt_hrs;
    TextViewPlus txt_scnd;
    TextViewPlus txt_minut;

    Animation animationBrochureBottomSlideDown;
    Animation animationBrochureBottomSlideUp;
    Animation animationBrochureTopSlideUp;
    Animation animationBrochureTopSlideDown;

    SharedPreferences sPref;

    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.toolbar_pdf_view_heading)
    TextView tvHeading;
    Bundle bundle;
    RatingBar rBarPDFViewRating;
    private boolean areTopBottomBarsVisible = true;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Brochure Clicked");

        setContentView(R.layout.activity_pdf_web_view);
        ButterKnife.bind(this);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        //pdfView = findViewById(R.id.pdfViewer);
        pdfWebView = findViewById(R.id.pdfWebView);
        pdfWebView.setWebViewClient(new WebViewClient());
        pdfWebView.getSettings().setBuiltInZoomControls(true);
        pdfWebView.getSettings().setDisplayZoomControls(false);
        pdfWebView.getSettings().setUseWideViewPort(true);
        pdfWebView.getSettings().setLoadWithOverviewMode(true);
        pdfWebView.getSettings().setSupportZoom(true);
        pdfWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        pdfWebView.setInitialScale(0);
        pdfWebView.getSettings().setJavaScriptEnabled(true);

        animationBrochureBottomSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_layout_brochure_bottom_slide_down);
        animationBrochureBottomSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_layout_brochure_bottom_slide_up);

        txt_days = findViewById(R.id.txt_days);
        txt_scnd = findViewById(R.id.txt_scnd);
        txt_minut = findViewById(R.id.txt_minut);
        txt_hrs = findViewById(R.id.txt_hrs);
//
        animationBrochureTopSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout_brochure_top_slide_down);
        animationBrochureTopSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout_brochure_top_slide_up);

        final GestureDetector gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_MAX_OFF_PATH = 600;
            private static final int SWIPE_MIN_DISTANCE = 80;
            private static final int SWIPE_MIN_DISTANCE_FOR_DOWN_SWIPE = 40;
            private static final int SWIPE_THRESHOLD_VELOCITY = 60;
            private static final int SWIPE_THRESHOLD_VELOCITY_FOR_DOWN_SWIPE = 20;

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (areTopBottomBarsVisible) {
                    toggleBrochureTopDownBarsVisibility(false);
                } else {
                    toggleBrochureTopDownBarsVisibility(true);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                        if (areTopBottomBarsVisible) {
                            toggleBrochureTopDownBarsVisibility(false);
                        }
                    }
                    else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE_FOR_DOWN_SWIPE
                            && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY_FOR_DOWN_SWIPE) {
                        if (!areTopBottomBarsVisible) {
                            toggleBrochureTopDownBarsVisibility(true);
                        }
                    }
                } catch (Exception ignored) {}
                return false;
            }
        });

        pdfWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gd.onTouchEvent(motionEvent);
            }
        });

        btnBack.setOnClickListener(this);

        rlPDFViewTitleBar = findViewById(R.id.rl_brochure_title_bar);
        rlPDFViewBottomBar = findViewById(R.id.rl_brochure_bottom_bar);

        llBrochureRatingBar = findViewById(R.id.ll_timeline_rating);
        llBrochureRate = findViewById(R.id.ll_raw_offers_rate);
        llBrochureRate.setOnClickListener(this);
        llPDFViewToolbarShare = findViewById(R.id.ll_pdf_view_toolbar_share);
        llPDFViewToolbarShare.setOnClickListener(this);
        llPDFViewBottomToolbarTimer = findViewById(R.id.ll_raw_offers_timer);
        llPDFViewBottomToolbarOfferEndType = findViewById(R.id.ll_raw_feed_offer_end_type);
        llPDFViewBottomToolbarLocation = findViewById(R.id.ll_raw_offers_location);
        llPDFViewBottomToolbarLocation.setOnClickListener(this);
        llPDFViewBottomToolbarCall = findViewById(R.id.ll_raw_offers_call);
        llPDFViewBottomToolbarCall.setOnClickListener(this);
        ivPDFViewIsCompanyVerified = findViewById(R.id.iv_raw_timeline_feed_verified);
        tvPDFViewBottomToolbarOfferEndType = findViewById(R.id.tv_raw_feed_offer_end_type);
        rBarPDFViewRating = findViewById(R.id.r_bar_brochure_rating);

        ivPDFViewRate = findViewById(R.id.iv_raw_timeline_feed_rate);

        sdvCompanyProfilePhoto = findViewById(R.id.sdvImage);
        sdv_cateogory = findViewById(R.id.sdv_cateogory);

        tvPDFViewTopBarCompanyName = findViewById(R.id.txt_compnay_name);
        tvPDFViewTopBarCompanyUserName = findViewById(R.id.txt_compny_username);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            tvHeading.setText(bundle.getString("BROCHURE_PDF_TITLE"));
            String pdfURL = bundle.getString("BROCHURE_PDF_URL");
            pdfWebView.loadUrl(pdfURL);
            //taskDownloadPDF = (DownloadPDFTask) new DownloadPDFTask().execute(pdfURL);

            sdvCompanyProfilePhoto.setImageURI(bundle.getString("company_profile_photo"));
            sdvCompanyProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PDFViewActivity.this, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, bundle.getString("COMPANY_NAME"));
                    intent.putExtra(Constant.COMPANY_ID, bundle.getInt("COMPANY_ID" ));
                    PDFViewActivity.this.startActivity(intent);
                }
            });

            tvPDFViewTopBarCompanyName.setText(bundle.getString("COMPANY_NAME"));
            tvPDFViewTopBarCompanyUserName.setText(bundle.getString("COMPANY_USER_NAME"));

            if (bundle.getBoolean("COMPANY_VERIFIED")) {
                ivPDFViewIsCompanyVerified.setVisibility(View.VISIBLE);
            } else {
                ivPDFViewIsCompanyVerified.setVisibility(View.INVISIBLE);
            }

            String offerEndType = bundle.getString("OFFER_END_TYPE");
            if (offerEndType.equalsIgnoreCase("Timer")) {
                llPDFViewBottomToolbarTimer.setVisibility(View.VISIBLE);
            startUpdateTimer();
            } else {
                llPDFViewBottomToolbarTimer.setVisibility(View.GONE);
                llPDFViewBottomToolbarOfferEndType.setVisibility(View.VISIBLE);
                if (offerEndType.equalsIgnoreCase("LimitedQuantity")) {
                    tvPDFViewBottomToolbarOfferEndType.setText("العرض متوفر حتى انتهاء الكمية");
                } else if (offerEndType.equalsIgnoreCase("LimitedTime")) {
                    tvPDFViewBottomToolbarOfferEndType.setText("العرض متوفر لمدة محدودة");
                }
            }
            sdv_cateogory.setImageURI(setCopmnyImage(bundle.getInt("COMPANY_TAG_ID")));

            sdv_cateogory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PDFViewActivity.this, CategoryTabActivity.class);
                    intent.putExtra("message", bundle.getInt("COMPANY_TAG_ID"));
                    intent.putExtra("title", Utility.getCategoryTitleByID(bundle.getInt("COMPANY_TAG_ID")));

                    PDFViewActivity.this.startActivity(intent);
                }
            });

        } else {
            Toast.makeText(PDFViewActivity.this, getResources().getString(
                    R.string.PDFVieWActivityDownloadingFailed), Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        rBarPDFViewRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            final int ratingValue = Math.round(v);
            if (ratingValue > 0 && b) {
                ratingBar.setRating(ratingValue);
                llBrochureRatingBar.setVisibility(View.GONE);
                isRatingLayoutVisible = false;
                if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                    Utility.WarningDialog(PDFViewActivity.this);
                    rBarPDFViewRating.setRating(0);
                } else {
                    ivPDFViewRate.setImageResource(R.mipmap.ic_rated_timeline);
                    Home.reportOfferLikes(bundle.getInt("OFFER_ID"), ratingValue);
//                        new OfferRatingTask(new String[]{bundle.getString("OFFER_ID"), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
                    new Handler().postDelayed(() -> {
                        if (ratingValue < 3) {
                            Utility.ShowDislikeDialog(PDFViewActivity.this);
                        } else {
                            if (sPref.getBoolean(Constant.LIKE_DIALOG, true)) {
                                Utility.ShowlikeDialog(PDFViewActivity.this);
                                sPref.edit().putBoolean(Constant.LIKE_DIALOG, false).apply();
                            }
                        }
                    }, 250);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_pdf_view_toolbar_share:
                Home.reportOfferShared(bundle.getInt("OFFER_ID"));
                Utility.shareImage("بروشور " + bundle.getString("COMPANY_NAME") ,bundle.getString("OFFER_IMAGE"), PDFViewActivity.this);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.ll_raw_offers_location:
                Home.reportOfferLocation(bundle.getInt("OFFER_ID"));
                Utility.initiateGoogleMapsIntent(this, bundle.getString("COMPANY_LOCATION"));
                break;
            case R.id.ll_raw_offers_call:
                Home.reportOfferCalled(bundle.getInt("OFFER_ID"));
                Utility.initiateCallIntent(this , bundle.getString("COMPANY_PHONE_NUMBER"));
                break;
            case R.id.ll_raw_offers_rate:
                if (!isRatingLayoutVisible) {
                    llBrochureRatingBar.setVisibility(View.VISIBLE);
                    isRatingLayoutVisible = true;
                } else {
                    llBrochureRatingBar.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                }
                break;
        }
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

    public void updateTimeRemaining(long currentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(bundle.getString("OFFER_TIME_END"));
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


    private Uri setCopmnyImage(int companyId) {
        String path = "res:/" + R.drawable.tweleve_icon;
        if (companyId==1){
            path = "res:/" + R.drawable.one_industry_icon;
        } else if (companyId==2) {
            path = "res:/" + R.drawable.two_airlinesicon;
        }else if (companyId==3) {
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

    private void toggleBrochureTopDownBarsVisibility(boolean visibility) {
        areTopBottomBarsVisible = visibility;
        if (visibility) {
            rlPDFViewTitleBar.setVisibility(View.VISIBLE);
            rlPDFViewBottomBar.setVisibility(View.VISIBLE);
            rlPDFViewTitleBar.startAnimation(animationBrochureTopSlideDown);
            rlPDFViewBottomBar.startAnimation(animationBrochureBottomSlideUp);
        } else {
            if (isRatingLayoutVisible) {
                llBrochureRatingBar.setVisibility(View.GONE);
                isRatingLayoutVisible = false;
            }
            rlPDFViewTitleBar.startAnimation(animationBrochureTopSlideUp);
            rlPDFViewBottomBar.startAnimation(animationBrochureBottomSlideDown);
            rlPDFViewTitleBar.setVisibility(View.GONE);
            rlPDFViewBottomBar.setVisibility(View.GONE);
        }
    }

//    private class DownloadPDFTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog(PDFViewActivity.this, getResources().getString(
//                    R.string.PDFVieWActivityAlertDialogMessage));
//            isDownloadPDFTaskRunning = true;
//        }
//
//        @Override
//        protected String doInBackground(String... f_url) {
//
//            int count;
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection connectionDownload = url.openConnection();
//                connectionDownload.connect();
//
//                // this will be useful so that you can show a typical 0-100%
//                // progress bar
//                int lengthOfFile = connectionDownload.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(url.openStream(),
//                        8192);
//
//                // Output stream
//                OutputStream output = new FileOutputStream(getApplicationContext().getFilesDir().getPath()
//                        + "/feeedj.pdf");
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }
//
//                // flushing output
//                output.flush();
//
//                // closing streams
//                output.close();
//                input.close();
//                responseCode = 200;
//            } catch (Exception e) {
//                e.printStackTrace();
//                responseCode = 400;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... progress) {
//            progressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            super.onPostExecute(file_url);
//            dismissProgressDialog();
//            if (responseCode == 200) {
////                Helpers.loadFragment(new BrochureFragment(), "BrochureFragment");
////                AppGlobals.putPdfUrl(urlPDF);
////                Log.e("responseCodePDFDownload", "" + responseCode);
//                File pdfFile = new File(getApplicationContext().getFilesDir().getPath()
//                        + "/feeedj.pdf");
//
//                pdfView.fromFile(pdfFile)
////                        .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
//                        .enableSwipe(true)
//                        .swipeHorizontal(false)
//                        .enableDoubletap(true)
//                        .defaultPage(0)
////                        .onDraw(onDrawListener)
////                        .onLoad(onLoadCompleteListener)
////                        .onPageChange(onPageChangeListener)
////                        .onPageScroll(onPageScrollListener)
////                        .onError(onErrorListener)
//                        .enableAnnotationRendering(false)
//                        .password(null)
//                        .scrollHandle(null)
//                        .load();
//            } else {
//                Toast.makeText(PDFViewActivity.this, getResources().getString(
//                        R.string.PDFVieWActivityDownloadingFailed), Toast.LENGTH_LONG).show();
//                onBackPressed();
//            }
//        }
//    }
//
//    public void showProgressDialog(final Context context, String message) {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage(message);
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setMax(100);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(
//                R.string.PDFVieWActivityAlertDialogCancelButton), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                onBackPressed();
//            }
//        });
//        progressDialog.show();
//    }
//
//    public void dismissProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (isDownloadPDFTaskRunning) {
//            taskDownloadPDF.cancel(true);
//        }
//    }

}
