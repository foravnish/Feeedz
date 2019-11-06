package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.RegistrationResponse;
import com.socialinfotech.feeedj.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.et_emailid)
    EditText etEmailid;
    @Bind(R.id.et_password)
    EditText etPassword;

    protected FeeedjAPIClass git;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    ProgressDialog pDilog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Login");

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = sPref.edit();
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(FeeedjAPIClass.API)
                .addConverterFactory(GsonConverterFactory.create()).build();
        git = restAdapter.create(FeeedjAPIClass.class);
        pDilog = new ProgressDialog(LoginActivity.this,R.style.MyTheme);
        pDilog.setCancelable(false);
        pDilog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    @OnClick({R.id.btn_back, R.id.btn_forgetPassword, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_forgetPassword:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
            case R.id.btn_login:
                if (etEmailid.getText().toString().length()>0) {
                    if (validateEmail(etEmailid.getText().toString().trim())){
                        Login();
                        // your code
                    }else{
                        Toast.makeText(LoginActivity.this, "ارجع عيد كتابة الايميل لان فيه شي غلطط ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "لازم تعبي كل المتطلبات عشان نقدر نسجلك", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }

    private boolean validateEmail(String data) {
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(data);
        return emailMatcher.matches();
    }

    private void Login() {
        pDilog.show();
        Map<String, String> params = new HashMap<>();
        params.put("UserName", etEmailid.getText().toString());
        params.put("Password", etPassword.getText().toString());
        params.put("grant_type", "password");
//        Toast.makeText(LoginActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();

        Call<RegistrationResponse> callRegistrationResponse = git.Login(params);
        callRegistrationResponse.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                pDilog.dismiss();
                if (response.isSuccessful()) {
                    editor.putString(Constant.ACCESS_TOKEN, response.body().getAccess_token()).apply();
                    editor.putString(Constant.USERNAME, response.body().getUserName()).apply();
                    editor.putString(Constant.TOKEN_TYPE, response.body().getToken_type()).apply();

                    editor.putBoolean(Constant.EXTERNAL_LOGIN, false).apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    LoginActivity.this.runOnUiThread(() -> {
                        //Handle UI here
                        Toast.makeText(LoginActivity.this, "فيه شي كاتبه غلط، ارجع عيد اللي كتبته ", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                pDilog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                }
                else//Quick fix just tell the user something wrong with credentials
                {
                    LoginActivity.this.runOnUiThread(() -> {
                        //Handle UI here
                        Toast.makeText(LoginActivity.this, "فيه شي كاتبه غلط، ارجع عيد اللي كتبته ", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

}
