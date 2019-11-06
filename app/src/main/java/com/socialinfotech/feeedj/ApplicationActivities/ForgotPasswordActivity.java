package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.R;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.et_emailid)
    EditText etEmailid;
    protected FeeedjAPIClass git;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    ProgressDialog pDilog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        sPref = PreferenceManager.getDefaultSharedPreferences(ForgotPasswordActivity.this);
        editor = sPref.edit();
        Retrofit restAdapter = new Retrofit.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//
//                            request.addHeader("Content-Type", "application/json");
//
//                    }
//                })

                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FeeedjAPIClass.API).build();

        git = restAdapter.create(FeeedjAPIClass.class);

        pDilog = new ProgressDialog(ForgotPasswordActivity.this,R.style.MyTheme);
        pDilog.setCancelable(false);
        pDilog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        Amplitude.getInstance().logEvent("Forgot Password");
    }

    @OnClick({R.id.btn_back, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_login:
                if (etEmailid.getText().toString().length()>0) {
                    if (validateEmail(etEmailid.getText().toString().trim())){
                        ForgotPassword();
                        // your code
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this, "ارجع عيد كتابة الايميل لان فيه شي غلطط ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "لازم تعبي كل المتطلبات عشان نقدر نسجلك", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private boolean validateEmail(String data) {
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(data);
        return emailMatcher.matches();
    }

    private void ForgotPassword() {
        pDilog.show();
        JsonObject obj = new JsonObject();
        obj.addProperty("email", etEmailid.getText().toString());

//        Toast.makeText(ForgotPasswordActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();

        Call<ResponseBody> forgotPasswordResponseCall = git.ForgotPassword(obj);
        forgotPasswordResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDilog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "ارسلنالك ايميل فيه باسوورد جديد ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "ارجع عيد كتابة الايميل لان فيه شي غلط ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDilog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
