package com.tecocraft.optree.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.CustomPagerAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.Banner;
import com.tecocraft.optree.model.details.CodeDetailsModel;
import com.tecocraft.optree.model.details.Detail;
import com.tecocraft.optree.model.favourite.addFavourite;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends BaseActivity {
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvCodeName)
    TextView tvCodeName;
    @BindView(R.id.tvCodeDescription)
    TextView tvCodeDescription;


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ivFavourite)
    ImageView ivFavourite;
    @BindView(R.id.ivBannerImage)
    ImageView ivBannerImage;
    @BindView(R.id.llBanner)
    LinearLayout llBanner;
    @BindView(R.id.btnAddon)
    Button btnAddon;
    @BindView(R.id.ivCart)
    ImageView ivCart;
    private ProgressDialog pd;
    private CommonUtils commonUtils;

    private SharedPref sharedPref;
    String codeName, codeDescription, codeImage;
    CodeDetailsModel model;
    private boolean isCardAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvToolbarTitle.setText("Detail Page");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorPrimaryDark));
        }

//        if (CommonUtils.isMyServiceRunning(DetailsActivity.this, BigAdsService.class)) {
//            stopService(new Intent(DetailsActivity.this, BigAdsService.class));
//        }
//        startService(new Intent(DetailsActivity.this, BigAdsService.class));

        init();
    }

    private void init() {
        btnAddon.setVisibility(View.GONE);

        tvToolbarTitle.setText("Detail Page");
        sharedPref = new SharedPref(DetailsActivity.this);

        pd = new ProgressDialog(DetailsActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        commonUtils = new CommonUtils(DetailsActivity.this);
        Bundle intent = getIntent().getExtras();
        codeName = intent.getString(sharedPref.CODE_NAME);

        codeDetailsFromServer();
    }


    private void addFavourite() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<addFavourite> call = apiService.addFavourite("" + sharedPref.getDataFromPref(sharedPref.USER_ID), "" + codeName, codeDescription);
            call.enqueue(new Callback<addFavourite>() {
                @Override
                public void onResponse(Call<addFavourite> call, Response<addFavourite> response) {
                    if (response.isSuccessful()) {
                        addFavourite faveModel = response.body();
                        if (faveModel.getSuccess()) {

                            model.setFavorite(true);
                            ivFavourite.setImageResource(R.drawable.favorites_fill);
                            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.success_added), "Success");
//                            Toast.makeText(DetailsActivity.this, "Data added to favourite successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.server_error));
//                            Toast.makeText(DetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }

                @Override
                public void onFailure(Call<addFavourite> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.internet_disconnect));
//            Toast.makeText(DetailsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFavourite() {


        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<addFavourite> call = apiService.removeFavourite("" + sharedPref.getDataFromPref(sharedPref.USER_ID), "" + codeName);
            call.enqueue(new Callback<addFavourite>() {
                @Override
                public void onResponse(Call<addFavourite> call, Response<addFavourite> response) {
                    if (response.isSuccessful()) {
                        addFavourite faveModel = response.body();
                        if (faveModel.getSuccess()) {
                            model.setFavorite(false);
                            ivFavourite.setImageResource(R.drawable.favourite);
//                            Toast.makeText(DetailsActivity.this, "Remove data from favourite successfully...", Toast.LENGTH_SHORT).show();
                            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.success_added_delete), "Success");
                        } else {
//                            Toast.makeText(DetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.server_error));
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }

                @Override
                public void onFailure(Call<addFavourite> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.internet_disconnect));
//            Toast.makeText(DetailsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void codeDetailsFromServer() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CodeDetailsModel> call = apiService.codeDetails("" + sharedPref.getDataFromPref(sharedPref.USER_ID), "" + codeName);
            call.enqueue(new Callback<CodeDetailsModel>() {
                @Override
                public void onResponse(Call<CodeDetailsModel> call, Response<CodeDetailsModel> response) {
                    if (response.isSuccessful()) {
                        model = response.body();
                        if (model.isSuccess()) {
                            List<Detail> detailList = model.getCode().getDetails();
                            if (detailList == null || detailList.isEmpty()) {
                                Detail detail = new Detail();
                                detail.setImageUrl("abc");
                                detailList.add(detail);
                            }
                            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(DetailsActivity.this, detailList);
                            //int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20 * 2, getResources().getDisplayMetrics());
                            viewpager.setAdapter(customPagerAdapter);
                            viewpager.setOffscreenPageLimit(customPagerAdapter.getCount());
                            //A little space between pages
                            viewpager.setPageMargin(-40);
                            //If hardware acceleration is enabled, you should also remove
                            // clipping on the pager for its children.
                            viewpager.setClipChildren(false);
                            tvCodeName.setText(model.getCode().getCodeName());
                            tvCodeDescription.setText(model.getCode().getDescription());
                            codeDescription = model.getCode().getDescription();
                            codeImage = model.getCode().getDetails().get(0).getImageUrl();

                            if (model.getAddon() != null && !model.getAddon().isEmpty())
                                btnAddon.setVisibility(View.VISIBLE);

                            if (model.isFavorite()) {
                                ivFavourite.setImageResource(R.drawable.favorites_fill);
                            } else {
                                ivFavourite.setImageResource(R.drawable.favourite);
                            }

                        } else {
                            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.server_error));
//                            Toast.makeText(DetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }

                @Override
                public void onFailure(Call<CodeDetailsModel> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(DetailsActivity.this, getString(R.string.internet_disconnect));
//            Toast.makeText(DetailsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick({R.id.ivFavourite, R.id.btnAddon, R.id.ivBack, R.id.llBanner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFavourite:
                if (!model.isFavorite()) {
                    addFavourite();
                } else {
                    removeFavourite();
                }
                break;
            case R.id.btnAddon:
                String jsonData = new Gson().toJson(model.getAddon());
                startActivity(new Intent(DetailsActivity.this, AddonActivity.class).putExtra("value", jsonData));
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.llBanner:
                startActivity(new Intent(DetailsActivity.this, WebviewActivity1.class).putExtra("SiteUrl", siteURL));
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

//            Glide.with(DetailsActivity.this).load(bannerList.get(imageCount).getImageurl())
//                    .into(ivBannerImage);
//            siteURL = bannerList.get(imageCount).getSiteurl();
            siteURL = commonUtils.loadBanner(DetailsActivity.this, bannerList.get(imageCount), ivBannerImage);
            imageCount++;
            handler.postDelayed(runnable, Conts.BANNER_TIME_RATE);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null)
            handler.removeCallbacks(runnable);

//        if (CommonUtils.isMyServiceRunning(DetailsActivity.this, BigAdsService.class)) {
//            stopService(new Intent(DetailsActivity.this, BigAdsService.class));
//        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (Conts.checkAddedOrNot(codeName)) {
            ivCart.setImageResource(R.drawable.cart_delete);
            isCardAdded = true;
        } else {
            ivCart.setImageResource(R.drawable.cart_add);
            isCardAdded = false;
        }

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
//                Glide.with(DetailsActivity.this).load(bannerList.get(imageCount).getImageurl())
//                        .into(ivBannerImage);
//                siteURL = bannerList.get(imageCount).getSiteurl();
                siteURL = commonUtils.loadBanner(DetailsActivity.this, bannerList.get(imageCount), ivBannerImage);
            }
        }
    }

    @OnClick(R.id.ivCart)
    public void onClick() {
        if (isCardAdded) {
            Conts.deleteCard(codeName);
            ivCart.setImageResource(R.drawable.cart_add);
            isCardAdded = false;
        } else {
            Conts.addCart(codeName, codeDescription, codeImage);
            ivCart.setImageResource(R.drawable.cart_delete);
            isCardAdded = true;
        }
    }


}
