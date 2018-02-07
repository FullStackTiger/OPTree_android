package com.tecocraft.optree.model.Ads;

import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("siteUrl")
    private String siteurl;
    @SerializedName("imageUrl")
    private String imageurl;

    private int drawable;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
