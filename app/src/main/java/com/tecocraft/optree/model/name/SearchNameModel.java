package com.tecocraft.optree.model.name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchNameModel {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("codes")
    @Expose
    private List<Code> codes = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

}