package com.socialinfotech.feeedj.ExploreActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.CompnayTagListingResponce.CompaniesBean;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;
import com.socialinfotech.feeedj.TimeLineActivities.ViewCompanyDetailsActivity;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CategoryFollowViewAdapter extends RecyclerView.Adapter<CategoryFollowViewAdapter.ViewHolder> {

    private final List<CompaniesBean> mValues;
    Context mContext;
    protected FeeedjAPIClass gitsubscribe;
    SharedPreferences sPref;
    public CategoryFollowViewAdapter(List<CompaniesBean> items, Context cnt) {
        sPref = PreferenceManager.getDefaultSharedPreferences(cnt);
        mValues = items;
        mContext=cnt;
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
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FeeedjAPIClass.API).build();
        gitsubscribe = restAdapter1.create(FeeedjAPIClass.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_cateogry_follow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txt_compnay_name.setText(mValues.get(position).getCompanyName_Ar());
        holder.txt_compny_tag.setText(mValues.get(position).getCompanyName());
        holder.sdvImage.setImageURI(Uri.parse(mValues.get(position).getCompanyProfilePhoto()));
        holder.txt_offers_number.setText(mValues.get(position).getNumberOfOffers()+ " عرض");
        if (mValues.get(position).isIsFollowed()){
            holder.chk_followed.setChecked(true);
        }else {
            holder.chk_followed.setChecked(false);
        }
        holder.chk_followed.setOnClickListener(v -> {
            if (((CheckBox)v).isChecked()){
                if (sPref.getString(Constant.ACCESS_TOKEN,"").equals("")){
                    WarningDilaog();
                    holder.chk_followed.setChecked(false);
                }else{
                Subscribe(mValues.get(position).getCompanyId(),position);
                }
            }else{
                UnSubscribe(mValues.get(position).getCompanyId(),position);

            }
        });
        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,ViewCompanyDetailsActivity.class);
            intent.putExtra(Constant.ToolbarTitle,mValues.get(position).getCompanyName_Ar());
            intent.putExtra(Constant.COMPANY_ID,mValues.get(position).getCompanyId());


            intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues.get(position).getCompanyProfilePhoto());
            intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues.get(position).getCompanyName_Ar());
            intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues.get(position).getCompanyName());

            intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues.get(position).getPhoneNumber());

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    private void WarningDilaog() {
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compnay_name;
        public final TextView txt_compny_tag;
        public final TextView txt_offers_number;
        public CompaniesBean mItem;
        CheckBox chk_followed;
        SimpleDraweeView sdvImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            txt_compny_tag = view.findViewById(R.id.txt_compny_tag);
            txt_offers_number = view.findViewById(R.id.txt_offers_number);
            sdvImage = view.findViewById(R.id.sdvImage);
            chk_followed = view.findViewById(R.id.chk_followed);
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }

    private void Subscribe(int companyID, final int pos) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> forgotPasswordResponseCall = gitsubscribe.Subscribe(String.valueOf(companyID));
        forgotPasswordResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues.get(pos).setIsFollowed(true);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "حدثت مشكلة اثناء الغاء متابعة الشركة، حاول لاحقاً", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
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
                    mValues.get(pos1).setIsFollowed(false);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}
