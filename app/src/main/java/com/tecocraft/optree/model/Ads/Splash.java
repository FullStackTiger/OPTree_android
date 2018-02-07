package com.tecocraft.optree.model.Ads;

import com.google.gson.annotations.SerializedName;

public class Splash {
    @SerializedName("imageUrl")
    private String imageurl;
    @SerializedName("siteUrl")
    private String siteurl;
    private int drawable;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }
}
