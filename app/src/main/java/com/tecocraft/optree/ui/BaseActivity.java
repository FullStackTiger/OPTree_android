package com.tecocraft.optree.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lenovo on 3/25/2017.
 */

public class BaseActivity extends AppCompatActivity  {

    public static int openApp = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //listener for keyboard open close
        openApp = 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        openApp = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        openApp = 1;
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        openApp = 0;
//    }
}
