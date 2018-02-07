package com.tecocraft.optree.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 3/24/2017.
 */

public class ConfirmModel {

    @SerializedName("success")
    private boolean success;
    @SerializedName("_id")
    private String Id;
    @SerializedName("message")
    private String message;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
