package com.tecocraft.optree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.Splash;
import com.tecocraft.optree.service.BigAdsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdsActivity extends AppCompatActivity {

    private static final String TAG = "AdsActivity";
    @BindView(R.id.imageClose)
    ImageView imageClose;
    @BindView(R.id.imageAds)
    ImageView imageAds;
    boolean isColseActivity = false;
    Runnable runnable;
    Handler handler;
    private int timeTask;
    SharedPref sharedPref;
    private List<Splash> splashList = new ArrayList<>();
    private String siteURL = "";
    private int totalSec = 0;
    CommonUtils commonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        if (CommonUtils.isMyServiceRunning(AdsActivity.this, BigAdsService.class)) {
            stopService(new Intent(AdsActivity.this, BigAdsService.class));
        }
        ButterKnife.bind(this);
        sharedPref = new SharedPref(AdsActivity.this);
        commonUtils = new CommonUtils(AdsActivity.this);
        init();
    }

    private void timerTask() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                totalSec = totalSec + 1;
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.post(runnable);
    }


    private void init() {
        String splashData = sharedPref.getDataFromPref(SharedPref.SPLASH);
        splashList = new Gson().fromJson(splashData, new TypeToken<List<Splash>>() {
        }.getType());

        long seed = System.nanoTime();
        Collections.shuffle(splashList, new Random(seed));
        if (!splashList.get(0).getImageurl().equals(""))
            Glide.with(AdsActivity.this).load(splashList.get(0).getImageurl()).placeholder(R.drawable.no_images).error(R.drawable.no_available)
                    .into(imageAds);
        else
            Glide.with(AdsActivity.this).load(splashList.get(0).getDrawable()).placeholder(R.drawable.no_images).error(R.drawable.no_available)
                    .into(imageAds);

        siteURL = splashList.get(0).getSiteurl();
    }

    @OnClick({R.id.imageClose, R.id.imageAds})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageClose:
                onBackPressed();
                break;
            case R.id.imageAds:
                if (commonUtils.isNetworkAvailable())
                    startActivity(new Intent(AdsActivity.this, WebviewActivity1.class).putExtra("SiteUrl", siteURL));
                else
                    CommonUtils.alertDialog(AdsActivity.this, getString(R.string.internet_disconnect));
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        timerTask();
    }

    @Override
    protected void onPause() {
        if (handler != null)
            handler.removeCallbacks(runnable);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        int nexTitme;
        if (totalSec < Conts.SPLASH_TIME_RATE) {
            nexTitme = Conts.SPLASH_TIME_RATE - totalSec;
        } else {
            nexTitme = Conts.SPLASH_TIME_RATE - (totalSec % Conts.SPLASH_TIME_RATE);
        }
        Intent mServiceIntent = new Intent(this, BigAdsService.class);
        mServiceIntent.putExtra("value", nexTitme * 1000);
        startService(mServiceIntent);

        super.onBackPressed();
    }
}
