package com.tecocraft.optree.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.service.BigAdsService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebviewActivity extends AppCompatActivity {
    private static final String TAG = "WebviewActivity";
    @BindView(R.id.webView)
    WebView myWebView;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    private ProgressDialog pd;
    private CommonUtils commonUtils;

    private SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        commonUtils = new CommonUtils(WebviewActivity.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(WebviewActivity.this, R.color.colorPrimaryDark));
        }
//        if (CommonUtils.isMyServiceRunning(WebviewActivity.this, BigAdsService.class)) {
//            stopService(new Intent(WebviewActivity.this, BigAdsService.class));
//        }
//        startService(new Intent(WebviewActivity.this, BigAdsService.class));

        pd = new ProgressDialog(WebviewActivity.this);
        pd.setMessage("Loading...");


        tvToolbarTitle.setText("Site Url");
        Bundle bundle = getIntent().getExtras();
        String siteurl = bundle.getString("SiteUrl");

        if (!siteurl.contains("http://")) {
            siteurl = "http://www." + siteurl;
        }

        myWebView.loadUrl(siteurl);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.centerend.com")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }*/

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pd != null && !pd.isShowing()) {
                pd.show();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack(); // Go to previous page
            return true;
        }
        // Use this as else part
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
//
//        if (CommonUtils.isMyServiceRunning(WebviewActivity.this,BigAdsService.class)) {
//            stopService(new Intent(WebviewActivity.this, BigAdsService.class));
//        }
    }


}
