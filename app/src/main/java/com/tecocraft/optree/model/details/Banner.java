package com.tecocraft.optree.model.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

@SerializedName("siteUrl")
@Expose
private String siteUrl;
@SerializedName("imageUrl")
@Expose
private String imageUrl;

public String getSiteUrl() {
return siteUrl;
}

public void setSiteUrl(String siteUrl) {
this.siteUrl = siteUrl;
}

public String getImageUrl() {
return imageUrl;
}

public void setImageUrl(String imageUrl) {
this.imageUrl = imageUrl;
}

}