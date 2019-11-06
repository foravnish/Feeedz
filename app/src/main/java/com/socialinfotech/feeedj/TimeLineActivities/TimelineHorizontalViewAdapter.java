package com.socialinfotech.feeedj.TimeLineActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.ParsingModel.CompnayTagListingResponce;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fi8er1 on 03/07/2017.
 */

public class TimelineHorizontalViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<CompnayTagListingResponce.CompaniesBean> mValues;
    Context mContext;
    protected FeeedjAPIClass gitsubscribe;
    SharedPreferences sPref;
    final int VIEW_TYPE_DATA = 0;
    final int VIEW_TYPE_VIEW_MORE_BUTTON = 1;
    boolean showViewMoreButton;

    public TimelineHorizontalViewAdapter(List<CompnayTagListingResponce.CompaniesBean> items, Context cnt) {
        mContext=cnt;
        sPref = PreferenceManager.getDefaultSharedPreferences(cnt);
        if (items.size() > 3) {
            showViewMoreButton = true;
        }
        mValues = items;
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
//        gitsubscribe = restAdapter1.create(FeeedjAPIClass.class);


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                String api_key = sPref.getString(Constant.ACCESS_TOKEN, "");
                String api_barear = sPref.getString(Constant.TOKEN_TYPE, "");
                if (api_key.length() > 0) {
                    requestBuilder.addHeader("Authorization", api_barear + " " + api_key);
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
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
    public int getItemViewType (int position) {
        if (position == 3 && showViewMoreButton) {
            return VIEW_TYPE_VIEW_MORE_BUTTON;
        }
        return VIEW_TYPE_DATA;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAG", "run");
        if (viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.raw_horizontal_following_item, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_VIEW_MORE_BUTTON) {
            Log.i("TAG", "more");
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.raw_layout_timeline_horizontal_view_more_button, parent, false);
            return new ViewMoreButtonHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_DATA:
                final ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.mItem = mValues.get(position);
                viewHolder.txt_compnay_name.setText(mValues.get(position).getCompanyName_Ar());
                viewHolder.txt_compny_tag.setText(mValues.get(position).getCompanyName());
                viewHolder.sdvImage.setImageURI(Uri.parse(mValues.get(position).getCompanyProfilePhoto()));
                if (mValues.get(position).isIsFollowed()) {
                    viewHolder.chk_followed.setChecked(true);
                } else {
                    viewHolder.chk_followed.setChecked(false);
                }

                viewHolder.chk_followed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                                WarningDilaog();
                                viewHolder.chk_followed.setChecked(false);
                            } else {
                                Subscribe(mValues.get(position).getCompanyId(), position);
                            }
                        } else {
                            UnSubscribe(mValues.get(position).getCompanyId(), position);
                        }
                    }
                });

                viewHolder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, mValues.get(position).getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_ID, mValues.get(position).getCompanyId());


                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues.get(position).getCompanyProfilePhoto());
                    intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues.get(position).getCompanyName_Ar());
                    intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues.get(position).getCompanyName());
                    intent.putExtra(Constant.COMPANY_PROFILE_PHONE, mValues.get(position).getPhoneNumber());
                    mContext.startActivity(intent);
                });

            break;
            case VIEW_TYPE_VIEW_MORE_BUTTON:
                final ViewMoreButtonHolder viewHolderTwo = (ViewMoreButtonHolder) holder;
                viewHolderTwo.rlViewMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showViewMoreButton = false;
                        notifyDataSetChanged();
                    }
                });
                break;
            }
    }

    @Override
    public int getItemCount() {
        int itemsCount;
        if (showViewMoreButton) {
            itemsCount = 4;
        } else {
            itemsCount = mValues.size();
        }
        return itemsCount;
    }

    private void WarningDilaog() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_login_warning);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                //startActivity(new Intent(ViewOfferActivity.this, MainActivity.class));
                //finish();
            }
        });
        btn_cnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                mContext.startActivity(new Intent(mContext, SplashActivity.class));
                //mContext.finish();
                finalDialog.dismiss();
            }
        });
        dialog.show();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compnay_name;
        public final TextView txt_compny_tag;
        public CompnayTagListingResponce.CompaniesBean mItem;
        CheckBox chk_followed;
        SimpleDraweeView sdvImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txt_compnay_name = view.findViewById(R.id.txt_compnay_name);
            txt_compny_tag = view.findViewById(R.id.txt_compny_tag);
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
        Call<Void> callForgotPasswordResponse = gitsubscribe.Subscribe(String.valueOf(companyID));
        callForgotPasswordResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues.get(pos).setIsFollowed(true);
                    notifyDataSetChanged();
                }else {
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
        Call<Void> callForgotPasswordResponse = gitsubscribe.UnSubscribe(String.valueOf(companyID));
        callForgotPasswordResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues.get(pos1).setIsFollowed(false);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private class ViewMoreButtonHolder extends RecyclerView.ViewHolder {

        final View mView;
        RelativeLayout rlViewMore;

        ViewMoreButtonHolder(View view) {
            super(view);
            mView = view;
            rlViewMore = view.findViewById(R.id.rl_timeline_horizontal_companies_list_view_more);
        }

    }
}
