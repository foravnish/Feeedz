package com.socialinfotech.feeedj.TimeLineActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.R;

import java.util.List;

public class Pager extends PagerAdapter {

    List<GetAllOffersResponse> views;
    List<GetAllOffersResponse.OffersImagesBeans> imagesBeans;
    Activity context;
    String img;
    String htmlImage;
    GetAllOffersResponse.CompanyBean companyBean;
    LayoutInflater mLayoutInflater;

    public Pager(List<GetAllOffersResponse> views, Activity context, List<GetAllOffersResponse.OffersImagesBeans> imagesBeans, String img, String htmlImage, GetAllOffersResponse.CompanyBean companyBean) {
        this.views = views;
        this.imagesBeans = imagesBeans;
        this.context = context;
        this.img=img;
        this.htmlImage=htmlImage;
        this.companyBean=companyBean;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return imagesBeans.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);

        final SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_add_iamge);


//        Display display = context.getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int screenWidth = size.x;
//        int screenHeight = size.y;

        if (views.get(position ).getMultiple()) {
            imageView.setImageURI(Uri.parse(imagesBeans.get(position).getImage()));
        }else{
            imageView.setImageURI(Uri.parse(img));
        }

        if (imagesBeans.get(position).getCoord() != null) {
            String[] sImageDimensions = imagesBeans.get(position).getCoord().split("x");
            if (Integer.parseInt(sImageDimensions[1]) >600 && Integer.parseInt(sImageDimensions[1]) <=698) {
                imageView.setAspectRatio(1);
            }
            else if (Integer.parseInt(sImageDimensions[1]) > 500 && Integer.parseInt(sImageDimensions[1]) <=698) {
                imageView.setAspectRatio((float)1.3);
            }
            else if (Integer.parseInt(sImageDimensions[1]) ==699) {
                imageView.setAspectRatio(1);
            }
           else if (Integer.parseInt(sImageDimensions[0]) > Integer.parseInt(sImageDimensions[1])) {
               imageView.setAspectRatio((float)1.8);
            }else {
                imageView.setAspectRatio(1);
            }
        } else{
            imageView.setAspectRatio(1);
        }


        imageView.setOnClickListener(v -> {
            Log.e("co-ords", "" + imagesBeans.get(position).getImage());
            Log.e("co-ords", "" + imagesBeans.get(position).getCoord());

            if (views.get(position ).getMultiple()) {

                String str = htmlImage;
//                if (str == null) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra(Constant.ImageNAme, imagesBeans.get(position).getImage());
                intent.putExtra(Constant.TextTile, views.get(position).getOfferTitle());
                intent.putExtra(Constant.OfferLocation, views.get(position).getOfferLocation());
                intent.putExtra(Constant.PhoneNumber, views.get(position).getPhoneNumber());
                intent.putExtra(Constant.OfferID, views.get(position).getOfferId());
                intent.putExtra(Constant.OFFER_RATING_STATUS, views.get(position).getOfferRating());
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight());
                context.startActivity(intent);

//                } else {
//                    Intent intent = new Intent(context, PDFViewActivity.class);
//                    intent.putExtra("OFFER_ID", views.get(position ).getOfferId());
//                    intent.putExtra("OFFER_IMAGE", imagesBeans.get(position).getImage());
//                    intent.putExtra("BROCHURE_PDF_URL", htmlImage);
//                    intent.putExtra("BROCHURE_PDF_TITLE", companyBean.getCompanyName_Ar());
//                    intent.putExtra("COMPANY_NAME", companyBean.getCompanyName_Ar());
//                    intent.putExtra("COMPANY_USER_NAME", companyBean.getCompanyName());
//                    intent.putExtra("COMPANY_VERIFIED", companyBean.isCompanyVerified());
//                    intent.putExtra("COMPANY_LOCATION", views.get(position ).getOfferLocation());
//                    intent.putExtra("COMPANY_PHONE_NUMBER", views.get(position ).getPhoneNumber());
//                    intent.putExtra("OFFER_END_TYPE", views.get(position ).getOfferEndType());
//                    intent.putExtra(Constant.COMPANY_ID, views.get(position ).getCompanyId());
//                    intent.putExtra("COMPANY_TAG_ID", views.get(position).getCompanyTagId());
//                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, companyBean.getCompanyProfilePhoto());
//                    intent.putExtra("OFFER_TIME_END", views.get(position ).getOfferTimeEnd());
//                    context.startActivity(intent);
//                }
            }else{
                String str = htmlImage;
                if (str == null) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra(Constant.ImageNAme, img);
                intent.putExtra(Constant.TextTile, views.get(position).getOfferTitle());
                intent.putExtra(Constant.OfferLocation, views.get(position).getOfferLocation());
                intent.putExtra(Constant.PhoneNumber, views.get(position).getPhoneNumber());
                intent.putExtra(Constant.OfferID, views.get(position).getOfferId());
                intent.putExtra(Constant.OFFER_RATING_STATUS, views.get(position).getOfferRating());
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight());
                context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PDFViewActivity.class);
                    intent.putExtra("OFFER_ID", views.get(position ).getOfferId());
                    intent.putExtra("OFFER_IMAGE", imagesBeans.get(position).getImage());
                    intent.putExtra("BROCHURE_PDF_URL", htmlImage);
                    intent.putExtra("BROCHURE_PDF_TITLE", companyBean.getCompanyName_Ar());
                    intent.putExtra("COMPANY_NAME", companyBean.getCompanyName_Ar());
                    intent.putExtra("COMPANY_USER_NAME", companyBean.getCompanyName());
                    intent.putExtra("COMPANY_VERIFIED", companyBean.isCompanyVerified());
                    intent.putExtra("COMPANY_LOCATION", views.get(position ).getOfferLocation());
                    intent.putExtra("COMPANY_PHONE_NUMBER", views.get(position ).getPhoneNumber());
                    intent.putExtra("OFFER_END_TYPE", views.get(position ).getOfferEndType());
                    intent.putExtra(Constant.COMPANY_ID, views.get(position ).getCompanyId());
                    intent.putExtra("COMPANY_TAG_ID", views.get(position).getCompanyTagId());
                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, companyBean.getCompanyProfilePhoto());
                    intent.putExtra("OFFER_TIME_END", views.get(position ).getOfferTimeEnd());
                    context.startActivity(intent);
                }
            }
        });

        container.addView(itemView);
        return itemView;
    }


    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }


}
