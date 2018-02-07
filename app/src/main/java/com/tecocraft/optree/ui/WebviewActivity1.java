package com.tecocraft.optree.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewActivity1 extends BaseActivity {

    @BindView(R.id.webView)
    WebView webview;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.imgNxt)
    ImageView imgNxt;
    @BindView(R.id.imgRefresh)
    ImageView imgRefresh;
    @BindView(R.id.webtoolbar)
    Toolbar webtoolbar;
    @BindView(R.id.progressBarWeb)
    ProgressBar progressBarWeb;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private String url;
    private boolean mIsLoadFinish = false;
    private TextView txtTitle;
    CommonUtils commonUtils;
    public static final String WEB_URL = "SiteUrl";
    public static boolean inWebSite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(WebviewActivity1.this, R.color.colorPrimaryDark));
        }

//        if (CommonUtils.isMyServiceRunning(WebviewActivity1.this,BigAdsService.class)) {
//            stopService(new Intent(WebviewActivity1.this, BigAdsService.class));
//        }
//        startService(new Intent(WebviewActivity1.this, BigAdsService.class));

        ButterKnife.bind(this);
        commonUtils = new CommonUtils(WebviewActivity1.this);
        InitUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
        inWebSite = true;
    }

    private void InitUI() {
        url = getIntent().getExtras().getString(WEB_URL, "");

        if (!url.contains("http")) {
            if (!url.contains("www")) {
                url = "www." + url;
            }

            if (!url.contains("http")) {
                url = "http://" + url;
            }
        }

        if (!Patterns.WEB_URL.matcher(url).matches()) {
            alertDialog();
            return;
        }

        setToolbar();
        webview = (WebView) findViewById(R.id.webView);
        txtTitle = (TextView) findViewById(R.id.textWeb);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);

        SetUrl();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("-->>", "--------->" + "onStop");
        inWebSite = false;
    }

    public void alertDialog() {
        new AlertDialog.Builder(WebviewActivity1.this)
                .setTitle("Invalid site url")
                .setMessage("We can't access this site")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        onBackPressed();
                    }
                })
                .show();
    }

    private void setToolbar() {
        setSupportActionBar(webtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        webtoolbar.setContentInsetsAbsolute(0, 0);

        TextView toolbarTitle = (TextView) webtoolbar.findViewById(R.id.textWeb);
        toolbarTitle.setText(R.string.app_name);

        findViewById(R.id.ivToolbarIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.goBack();
                enableControllerButton();
            }
        });

        findViewById(R.id.imgNxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.goForward();
                enableControllerButton();
            }
        });

        findViewById(R.id.imgRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUrl();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_web, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionWeb) {
            ShowInBrowser();
            return true;
        } else if (item.getItemId() == R.id.actionCopy) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.app_name), url);
            clipboard.setPrimaryClip(clip);
            return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void ShowInBrowser() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        webview.loadUrl("");
        webview.stopLoading();
        super.onBackPressed();

//        if (CommonUtils.isMyServiceRunning(WebviewActivity1.this,BigAdsService.class)) {
//            stopService(new Intent(WebviewActivity1.this, BigAdsService.class));
//        }
//        startService(new Intent(WebviewActivity1.this, BigAdsService.class));

    }

    private void SetUrl() {

        if (!commonUtils.isNetworkAvailable())
            snackBarRetry(getString(R.string.internet_error));

        mIsLoadFinish = false;
        progressBarWeb.setVisibility(View.VISIBLE);
        webview.setWebViewClient(new WebViewClient() {
            // load url
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            // when finish loading page
            public void onPageFinished(WebView view, String url) {
                txtTitle.setText(view.getTitle());
                mIsLoadFinish = true;
                enableControllerButton();
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBarWeb.setProgress(newProgress);
                if (newProgress == 100)
                    progressBarWeb.setVisibility(View.GONE);
                else
                    progressBarWeb.setVisibility(View.VISIBLE);
            }
        });

        // set url for webview to load
        webview.loadUrl(url);
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > 5)
                progressBarWeb.setProgress(newProgress);
            else
                progressBarWeb.setProgress(5);

            super.onProgressChanged(view, newProgress);
        }
    }

    private void enableControllerButton() {
        if (mIsLoadFinish) {
            imgRefresh.setEnabled(true);
            if (webview.canGoBack()) {
                imgBack.setEnabled(true);
                imgBack.setImageResource(R.drawable.left_arrows);
            } else {
                imgBack.setEnabled(false);
                imgBack.setImageResource(R.drawable.left_arrows_gray);
            }
            if (webview.canGoForward()) {
                imgNxt.setEnabled(true);
                imgNxt.setImageResource(R.drawable.right_arrows);

            } else {
                imgNxt.setEnabled(false);
                imgNxt.setImageResource(R.drawable.right_arrows_gray);
            }
        } else {
            imgBack.setEnabled(false);
            imgNxt.setEnabled(false);
            imgBack.setImageResource(R.drawable.left_arrows_gray);
            imgNxt.setImageResource(R.drawable.right_arrows_gray);
        }
    }

    protected void snackBarRetry(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SetUrl();
                    }
                });
        snackbar.show();
        snackbar.setActionTextColor(ContextCompat.getColor(WebviewActivity1.this, R.color.red_color));
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (CommonUtils.isMyServiceRunning(WebviewActivity1.this,BigAdsService.class)) {
//            stopService(new Intent(WebviewActivity1.this, BigAdsService.class));
//        }
    }


}
