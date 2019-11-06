package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.RegistrationResponse;
import com.socialinfotech.feeedj.R;

import java.io.IOException;
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

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.et_emailid)
    EditText etEmailid;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_userID)
    EditText etUserID;
    protected FeeedjAPIClass git;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    ProgressDialog progress;
    private String blockCharacterSet = "~`!@#$%^&*()? ";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Register");

        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        sPref = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
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
        progress = new ProgressDialog(RegistrationActivity.this,R.style.MyTheme);
        progress.setCancelable(false);
        progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        git = restAdapter.create(FeeedjAPIClass.class);
        etUserID.setFilters(new InputFilter[] { filter });

    }
    private boolean validateEmail(String data){
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(data);
        return emailMatcher.matches();
    }
    @OnClick({R.id.btn_back, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_login:
                if (etUserID.getText().toString().length()>0 && etPassword.getText().toString().length()>0 && etEmailid.getText().toString().length()>0) {
                    if (validateEmail(etEmailid.getText().toString().trim())){
                        if(etPassword.getText().length() < 6) {//quick fix if valid email, check password not less than 6 chars
                            Toast.makeText(RegistrationActivity.this, "كلمة المرور لازم تتكون من 6 حروف عالأقل", Toast.LENGTH_SHORT).show();
                        } else
                            progress.show();
                            RegisterUser();
                        // your code
                    }else{
                        Toast.makeText(RegistrationActivity.this, "ارجع عيد كتابة الايميل لان فيه شي غلط ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegistrationActivity.this, "لازم تعبي كل المتطلبات عشان نقدر نسجلك", Toast.LENGTH_SHORT).show();
                    Log.d("registerT","FillAll");
                }
//                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                break;
        }
    }

    private void RegisterUser() {
        Toast.makeText(RegistrationActivity.this, "لحظات قاعدين نسجلك", Toast.LENGTH_SHORT).show();
        JsonObject obj = new JsonObject();
        obj.addProperty("Email", etEmailid.getText().toString());
        obj.addProperty("UserName", etUserID.getText().toString());
        obj.addProperty("Password", etPassword.getText().toString());
        obj.addProperty("ConfirmPassword", etPassword.getText().toString());

        Call<RegistrationResponse> registrationResponseCall = git.Registration(obj);
        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Log.d("registerT","success");
                if (response.isSuccessful()){
                    progress.dismiss();
                    if (response.body().getAccess_token().length()>0) {
                        Log.d("registerT","received Token");
                        editor.putString(Constant.ACCESS_TOKEN, response.body().getAccess_token()).apply();
                        editor.putString(Constant.USERNAME, response.body().getUserName()).apply();
                        editor.putString(Constant.TOKEN_TYPE, response.body().getToken_type()).apply();
                        editor.putBoolean(Constant.EXTERNAL_LOGIN, false).apply();
                        Intent intent =new Intent(RegistrationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Log.d("registerT","noToken");
                        RegistrationActivity.this.runOnUiThread(() -> {
                            //Handle UI here
                            Toast.makeText(RegistrationActivity.this, " شكلك استخدمت رموز ومسافات باسم المستخدم \uD83D\uDE27\\n او سبقوك غيرك واخذوا الاسم Disappointed face \\n\\n حاول تختار غيره!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
                else {//In case something else but 200 blame it on the username as quick fix
                    progress.dismiss();
                    RegistrationActivity.this.runOnUiThread(() -> {
                        //Handle UI here
                        Toast.makeText(RegistrationActivity.this, " شكلك استخدمت رموز ومسافات باسم المستخدم \uD83D\uDE27\\n او سبقوك غيرك واخذوا الاسم Disappointed face \\n\\n حاول تختار غيره!", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException) {
                    Log.d("registerT","networkFailure");
                    RegistrationActivity.this.runOnUiThread(() -> {
                        //Handle UI here
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                    });
                }
                else//Blame it on the user here for some reason if it is not 200 it goes here
                {
                    Log.d("registerT","wrong user");
                    RegistrationActivity.this.runOnUiThread(() -> {
                        //Handle UI here
                        Toast.makeText(RegistrationActivity.this, " شكلك استخدمت رموز ومسافات باسم المستخدم \uD83D\uDE27\\n او سبقوك غيرك واخذوا الاسم Disappointed face \\n\\n حاول تختار غيره!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
