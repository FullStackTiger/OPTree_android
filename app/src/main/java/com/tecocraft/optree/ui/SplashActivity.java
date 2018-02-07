package com.tecocraft.optree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.AdsModel;
import com.tecocraft.optree.model.Ads.Banner;
import com.tecocraft.optree.model.Ads.Splash;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private Runnable runnable;
    private Handler handler;

    private CommonUtils commonUtils;
    Call<AdsModel> call;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        commonUtils = new CommonUtils(SplashActivity.this);
        sharedPref = new SharedPref(SplashActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (handler != null)
            handler.removeCallbacks(runnable);

        if (call != null)
            call.cancel();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromServer();
    }

    private void timer() {


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {


                if (!sharedPref.getBoolean("isLogin")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void getDataFromServer() {

        final List<Splash> splash = new ArrayList<>();
        final List<Banner> banner = new ArrayList<>();

        Splash splashModel = new Splash();
        splashModel.setDrawable(R.drawable.splash_ads);
        splashModel.setSiteurl("http://www.oandptree.com");
        splashModel.setImageurl("");
        splash.add(splashModel);

        Banner bannerModel = new Banner();
        bannerModel.setDrawable(R.drawable.banner_ads);
        bannerModel.setSiteurl("http://www.oandptree.com");
        bannerModel.setImageurl("");
        banner.add(bannerModel);


        if (commonUtils.isNetworkAvailable()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getADS();
            call.enqueue(new Callback<AdsModel>() {
                @Override
                public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {
                    if (response.isSuccessful()) {
                        AdsModel model = response.body();
                        if (model.getSuccess()) {
                            Gson gson = new Gson();

                            banner.addAll(model.getBanner());
                            splash.addAll(model.getSplash());

                            String bannerImages = gson.toJson(banner);
                            String splashImages = gson.toJson(splash);
                            sharedPref.setDataInPref(SharedPref.BANNER, bannerImages);
                            sharedPref.setDataInPref(SharedPref.SPLASH, splashImages);
                        }
                    }

                    timer();
                }

                @Override
                public void onFailure(Call<AdsModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());

                    timer();
                }
            });
        } else {
            CommonUtils.alertDialog(SplashActivity.this, getString(R.string.internet_disconnect));
//            Toast.makeText(SplashActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
