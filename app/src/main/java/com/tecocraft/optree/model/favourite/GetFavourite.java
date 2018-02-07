package com.tecocraft.optree.model.favourite;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 3/7/2017.
 */

public class GetFavourite {
    @SerializedName("success")
    private boolean success;
    @SerializedName("favlist")
    private List<Favlist> favlist;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Favlist> getFavlist() {
        return favlist;
    }

    public void setFavlist(List<Favlist> favlist) {
        this.favlist = favlist;
    }
}
