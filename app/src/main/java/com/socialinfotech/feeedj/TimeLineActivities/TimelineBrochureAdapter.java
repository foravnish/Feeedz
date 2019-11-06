package com.socialinfotech.feeedj.TimeLineActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.PDFViewActivity;
import com.socialinfotech.feeedj.ParsingModel.FilterSearchModel;
import com.socialinfotech.feeedj.ParsingModel.GetAllOffersResponse;
import com.socialinfotech.feeedj.R;

import java.util.ArrayList;

/**
 * Created by fi8er1 on 27/10/2016.
 */

public class TimelineBrochureAdapter extends RecyclerView.Adapter<TimelineBrochureAdapter.ViewHolder> {

    private final ArrayList<GetAllOffersResponse> mValues;
    Context mContext;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    TimelineBrochureAdapter(ArrayList<GetAllOffersResponse> responses, Context cnt) {
        mContext = cnt;
        sPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sPref.edit();
        mValues = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {
            if (isTimelineHeaderViewed(String.valueOf(responses.get(i).getOfferId()))) {
                mValues.add(responses.get(i));
            }
        }
        for (int i = 0; i < responses.size(); i++) {
            if (!isTimelineHeaderViewed(String.valueOf(responses.get(i).getOfferId()))) {
                mValues.add(responses.get(i));
            }
        }
    }

    @Override
    public TimelineBrochureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_timeline_brochure_items, parent, false);
        return new TimelineBrochureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimelineBrochureAdapter.ViewHolder holder, final int position) {
        holder.sdv_add_image.setImageURI(Uri.parse(mValues.get(position).getCompany().getCompanyProfilePhoto()));
        holder.txt_title.setText(mValues.get(position).getCompany().getCompanyName_Ar());

        if (isTimelineHeaderViewed(String.valueOf(mValues.get(position).getOfferId()))) {
            holder.sdv_add_image.setBackgroundResource(R.mipmap.bdr_brochure_pic_viewed);
        } else {
            holder.sdv_add_image.setBackgroundResource(R.mipmap.bdr_brochure_pic_un_viewed);
        }

        String brochureBadgeText = mValues.get(position).getBrochureBadge();
        if (brochureBadgeText == null || brochureBadgeText.trim().isEmpty()) {
            holder.rlBrochureBadge.setVisibility(View.INVISIBLE);
        } else {
            holder.rlBrochureBadge.setVisibility(View.VISIBLE);
            holder.tvBrochure.setText(brochureBadgeText);
        }

        holder.sdv_add_image.setOnClickListener(v -> {
            holder.sdv_add_image.setBackgroundResource(R.mipmap.bdr_brochure_pic_viewed);
            Intent intent = new Intent(mContext, PDFViewActivity.class);
            Log.e("companyID", "" + mValues.get(position).getCompany().getCompanyId());
            intent.putExtra("OFFER_ID", mValues.get(position).getOfferId());
            intent.putExtra("OFFER_IMAGE", mValues.get(position).getOfferImage());
            intent.putExtra("BROCHURE_PDF_URL", mValues.get(position).getAttachmentHTML());
            intent.putExtra("BROCHURE_PDF_TITLE", mValues.get(position).getCompany().getCompanyName_Ar());
            intent.putExtra("COMPANY_NAME", mValues.get(position).getCompany().getCompanyName_Ar());
            intent.putExtra("COMPANY_USER_NAME", mValues.get(position).getCompany().getCompanyName());
            intent.putExtra("COMPANY_VERIFIED", mValues.get(position).getCompany().isCompanyVerified());
            intent.putExtra("COMPANY_LOCATION", mValues.get(position).getOfferLocation());
            intent.putExtra("COMPANY_PHONE_NUMBER", mValues.get(position).getPhoneNumber());
            intent.putExtra("OFFER_END_TYPE", mValues.get(position).getOfferEndType());
            intent.putExtra("COMPANY_ID", mValues.get(position).getCompanyId());
            intent.putExtra("COMPANY_TAG_ID", mValues.get(position).getCompanyTagId());
            intent.putExtra("COMPANY_PROFILE_PHOTO", mValues.get(position).getCompany().getCompanyProfilePhoto());
            intent.putExtra("OFFER_TIME_END", mValues.get(position).getOfferTimeEnd());
            if (!isTimelineHeaderViewed(String.valueOf(mValues.get(position).getOfferId()))) {
            putTimelineHeaderViewed(String.valueOf(mValues.get(position).getOfferId()));
    }
            notifyDataSetChanged();
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv_add_image;
        RelativeLayout rlBrochureBadge;
        TextView tvBrochure;
        TextView txt_title;
        public FilterSearchModel mItem;
        public ViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_title);
            sdv_add_image = view.findViewById(R.id.sdv_add_iamge);
            rlBrochureBadge = view.findViewById(R.id.rl_brochure_badge);
            tvBrochure = view.findViewById(R.id.tv_brochure_badge);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }

    private boolean isTimelineHeaderViewed(String offerID) {
        String dataFromSharedPreferences = sPref.getString(Constant.TIMELINE_HEADERS_VIEWED, "");
        if (dataFromSharedPreferences.equalsIgnoreCase("")) {
            return false;
        }
        String[] mainArrayFromPreferences = dataFromSharedPreferences.split(",");
        for (String mainArrayFromPreference : mainArrayFromPreferences) {
            String[] childArrayFromPreferences = mainArrayFromPreference.split(":");
            if (childArrayFromPreferences[0].equalsIgnoreCase(offerID) && Boolean.parseBoolean(childArrayFromPreferences[1])) {
                return true;
            }
        }
        return false;
    }

    private void putTimelineHeaderViewed(String offerID) {
        String dataFromSharedPreferences = sPref.getString(Constant.TIMELINE_HEADERS_VIEWED, "");
        dataFromSharedPreferences = dataFromSharedPreferences + offerID + ":true,";
        editor.putString(Constant.TIMELINE_HEADERS_VIEWED, dataFromSharedPreferences).apply();
    }
}
