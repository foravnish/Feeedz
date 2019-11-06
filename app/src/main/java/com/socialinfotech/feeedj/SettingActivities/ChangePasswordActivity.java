package com.socialinfotech.feeedj.SettingActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {

    @Bind(R.id.et_old)
    EditText etOld;
    @Bind(R.id.et_new_pass)
    EditText etNewPass;
    @Bind(R.id.et_cnf_pass)
    EditText etCnfPass;
    ProgressDialog pDilog;
    String passwordOld, passwordNew, passwordRepeat;
    protected FeeedjAPIClass git;

    SharedPreferences Spref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        Spref = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this);
        editor = Spref.edit();

        Amplitude.getInstance().logEvent("Change Password");


        Interceptor interceptor = chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            String api_key = Spref.getString(Constant.ACCESS_TOKEN, "");
            String api_barear = Spref.getString(Constant.TOKEN_TYPE, "");
            if (api_key.length() > 0) {
                requestBuilder.addHeader("Authorization", api_barear + " " + api_key);
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit restAdapter = new Retrofit.Builder()
                .client(okHttpClient).baseUrl(FeeedjAPIClass.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        git = restAdapter.create(FeeedjAPIClass.class);

        pDilog = new ProgressDialog(ChangePasswordActivity.this, R.style.MyTheme);
        pDilog.setCancelable(false);
        pDilog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    @OnClick({R.id.btn_setting, R.id.btn_change_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                onBackPressed();
                break;
            case R.id.btn_change_password:

                passwordOld = etOld.getText().toString();
                passwordNew = etNewPass.getText().toString();
                passwordRepeat = etCnfPass.getText().toString();

                if (validatePasswordChangeInfo()) {
                    changePassword();
                }
                break;
        }
    }

    public boolean validatePasswordChangeInfo() {
        boolean valid = true;

        if (passwordOld.trim().isEmpty() || passwordOld.length() < 6) {
            etOld.setError("فضلاً، كلمة المرور يجب أن لاتقل عن 6 أحرف");
            valid = false;
        } else {
            etOld.setError(null);
        }

        if (passwordNew.trim().isEmpty() || passwordNew.length() < 6) {
            etNewPass.setError("فضلاً، كلمة المرور يجب أن لاتقل عن 6 أحرف");
            valid = false;
        } else {
            etNewPass.setError(null);
        }

        if (!passwordNew.equals(passwordRepeat)) {
            etCnfPass.setError("كلمة المرور الجديدة غير متطابقة");
            valid = false;
        } else {
            etCnfPass.setError(null);
        }
        return valid;
    }

    private void changePassword() {
        pDilog.show();
        JsonObject obj = new JsonObject();
        obj.addProperty("oldPassword", passwordOld);
        obj.addProperty("newPassword", passwordNew);
        obj.addProperty("confirmPassword", passwordRepeat);

//        Toast.makeText(ForgotPasswordActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();

        Call<ResponseBody> forgotPasswordResponseCall = git.ChangePassword(obj);
        forgotPasswordResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDilog.dismiss();
                Log.e("passwordChangeResponse", "" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "تم تغيير كلمة المرور بنجاح", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDilog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
