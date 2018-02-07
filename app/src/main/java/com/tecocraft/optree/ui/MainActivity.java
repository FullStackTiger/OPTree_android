package com.tecocraft.optree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.VisitListAdapter;
import com.tecocraft.optree.fragment.CartFragment;
import com.tecocraft.optree.fragment.CategoryFragment;
import com.tecocraft.optree.fragment.FavouriteFragment;
import com.tecocraft.optree.fragment.GlosaryFragment;
import com.tecocraft.optree.fragment.HomeFragment;
import com.tecocraft.optree.fragment.SearchByNameFragment;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.Ads.Banner;
import com.tecocraft.optree.model.DrawerItemModel;
import com.tecocraft.optree.service.BigAdsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.ivBannerImage)
    ImageView ivBannerImage;
    @BindView(R.id.ivDelete)
    public ImageView ivDelete;
    @BindView(R.id.llBanner)
    LinearLayout llBanner;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private List<DrawerItemModel> navItemList = new ArrayList<>();
    private VisitListAdapter adapter;

    SharedPref sharedPref;
    CommonUtils commonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        CommonUtils.activityRef = this;

        if (CommonUtils.isMyServiceRunning(MainActivity.this, BigAdsService.class)) {
            stopService(new Intent(MainActivity.this, BigAdsService.class));
        }
        startService(new Intent(MainActivity.this, BigAdsService.class));
        sharedPref = new SharedPref(MainActivity.this);
        commonUtils = new CommonUtils(MainActivity.this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        handler1.postDelayed(runnable1, 100);
        initView();
    }


    private void initView() {

        navItemList.add(new DrawerItemModel(1, "Search by List", R.drawable.search_name));
        navItemList.add(new DrawerItemModel(2, "Search by Category", R.drawable.category_search));
        navItemList.add(new DrawerItemModel(3, "Favorites", R.drawable.favorites_fill));
        navItemList.add(new DrawerItemModel(4, "Cart", R.drawable.cart_add));
        navItemList.add(new DrawerItemModel(5, "Glossary", R.drawable.glossary));
        navItemList.add(new DrawerItemModel(6, "Home", R.drawable.home));
        navItemList.add(new DrawerItemModel(7, "Logout", R.drawable.logout));


        adapter = new VisitListAdapter(MainActivity.this, navItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(adapter);


        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = HomeFragment.class;


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, fragment).commit();
        llBanner.setVisibility(View.GONE);
        ivDelete.setVisibility(View.INVISIBLE);


    }

    public void drawerItemClick(int position) {
        drawerLayout.closeDrawer(GravityCompat.START);


        if (position == 0) {
            if (commonUtils.isNetworkAvailable())
                replaceFragment(SearchByNameFragment.class);
            else
                CommonUtils.alertDialog(MainActivity.this, getString(R.string.internet_disconnect));
        } else if (position == 1) {
            if (commonUtils.isNetworkAvailable())
                replaceFragment(CategoryFragment.class);
            else
                CommonUtils.alertDialog(MainActivity.this, getString(R.string.internet_disconnect));
        } else if (position == 2) {
            if (commonUtils.isNetworkAvailable())
                replaceFragment(FavouriteFragment.class);
            else
                CommonUtils.alertDialog(MainActivity.this, getString(R.string.internet_disconnect));
        } else if (position == 3) {
            if (commonUtils.isNetworkAvailable())
                replaceFragment(CartFragment.class);
            else
                CommonUtils.alertDialog(MainActivity.this, getString(R.string.internet_disconnect));

        } else if (position == 4) {
            replaceFragment(GlosaryFragment.class);

        } else if (position == 5) {
            replaceFragment(HomeFragment.class);

        } else if (position == 6) {
            sharedPref.setBoolean("isLogin", false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    public void replaceFragment(Class fragmentClass) {

        if (fragmentClass == HomeFragment.class) {
            llBanner.setVisibility(View.GONE);
        } else {
            llBanner.setVisibility(View.VISIBLE);
        }

        if (fragmentClass == CartFragment.class) {
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(View.INVISIBLE);
        }

        if (fragmentClass == HomeFragment.class) {
            tvTitle.setText("O&P Tree");
        } else if (fragmentClass == CartFragment.class) {
            tvTitle.setText("Cart");

        } else if (fragmentClass == SearchByNameFragment.class) {
            tvTitle.setText("Search by List");

        } else if (fragmentClass == CategoryFragment.class) {

            tvTitle.setText("Search By Category");

        } else if (fragmentClass == GlosaryFragment.class) {
            tvTitle.setText("Glossary");

        } else if (fragmentClass == FavouriteFragment.class) {
            tvTitle.setText("Favorites");

        }
//        else if (fragmentClass == CartFragment.class) {
//            tvTitle.setText("Favorite");
//
//        }

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

//        if (CommonUtils.isMyServiceRunning(MainActivity.this, BigAdsService.class)) {
//            stopService(new Intent(MainActivity.this, BigAdsService.class));
//        }
//
//        startService(new Intent(MainActivity.this, BigAdsService.class));

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (CommonUtils.isMyServiceRunning(MainActivity.this, BigAdsService.class)) {
            stopService(new Intent(MainActivity.this, BigAdsService.class));
        }
    }


    @OnClick({R.id.ivMenu, R.id.llBanner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                commonUtils.hideKeyboard(MainActivity.this);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.llBanner:
                if (commonUtils.isNetworkAvailable())
                    startActivity(new Intent(MainActivity.this, WebviewActivity1.class).putExtra("SiteUrl", siteURL));
                else
                    CommonUtils.alertDialog(MainActivity.this, getString(R.string.internet_disconnect));
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

//            Glide.with(MainActivity.this).load(bannerList.get(imageCount).getImageurl())
//                    .into(ivBannerImage);
//            siteURL = bannerList.get(imageCount).getSiteurl();
            siteURL = commonUtils.loadBanner(MainActivity.this, bannerList.get(imageCount), ivBannerImage);
            imageCount++;
            handler.postDelayed(runnable, Conts.BANNER_TIME_RATE);
        }
    };


//    Handler handler1 = new Handler();
//    int i = 0;
//    Runnable runnable1 = new Runnable() {
//        @Override
//        public void run() {
//            Log.e("banner","------------ "+i+"-------------");
//            i = i+1;
//            handler1.postDelayed(runnable1, 1000);
//        }
//    };


    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (CommonUtils.isMyServiceRunning(MainActivity.this, BigAdsService.class)) {
//            stopService(new Intent(MainActivity.this, BigAdsService.class));
//        }
//        startService(new Intent(MainActivity.this, BigAdsService.class));3


        BaseActivity.openApp = 1;
        CommonUtils.activityRef = this;
        setBannerLogic();
    }

    private void setBannerLogic() {
        String splashData = sharedPref.getDataFromPref(SharedPref.BANNER);
        bannerList = new Gson().fromJson(splashData, new TypeToken<List<Banner>>() {
        }.getType());

        if (bannerList != null && !bannerList.isEmpty()) {
            long seed = System.nanoTime();
            Collections.shuffle(bannerList, new Random(seed));

//            Log.e("banner","-->> baner size "+bannerList.size());
            if (bannerList.size() != 1) {
//                Log.e("banner","here != 1 ");
                handler.postDelayed(runnable, 100);
            } else {
//                Log.e("banner","here == 1 ");
                siteURL = commonUtils.loadBanner(MainActivity.this, bannerList.get(imageCount), ivBannerImage);
            }
        }


    }


}
