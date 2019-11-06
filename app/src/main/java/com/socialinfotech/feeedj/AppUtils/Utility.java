package com.socialinfotech.feeedj.AppUtils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.SearchActivity.SearchResultActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Social Infotech on 6/13/2016.
 */
public class Utility {

    public static boolean brochureInitialized;

    public static void shareImage(final String offerTitle, String imageURI, final Context context) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imageURI))
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
            }

            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (dataSource.isFinished() && bitmap != null) {
                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                    bitmap, "Image Description", null);
                            Uri bmpUri = Uri.parse(path);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, offerTitle + "\n\n\n" + "حمّل تطبيق فيييدز وشوف عروض السوق اللي تهمك!\uD83D\uDE0D \n\n http://onelink.to/feeedz\n");
                            shareIntent.setType("image/*");

                            context.startActivity(Intent.createChooser(shareIntent, "شارك العرض"));
                        } else {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            Toast.makeText(context, "لازم تعطي صلاحيات للتطبيق علشان تقدر تستفيد من هالخاصية", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "لازم تعطي صلاحيات للتطبيق علشان تقدر تستفيد من هالخاصية", Toast.LENGTH_SHORT).show();
                    }
                }
                dataSource.close();
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static void WarningDialog(final Context mContext) {
        Dialog dialog = new Dialog(mContext);
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
            mContext.startActivity(new Intent(mContext, SplashActivity.class));
            //mContext.finish();
            finalDialog.dismiss();
        });
        dialog.show();
    }

    public static void ShowDislikeDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_dislike);
        TextView txt_road = dialog.findViewById(R.id.txt);
        final Dialog finalDialog = dialog;
        txt_road.setOnClickListener(v -> finalDialog.dismiss());
        dialog.show();
    }

    public static void ShowlikeDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_like);
        TextView txt_road = dialog.findViewById(R.id.txt);
        final Dialog finalDialog = dialog;
        txt_road.setOnClickListener(v -> finalDialog.dismiss());
        dialog.show();
    }

    public static void initiateCallIntent(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void initiateGoogleMapsIntent(Context context, String location) {
        if (isGoogleMapsInstalled(context)) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Map غير مثبت", Toast.LENGTH_SHORT).show();
        }
    }

    public static void initiateEmailIntent(Context context, String address, String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + address));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text
        context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }

    public static void initiateURLIntent(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }


    private static boolean isGoogleMapsInstalled(Context c) {
        try {
            ApplicationInfo info = c.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String dateConvert(String dat) {
        String s2 = "";
        Date d = null;
        try {
            d = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(dat);
            s2 = (new SimpleDateFormat("yyyy-MM-dd")).format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s2;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static HttpURLConnection openConnectionForUrl(String path, String RequestType, String userToken)
            throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(RequestType);
        connection.setRequestProperty("charset", "utf-8");
        if (userToken != null) {
            connection.setRequestProperty("Authorization", "Token " + userToken);
        }
        return connection;
    }


    public static void diableCompanyNotificationsDialog(
            Context context, String title, String positiveButtonText,
            String negativeButtonText, final Runnable listenerYes, final Runnable listenerNo) {

        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_disable_company_notifications);

        TextView titleText = dialog.findViewById(R.id.text_title);
        titleText.setText(title);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setText(positiveButtonText);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
        btn_cnl.setText(negativeButtonText);

        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(v -> {
            listenerYes.run();
            finalDialog.dismiss();
        });
        btn_cnl.setOnClickListener(v -> {
            listenerNo.run();
            finalDialog.dismiss();
        });
        dialog.show();
    }


    public static void hideSoftKeybaord(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void search(Context context, String searchTerm) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("SearchTerm", searchTerm);
        context.startActivity(intent);
    }

    public static String getCategoryTitleByID(int companyId) {
        String title = null;
        switch (companyId) {
            case 1:
                title = "الاتصالات";
                break;
            case 2:
                title = "الطيران";
                break;
            case 3:
                title = "البنوك";
                break;
            case 4:
                title = "الإلكترونيات";
                break;
            case 5:
                title = "السفر";
                break;
            case 6:
                title = "السيارات";
                break;
            case 7:
                title = "المطاعم";
                break;
            case 8:
                title = "السوبرماركت";
                break;
            case 9:
                title = "المفروشات";
                break;
            case 10:
                title = "الملابس";
                break;
            case 11:
                title = "الصحة";
                break;
            case 12:
                title = "أشياء ثانية";
                break;
        }
        return title;
    }


}
