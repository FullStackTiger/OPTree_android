package com.tecocraft.optree.model.name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Code {

    @SerializedName("codeName")
    @Expose
    private String codeName;
    @SerializedName("codeDescription")
    @Expose
    private String codeDescription;
    @SerializedName("detailImg")
    @Expose
    private String detailImg;

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

    private boolean isCardAdd = false;

    public boolean isCardAdd() {
        return isCardAdd;
    }

    public void setCardAdd(boolean cardAdd) {
        isCardAdd = cardAdd;
    }


    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public void setDetailImg(String detailImg) {
        this.detailImg = detailImg;
    }

}