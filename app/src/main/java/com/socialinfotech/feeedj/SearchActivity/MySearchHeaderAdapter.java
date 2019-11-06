package com.socialinfotech.feeedj.SearchActivity;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Utility;
import com.socialinfotech.feeedj.ParsingModel.FilterSearchModel;
import com.socialinfotech.feeedj.R;

import java.util.ArrayList;


public class MySearchHeaderAdapter extends RecyclerView.Adapter<MySearchHeaderAdapter.ViewHolder> {

    private final ArrayList<JsonObject>  mValues;
    Context mContext;

    public MySearchHeaderAdapter(ArrayList<JsonObject> data, Context cnt) {
        mValues = data;
        mContext=cnt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_search_header_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.sdv_add_iamge.setImageURI(Uri.parse(mValues.get(position).get("imageURL").getAsString()));
        holder.txt_title.setText(mValues.get(position).get("hashtag").getAsString());
        holder.sdv_add_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.search(mContext, holder.txt_title.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView sdv_add_iamge;
        TextView txt_title;
        RelativeLayout llBrochureBadge;
        TextView tvBrochure;
        TextView txt_compnay_name;

        public FilterSearchModel mItem;

        ViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_title);
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            sdv_add_iamge = view.findViewById(R.id.sdv_add_iamge);
            llBrochureBadge = view.findViewById(R.id.rl_brochure_badge);
            tvBrochure = view.findViewById(R.id.tv_brochure_badge);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
