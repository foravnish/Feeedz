package com.socialinfotech.feeedj.ApplicationActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ParsingModel.FeeedjAPIClass;
import com.socialinfotech.feeedj.ParsingModel.RegistrationResponse;
import com.socialinfotech.feeedj.R;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    LoginButton loginButton;
    private CallbackManager callbackManager;
    ProfileTracker profileTracker;
    private TwitterLoginButton loginButton1;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    boolean launch;
    protected FeeedjAPIClass git;
    ProgressDialog pDilog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Landing");

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


       // Log.d("TokenSavedhere",""+sPref.getString(Constant.FIREBASE_TOKEN, ""));
/*try{
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.socialinfotech.feeedj",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (Exception e) {
    Log.d("Errora",e.toString());
}*/
        pDilog = new ProgressDialog(SplashActivity.this,R.style.MyTheme);
        pDilog.setCancelable(false);
        pDilog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

//        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "email"));

        //Facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("STATE","InsideFacebook3");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,GraphResponse response) {
                                try {
                                    /*Log.d("STATE", object.toString());
                                    Log.d("STATE", "InsideFacebook1");*/
                                    LoginExternal(object.getString("email"), object.getString("id"), "");
                                } catch(Exception ex) {
                                    /*Log.d("STATE", "InsideFacebook2");
                                    Log.d("STATE", ex.toString());*/
                                    Toast.makeText(SplashActivity.this, "فيسبوك مو راضي يعطينا إيميلك عشان نقدر نسجلك", Toast.LENGTH_SHORT).show();
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email, id, name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //Log.d("STATE", "InsideFacebook4");
            }

            @Override
            public void onError(FacebookException e) {
                /*Log.d("STATE", "InsideFacebook5");
                Log.d("STATE", e.toString());*/
                Toast.makeText(SplashActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
            }
        });

        //TwitterAuthConfig authConfig = new TwitterAuthConfig("iKI6gsUzw2oXdzQjct6QOwNCD", "U1C2WCM8pNfMMxdKeFlYhIfUBWSK99WHb8JKquhXyR9Yo5zsS5") ;
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(, ) ;
//        Fabric.with(this, new Twitter(authConfig));
//
//        TwitterConfig config = new TwitterConfig.Builder(this)
//                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
//                .twitterAuthConfig(new TwitterAuthConfig("Ezdda4qifGO4TAMBM2CSFOwn6", "7rCYvnyJUfrBNYYVqDBuynGoBDCffXBjI6ToRkejaPGFomeIeB"))//pass the created app Consumer KEY and Secret also called API Key and Secret
//                .debug(true)//enable debug mode
//                .build();

        //finally initialize twitter with created configs
//        Twitter.initialize(config);

        sPref = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor = sPref.edit();
        Retrofit restAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FeeedjAPIClass.API).build();

        git = restAdapter.create(FeeedjAPIClass.class);
        launch = sPref.getBoolean(Constant.Launcher, true);
        loginButton1 = findViewById(R.id.twitter_login_button);

        loginButton1.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>()  {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;


                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        if(result.data != null)
                            LoginExternal(session.getUserName(), "", String.valueOf(session.getUserId()));
                        else
                            LoginExternal(session.getUserName(), "", String.valueOf(session.getUserId()));
                        // Do something with the result, which provides the email address
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.e("teiter",exception.toString());
                        LoginExternal(session.getUserName(), "", String.valueOf(session.getUserId()));
                      //  Toast.makeText(SplashActivity.this, "q", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        if (launch) {
//            ShowDialog();
        }
    }

    private void LoginExternal(String email, String facebookID, String twitterID) {
        pDilog.show();
        JsonObject obj = new JsonObject();
        obj.addProperty("email", email);
        obj.addProperty("facebookid", facebookID);
        obj.addProperty("twitterid", twitterID);
//        Toast.makeText(LoginActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();
        Call<RegistrationResponse> registrationResponseCall = git.LoginExternal(obj);
        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                pDilog.dismiss();
                if (response.isSuccessful()) {
                    editor.putString(Constant.ACCESS_TOKEN, response.body().getAccess_token()).apply();
                    editor.putString(Constant.USERNAME, response.body().getUserName()).apply();
                    editor.putString(Constant.TOKEN_TYPE, response.body().getToken_type()).apply();
                    editor.putBoolean(Constant.EXTERNAL_LOGIN, true);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(SplashActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                pDilog.dismiss();
                if (t instanceof IOException)
                {
                    Toast.makeText(SplashActivity.this, getResources().getString(R.string.No_internet_conection), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ShowDialog() {
        Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_on_launch);

        TextView txt_road = dialog.findViewById(R.id.txt);
        final Dialog finalDialog = dialog;
        txt_road.setOnClickListener(v -> {
            finalDialog.dismiss();
            editor.putBoolean(Constant.Launcher, false).commit();
        });

        dialog.show();
    }

    private void ShowwarningDialog() {
        Dialog dialog = new Dialog(SplashActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_warning);

        TextView warnig_two = dialog.findViewById(R.id.warnig_two);
        warnig_two.setText("عشان تقدر تشوف العروض الحصرية اللي عندنا؟");

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);

        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(v -> {
            finalDialog.dismiss();
            skip();
        });
        btn_cnl.setOnClickListener(v -> {
            finalDialog.dismiss();
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            finalDialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton1.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_register, R.id.btn_login, R.id.btn_skip,R.id.btn_facebook,R.id.btn_twitter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                break;
            case R.id.btn_skip:
                ShowwarningDialog();
                break;
            case R.id.btn_facebook:
                loginButton.performClick();
                break;
            case R.id.btn_twitter:
                loginButton1.performClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        //Check these later
    }

    private void skip() {
        pDilog.show();
        Call<RegistrationResponse> callRegistrationResponse = git.Skip();
        callRegistrationResponse.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                pDilog.dismiss();
                if (response.isSuccessful()) {
                    editor.putString(Constant.ACCESS_TOKEN, response.body().getAccess_token()).apply();
                    editor.putString(Constant.USERNAME, response.body().getUserName()).apply();
                    editor.putString(Constant.TOKEN_TYPE, response.body().getToken_type()).apply();

                    editor.putBoolean(Constant.EXTERNAL_LOGIN, true).apply();

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {


            }
        });
    }
}
