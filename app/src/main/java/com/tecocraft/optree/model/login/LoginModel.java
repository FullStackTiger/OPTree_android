package com.tecocraft.optree.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("banner")
    @Expose
    private List<Banner> banner = null;
    @SerializedName("splash")
    @Expose
    private List<Splash> splash = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("token")
    @Expose
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}