package com.tecocraft.optree.model.favourite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 3/7/2017.
 */

public class addFavourite {
    @SerializedName("success")
    private boolean success;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
