package com.tecocraft.optree.model.Ads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 3/8/2017.
 */

public class AdsModel {

    @SerializedName("success")
    private boolean success;
    @SerializedName("banner")
    private List<Banner> banner;
    @SerializedName("splash")
    private List<Splash> splash;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Splash> getSplash() {
        return splash;
    }

    public void setSplash(List<Splash> splash) {
        this.splash = splash;
    }
}
