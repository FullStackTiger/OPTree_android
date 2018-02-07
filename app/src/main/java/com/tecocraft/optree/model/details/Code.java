package com.tecocraft.optree.model.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Code {

    @SerializedName("codeName")
    @Expose
    private String codeName;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("splashs")
    @Expose
    private List<Splash> splashs = null;
    @SerializedName("banners")
    @Expose
    private List<Banner> banners = null;
    @SerializedName("codeDescription")
    @Expose
    private String description;



    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public List<Splash> getSplashs() {
        return splashs;
    }

    public void setSplashs(List<Splash> splashs) {
        this.splashs = splashs;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}