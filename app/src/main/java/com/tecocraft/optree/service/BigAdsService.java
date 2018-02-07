package com.tecocraft.optree.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.Splash;
import com.tecocraft.optree.ui.AdsActivity;
import com.tecocraft.optree.ui.BaseActivity;
import com.tecocraft.optree.ui.WebviewActivity1;

import java.util.List;


public class BigAdsService extends Service {
    int value;
    private Runnable runnable;
    private Handler handler;


    public BigAdsService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        value = intent.getIntExtra("value", 3000);
        timerSetAds();
//        timer();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void timerSetAds() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                Log.e("-->>", "--------->" + "Google "+WebviewActivity1.inWebSite);
                if (!WebviewActivity1.inWebSite) {
                    if (BaseActivity.openApp == 1) {

                        String splashData = new SharedPref(BigAdsService.this).getDataFromPref(SharedPref.SPLASH);
                        List<Splash> splashList = new Gson().fromJson(splashData, new TypeToken<List<Splash>>() {
                        }.getType());

                        if (splashList != null && !splashList.isEmpty()) {
                            // handler.postDelayed(runnable, 3000);
                            Intent intent = new Intent(BigAdsService.this, AdsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            BigAdsService.this.stopSelf();
                        }
                    } else {
                        handler.postDelayed(runnable, value);
                    }
                }else{
                    handler.postDelayed(runnable, value);
                }
            }
        };
        handler.postDelayed(runnable, value);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handler != null)
            handler.removeCallbacks(runnable);

    }
}
