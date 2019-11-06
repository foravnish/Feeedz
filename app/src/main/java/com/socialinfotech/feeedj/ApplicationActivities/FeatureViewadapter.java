package com.socialinfotech.feeedj.ApplicationActivities;

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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeaturedResponse;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeatureViewadapter extends RecyclerView.Adapter<FeatureViewadapter.ViewHolder> {

    private final FeaturedResponse[] mValues;
    Context mContext;
    protected FeeedjAPIClass gitsubscribe;
    SharedPreferences sPref;

    public FeatureViewadapter(FeaturedResponse[] getAllOffersResponces, Context cnt) {
        mValues = getAllOffersResponces;
        mContext = cnt;
        sPref = PreferenceManager.getDefaultSharedPreferences(cnt);
//        RestAdapter restAdapter1 = new RestAdapter.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//                        String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
//                        String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
//                        if (api_key.length() > 0) {
//                            request.addHeader("Authorization", api_barear+" "+api_key);
//                        }
//                    }
//                })
//                .setEndpoint(FeeedjAPIClass.API).build();

        Interceptor interceptor = chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
            String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
            if (api_key.length() > 0) {
                requestBuilder.addHeader("Authorization", api_barear + " " + api_key);
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit restAdapter1 = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(FeeedjAPIClass.API).build();

        gitsubscribe = restAdapter1.create(FeeedjAPIClass.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_user_featured, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues[position];
        holder.sdv_add_iamge.setImageURI(Uri.parse(mValues[position].getCompanyProfilePhoto()));
        holder.txt_title.setText(mValues[position].getCompanyName_Ar());
        holder.txt_compnay_name.setText(mValues[position].getCompanyName());
        holder.txt_offers_number.setText(mValues[position].getNumberOfOffers()+ " عرض");
        if (mValues[position].isIsFollowed()){
            holder.chk_follow.setChecked(true);
        }else{
            holder.chk_follow.setChecked(false);
        }
        holder.sdv_add_iamge.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,ViewCompanyDetailsActivity.class);
            intent.putExtra(Constant.ToolbarTitle,mValues[position].getCompanyName_Ar());
            intent.putExtra(Constant.COMPANY_ID,mValues[position].getCompanyId());


            intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position].getCompanyProfilePhoto());
            intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position].getCompanyName_Ar());
            intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position].getCompanyName());

            mContext.startActivity(intent);
            Log.e("adasd",mValues[position].getCompanyId()+"");
        });
        holder.chk_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                        Subscribe(mValues[position].getCompanyId(),position);
                } else {
                    UnSubscribe(mValues[position].getCompanyId(),position);
                }
            }
        });
    }
    private void Subscribe(int companyID, final int pos) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> forgotPasswordResponseCall = gitsubscribe.Subscribe(String.valueOf(companyID));
        forgotPasswordResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues[pos].setIsFollowed(true);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "حدثت مشكلة اثناء الغاء متابعة الشركة، حاول لاحقاً", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void UnSubscribe(int companyID, final int pos1) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> forgotPasswordResponseCall = gitsubscribe.UnSubscribe(String.valueOf(companyID));
        forgotPasswordResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues[pos1].setIsFollowed(false);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdv_add_iamge;
        TextView txt_title;
        TextView txt_compnay_name;
        TextView txt_offers_number;
        CheckBox chk_follow;
        public FeaturedResponse mItem;

        public ViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_compny_tag);
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            txt_offers_number = view.findViewById(R.id.txt_offers_number);
            sdv_add_iamge = view.findViewById(R.id.sdvImage);
            chk_follow = view.findViewById(R.id.chk_follow);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
