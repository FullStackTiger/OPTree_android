package com.tecocraft.optree.model.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodeDetailsModel {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("code")
    @Expose
    private Code code;
    @SerializedName("favorite")
    @Expose
    private boolean favorite;
    @SerializedName("addon")
    @Expose
    private List<Addon> addon;


    public List<Addon> getAddon() {
        return addon;
    }

    public void setAddon(List<Addon> addon) {
        this.addon = addon;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}