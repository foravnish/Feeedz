package com.socialinfotech.feeedj.ApplicationActivities;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.zoomablelibrary.ZoomableDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewActivity extends AppCompatActivity {

    private static final int SWIPE_MIN_DISTANCE = 160;
    private static final int SWIPE_MAX_OFF_PATH = 260;
    private static final int SWIPE_THRESHOLD_VELOCITY = 450;
    @Bind(R.id.img)
    ZoomableDraweeView img;
    String imgUri;
    int thumbnailWidth, thumbnailHeight, thumbnailTop, thumbnailLeft;
    int ANIM_DURATION = 400;
    private String storeLocation = null;
    private String storePhoneNumber = null;
    View.OnTouchListener gestureListener;
    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;
    private ColorDrawable colorDrawable;
    private GestureDetector gestureDetector;
    Animation animImageViewTransitionUp;
    Animation animImageViewTransitionDown;
    LinearLayout llImageViewRating, llImageButtonsOverlay;
    boolean isRatingLayoutVisible;
    RatingBar rBarImageViewRating;
    ImageView ivImageViewRating;
    Bundle bundle;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        Fresco.initialize(ImageViewActivity.this);


        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        FrameLayout frameLayout = findViewById(R.id.fl_image_view_activity);
        animImageViewTransitionUp = AnimationUtils.loadAnimation(this, R.anim.anim_image_view_transition_up);
        animImageViewTransitionUp.setFillAfter(true);
        animImageViewTransitionDown = AnimationUtils.loadAnimation(this, R.anim.anim_image_view_transition_down);
        animImageViewTransitionDown.setFillAfter(true);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sPref.edit();

        ivImageViewRating = findViewById(R.id.iv_rate_image_view);
        llImageViewRating = findViewById(R.id.ll_image_view_rating);
        llImageButtonsOverlay = findViewById(R.id.ll_image_view_buttons_overlay);
        rBarImageViewRating = findViewById(R.id.r_bar_image_view_rating);
        rBarImageViewRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            final int ratingValue = Math.round(v);
            if (ratingValue > 0) {
                llImageViewRating.setVisibility(View.GONE);
                isRatingLayoutVisible = false;
                if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")) {
                    Utility.WarningDialog(ImageViewActivity.this);
                    rBarImageViewRating.setRating(0);
                } else {
                    ivImageViewRating.setImageResource(R.mipmap.ic_rated_image_view);
                    Home.reportOfferLikes(bundle.getInt("offerID"), ratingValue);
//                    new OfferRatingTask(new String[]{String.valueOf(bundle.getInt("offerID")), String.valueOf(ratingValue), sPref.getString(Constant.ACCESS_TOKEN, "")}).execute();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ratingValue < 3) {
                                Utility.ShowDislikeDialog(ImageViewActivity.this);
                            } else {
                                if (sPref.getBoolean(Constant.LIKE_DIALOG, true)) {
                                    Utility.ShowlikeDialog(ImageViewActivity.this);
                                    sPref.edit().putBoolean(Constant.LIKE_DIALOG, false);
                                }

                            }
                        }
                    }, 250);
                }

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (ratingValue < 3) {
//                                ivImageViewRating.setImageResource(R.mipmap.ic_rated_image_view);
//                                if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
//                                    Utility.WarningDialog(ImageViewActivity.this);
//                                    ivImageViewRating.setImageResource(R.mipmap.ic_rate_image_view);
//                                } else {
//                                    Utility.ShowDislikeDialog(ImageViewActivity.this);
//                                }
//                            } else {
//                                ivImageViewRating.setImageResource(R.mipmap.ic_rated_image_view);
//                                if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")) {
//                                    Utility.WarningDialog(ImageViewActivity.this);
//                                    ivImageViewRating.setImageResource(R.mipmap.ic_rate_image_view);
//                                } else {
//                                    if (sPref.getBoolean(Constant.LIKEDILAOG,true)){
//                                        Utility.ShowlikeDialog(ImageViewActivity.this);
//                                        editor.putBoolean(Constant.LIKEDILAOG,false).apply();
//                                    }
//                                }
//                            }
//                        }
//                    }, 350);
            }
        });
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        img.setOnTouchListener(gestureListener);
        colorDrawable = new ColorDrawable(Color.parseColor("#CC000000"));
        frameLayout.setBackground(colorDrawable);

        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)

        if (savedInstanceState == null) {
            ViewTreeObserver observer = img.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    img.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    img.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / img.getWidth();
                    mHeightScale = (float) thumbnailHeight / img.getHeight();

                    enterAnimation();
                    return true;
                }
            });
        }

        animImageViewTransitionUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTopDelta = mTopDelta + 300;
                onBackPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animImageViewTransitionDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTopDelta = mTopDelta - 300;
                onBackPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        bundle = getIntent().getExtras();
        if (bundle != null) {

            thumbnailTop = bundle.getInt("top");
            thumbnailLeft = bundle.getInt("left");
            thumbnailWidth = bundle.getInt("width");
            thumbnailHeight = bundle.getInt("height");

            imgUri = bundle.getString(Constant.ImageNAme);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(bundle.getString(Constant.ImageNAme))
                    .build();
            img.setController(controller);

            if (bundle.getInt(Constant.OfferID) == 0) {
                llImageButtonsOverlay.setVisibility(View.GONE);
            } else {
                storeLocation = bundle.getString(Constant.OfferLocation);
                storePhoneNumber = bundle.getString(Constant.PhoneNumber);

                int offerRating = bundle.getInt(Constant.OFFER_RATING_STATUS);

                if (offerRating > 0) {
                    ivImageViewRating.setImageResource(R.mipmap.ic_rated_image_view);
                    rBarImageViewRating.setRating(offerRating);
                } else {
                    ivImageViewRating.setImageResource(R.mipmap.ic_rate_image_view);
                }

            /*Typeface type = Typeface.createFromAsset(getAssets(), "FONT/Glober_Regular.otf");
            title.setTypeface(type);*/
//            img.setImageURI(Uri.parse(bundle.getString(Constant.ImageNAme)));
                Home.reportOfferImageViewed(bundle.getInt(Constant.OfferID));
            }
        } else {
            Toast.makeText(ImageViewActivity.this, "شكله صارت مشكلة, حاول مرة ثانية", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.btn_close, R.id.ll_image_view_share, R.id.ll_image_view_rate, R.id.ll_image_view_location, R.id.ll_image_view_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                onBackPressed();
                break;
            case R.id.ll_image_view_share:
                Utility.shareImage(bundle.getString("texttitle") ,imgUri, ImageViewActivity.this);
                Home.reportOfferShared(bundle.getInt(Constant.OfferID));
                break;
            case R.id.ll_image_view_rate:
                if (!isRatingLayoutVisible) {
                    llImageViewRating.setVisibility(View.VISIBLE);
                    isRatingLayoutVisible = true;
                } else {
                    llImageViewRating.setVisibility(View.GONE);
                    isRatingLayoutVisible = false;
                }
                break;
            case R.id.ll_image_view_location:
                if (storeLocation != null) {
                    Utility.initiateGoogleMapsIntent(this, storeLocation);
//                    new OfferReportingTask(new int[]{3, bundle.getInt(Constant.OfferID)}).execute();
                    Home.reportOfferLocation(bundle.getInt(Constant.OfferID));
                } else {
                    Toast.makeText(this, getString(R.string.LocationIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_image_view_call:
                if (storePhoneNumber != null) {
                    Utility.initiateCallIntent(this, storePhoneNumber);
//                    new OfferReportingTask(new int[]{2, bundle.getInt(Constant.OfferID)}).execute();
                    Home.reportOfferCalled(bundle.getInt(Constant.OfferID));
                } else {
                    Toast.makeText(this, getString(R.string.PhoneNumberIsNotAvailable), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void enterAnimation() {
        img.setPivotX(0);
        img.setPivotY(0);
        img.setScaleX(mWidthScale);
        img.setScaleY(mHeightScale);
        img.setTranslationX(mLeftDelta);
        img.setTranslationY(mTopDelta);

        // interpolator where the rate of change starts out quickly and then decelerates.
        TimeInterpolator sDecelerator = new DecelerateInterpolator();

        // Animate scale and translation to go from thumbnail to full size
        img.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
                translationX(0).translationY(0).setInterpolator(sDecelerator);

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    public void exitAnimation(final Runnable endAction) {
        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        img.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(sInterpolator).withEndAction(endAction);

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    @Override
    public void onBackPressed() {
        exitAnimation(() -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // up to down swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    img.startAnimation(animImageViewTransitionUp);
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    img.startAnimation(animImageViewTransitionDown);
                }
            } catch (Exception ignored) {}
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

    }
}
