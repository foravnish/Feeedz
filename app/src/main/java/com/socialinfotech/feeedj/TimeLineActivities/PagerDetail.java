package com.socialinfotech.feeedj.TimeLineActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.ImageViewActivity;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.ParsingModel.GetCompnayDetailResponse;
import com.socialinfotech.feeedj.R;

import java.util.List;

public class PagerDetail extends PagerAdapter {

    List<GetCompnayDetailResponse> views;
    List<GetCompnayDetailResponse.OffersBean.OffersImagesBeansDetails> imagesBeans;
    Context context;
    LayoutInflater mLayoutInflater;

    public PagerDetail(List<GetCompnayDetailResponse> views, Context context, List<GetCompnayDetailResponse.OffersBean.OffersImagesBeansDetails> imagesBeans) {
        this.views = views;
        this.imagesBeans = imagesBeans;
        this.context = context;
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
        imageView.setImageURI(Uri.parse(imagesBeans.get(position).getImage()));

        if (imagesBeans.get(position ).getCoord() != null) {
            String[] sImageDimensions = imagesBeans.get(position ).getCoord().split("x");
            if (Integer.parseInt(sImageDimensions[0]) < Integer.parseInt(sImageDimensions[1])) {
                imageView.setAspectRatio(1);
            } else {
                imageView.setAspectRatio((float) Integer.parseInt(sImageDimensions[0]) / (float) Integer.parseInt(sImageDimensions[1]));
            }
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("co-ords", "" + imagesBeans.get(position).getCoord());
//                String str = views.get(position ).getAttachmentHTML();
//                if (str == null) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra(Constant.ImageNAme, imagesBeans.get(position).getImage());
//                intent.putExtra(Constant.TextTile, views.get(position ).getOfferTitle());
//                intent.putExtra(Constant.OfferLocation, views.get(position ).getOfferLocation());
               // intent.putExtra(Constant.PhoneNumber, views.get(position ).getPhoneNumber());
//                intent.putExtra(Constant.OfferID, views.get(position ).getOfferId());
//                intent.putExtra(Constant.OFFER_RATING_STATUS, views.get(position ).getOfferRating());
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
//                    intent.putExtra("OFFER_IMAGE", views.get(position ).getOfferImage());
//                    intent.putExtra("BROCHURE_PDF_URL", views.get(position ).getAttachmentHTML());
//                    intent.putExtra("BROCHURE_PDF_TITLE", views.get(position ).getCompany().getCompanyName_Ar());
//                    intent.putExtra("COMPANY_NAME", views.get(position ).getCompany().getCompanyName_Ar());
//                    intent.putExtra("COMPANY_USER_NAME", views.get(position).getCompany().getCompanyName());
//                    intent.putExtra("COMPANY_VERIFIED", views.get(position ).getCompany().isCompanyVerified());
//                    intent.putExtra("COMPANY_LOCATION", views.get(position ).getOfferLocation());
//                    intent.putExtra("COMPANY_PHONE_NUMBER", views.get(position ).getPhoneNumber());
//                    intent.putExtra("OFFER_END_TYPE", views.get(position ).getOfferEndType());
//                    intent.putExtra(Constant.COMPANY_ID, views.get(position ).getCompanyId());
//                    intent.putExtra("COMPANY_TAG_ID", views.get(position).getCompanyTagId());
//                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, views.get(position ).getCompany().getCompanyProfilePhoto());
//                    intent.putExtra("OFFER_TIME_END", views.get(position ).getOfferTimeEnd());
//                    context.startActivity(intent);
//                }
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


