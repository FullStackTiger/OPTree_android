package com.tecocraft.optree.model.favourite;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Favlist {

    @SerializedName("cname")
    private String cname;
    @SerializedName("description")
    private String description;
    @SerializedName("imgUrl")
    private String imgurl;
    @SerializedName("_id")
    private String Id;


    private String searchString;

    private List<String> searchList = new ArrayList<>();

    public List<String> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<String> searchList) {
        this.searchList = searchList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}
