package com.tecocraft.optree.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.AddonListAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.Banner;
import com.tecocraft.optree.model.details.Addon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddonActivity extends BaseActivity {
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivBannerImage)
    ImageView ivBannerImage;
    private CommonUtils commonUtils;
    private SharedPref sharedPref;

    //L0174


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addon);
        ButterKnife.bind(this);

        tvToolbarTitle.setText("Add on codes");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(AddonActivity.this, R.color.colorPrimaryDark));
        }
        init();

//        if (CommonUtils.isMyServiceRunning(AddonActivity.this,BigAdsService.class)) {
//            stopService(new Intent(AddonActivity.this, BigAdsService.class));
//        }
//        startService(new Intent(AddonActivity.this, BigAdsService.class));
    }

    private void init() {

        String json = getIntent().getStringExtra("value");
        List<Addon> dataList = new Gson().fromJson(json, new TypeToken<List<Addon>>() {
        }.getType());

        sharedPref = new SharedPref(AddonActivity.this);
        tvToolbarTitle.setText("Add on Codes");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddonActivity.this);
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        commonUtils = new CommonUtils(AddonActivity.this);


        for (int i = 0; i < dataList.size(); i++) {
            if (Conts.checkAddedOrNot(dataList.get(i).getRolename())) {
                Addon c = dataList.get(i);
                c.setCardAdd(true);
                dataList.set(i, c);
            } else {
                Addon c = dataList.get(i);
                c.setCardAdd(false);
                dataList.set(i, c);
            }
        }



        AddonListAdapter adapter = new AddonListAdapter(AddonActivity.this, dataList);
        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    @OnClick({R.id.ivBack, R.id.llBanner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.llBanner:
                if (commonUtils.isNetworkAvailable())
                startActivity(new Intent(AddonActivity.this, WebviewActivity1.class).putExtra("SiteUrl", siteURL));
                else
                    CommonUtils.alertDialog(AddonActivity.this, getString(R.string.internet_disconnect));
                break;
        }
    }


    List<Banner> bannerList = new ArrayList<>();
    Handler handler = new Handler();
    int imageCount = 0;
    String siteURL = "";
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (imageCount == bannerList.size())
                imageCount = 0;

//            Glide.with(AddonActivity.this).load(bannerList.get(imageCount).getImageurl())
//                    .into(ivBannerImage);
//            siteURL = bannerList.get(imageCount).getSiteurl();
            siteURL =commonUtils.loadBanner(AddonActivity.this,bannerList.get(imageCount),ivBannerImage);
            imageCount++;
            handler.postDelayed(runnable, Conts.BANNER_TIME_RATE);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (handler!=null)
            handler.removeCallbacks(runnable);

//        if (CommonUtils.isMyServiceRunning(AddonActivity.this,BigAdsService.class)) {
//            stopService(new Intent(AddonActivity.this, BigAdsService.class));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBannerLogic();
    }

    private void setBannerLogic() {
        String splashData = sharedPref.getDataFromPref(SharedPref.BANNER);
        bannerList = new Gson().fromJson(splashData, new TypeToken<List<Banner>>() {
        }.getType());

        if (bannerList != null && !bannerList.isEmpty()) {
            long seed = System.nanoTime();
            Collections.shuffle(bannerList, new Random(seed));

            if (bannerList.size() != 1) {
                handler.postDelayed(runnable, 100);
            } else {
//                Glide.with(AddonActivity.this).load(bannerList.get(imageCount).getImageurl())
//                        .into(ivBannerImage);
//                siteURL = bannerList.get(imageCount).getSiteurl();
                siteURL =commonUtils.loadBanner(AddonActivity.this,bannerList.get(imageCount),ivBannerImage);
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (CommonUtils.isMyServiceRunning(AddonActivity.this,BigAdsService.class)) {
//            stopService(new Intent(AddonActivity.this, BigAdsService.class));
//        }
//        startService(new Intent(AddonActivity.this, BigAdsService.class));
//    }


    @Override
    public void finish() {
        super.finish();
        CommonUtils.activityRef = null;
    }

}

