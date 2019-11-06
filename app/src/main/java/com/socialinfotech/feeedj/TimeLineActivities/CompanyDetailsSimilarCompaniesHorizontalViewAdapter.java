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
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.GetSimilarCompaniesResponse;
import com.socialinfotech.feeedj.R;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompanyDetailsSimilarCompaniesHorizontalViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final GetSimilarCompaniesResponse[] mValues;
    Context mContext;
    protected FeeedjAPIClass gitsubscribe;
    SharedPreferences sPref;
    final int VIEW_TYPE_DATA = 0;
    final int VIEW_TYPE_VIEW_MORE_BUTTON = 1;
    boolean showViewMoreButton;

    public CompanyDetailsSimilarCompaniesHorizontalViewAdapter(GetSimilarCompaniesResponse[] items, Context cnt) {
        mContext=cnt;
        sPref = PreferenceManager.getDefaultSharedPreferences(cnt);
        if (items.length > 3) {
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
    public int getItemViewType (int position) {
        if (position == 4 && showViewMoreButton) {
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
            return new CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_VIEW_MORE_BUTTON) {
            Log.i("TAG", "more");
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.raw_layout_timeline_horizontal_view_more_button, parent, false);
            return new CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewMoreButtonHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_DATA:
                final CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewHolder viewHolder = (CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewHolder) holder;
                viewHolder.mItem = mValues[position];
                viewHolder.txt_compnay_name.setText(mValues[position].getCompanyNameAr());
                viewHolder.txt_compny_tag.setText(mValues[position].getCompanyName());
                viewHolder.sdvImage.setImageURI(Uri.parse(mValues[position].getCompanyProfilePhoto()));
                if (mValues[position].getIsFollowed()) {
                    viewHolder.chk_followed.setChecked(true);
                } else {
                    viewHolder.chk_followed.setChecked(false);
                }

                viewHolder.chk_followed.setOnClickListener(v -> {
                    if (((CheckBox) v).isChecked()) {
                        if (sPref.getString(Constant.ACCESS_TOKEN, "").equals("")) {
                            WarningDialog();
                            viewHolder.chk_followed.setChecked(false);
                        } else {
                            Subscribe(mValues[position].getCompanyId(), position);
                        }
                    } else {
                        UnSubscribe(mValues[position].getCompanyId(), position);
                    }
                });

                viewHolder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, ViewCompanyDetailsActivity.class);
                    intent.putExtra(Constant.ToolbarTitle, mValues[position].getCompanyNameAr());
                    intent.putExtra(Constant.COMPANY_ID, mValues[position].getCompanyId());


                    intent.putExtra(Constant.COMPANY_PROFILE_PHOTO, mValues[position].getCompanyProfilePhoto());
                    intent.putExtra(Constant.COMPANY_PROFILE_NAME, mValues[position].getCompanyNameAr());
                    intent.putExtra(Constant.COMPANY_PROFILE_TAG, mValues[position].getCompanyName());

                    mContext.startActivity(intent);
                });

                break;
            case VIEW_TYPE_VIEW_MORE_BUTTON:
                final CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewMoreButtonHolder viewHolderTwo = (CompanyDetailsSimilarCompaniesHorizontalViewAdapter.ViewMoreButtonHolder) holder;
                viewHolderTwo.rlViewMore.setOnClickListener(view -> {
                    showViewMoreButton = false;
                    notifyDataSetChanged();
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        int itemsCount;
        if (showViewMoreButton) {
            itemsCount = 5;
        } else {
            itemsCount = mValues.length;
        }
        return itemsCount;
    }

    private void WarningDialog() {
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

    private class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txt_compnay_name;
        public final TextView txt_compny_tag;
        public GetSimilarCompaniesResponse mItem;
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

    public void Subscribe(int companyID, final int pos) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CompanyId", companyID);
        Call<Void> callForgotPasswordResponse = gitsubscribe.Subscribe(String.valueOf(companyID));
        callForgotPasswordResponse.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mValues[pos].setIsFollowed(true);
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
                    mValues[pos1].setIsFollowed(false);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) { }
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
