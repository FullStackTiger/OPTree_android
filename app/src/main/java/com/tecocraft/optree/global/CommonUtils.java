package com.tecocraft.optree.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tecocraft.optree.model.Ads.Banner;
import com.tecocraft.optree.ui.MainActivity;

public class CommonUtils {


    public static MainActivity activityRef;

    Context mContext;
    int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public CommonUtils(Context mContext) {
        this.mContext = mContext;
    }


    public void toastShow(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    //check internet connection
    public boolean isNetworkAvailable() {
        /* getting systems Service connectivity manager */
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    // soft keyboard hide
    public void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {

        }
    }

    // soft keyboard hide
    public void hideKeyboard(Activity activity, View view) {
        // Check if no view has focus:
        try {
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    // soft keyboard show
    public void showKeyboard(Activity activity, View view) {
        // Check if no view has focus:
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {

        }
    }

    //create dialog
    public ProgressDialog createDialog() {
        ProgressDialog progress = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progress.setMessage(Html.fromHtml("<font color='black'  ><big>"
                + "Loading..." + "</big></font>"));
        progress.setCancelable(false);
        return progress;
    }

    public void showDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
    }

    public void dismissDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    //show simple message print for bottom of screen
    public void snackbar(View coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
//        snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark1));
    }

    // DP to PX converter
    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mContext.getResources().getDisplayMetrics());
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void alertDialog(Context activity, String message) {
        new AlertDialog.Builder(activity)
                .setTitle("Alert!!!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void alertDialog(Context activity, String message, String title) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void alertDialog(Context activity, String message, boolean withoutTitle) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public String loadBanner(Context context, Banner banner, ImageView imageView) {
        if (banner.getImageurl().equals(""))
            Glide.with(context).load(banner.getDrawable())
                    .into(imageView);
        else
            Glide.with(context).load(banner.getImageurl())
                    .into(imageView);

        return banner.getSiteurl();
    }
}
