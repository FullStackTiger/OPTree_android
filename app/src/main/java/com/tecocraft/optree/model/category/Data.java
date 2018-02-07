package com.tecocraft.optree.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 3/7/2017.
 */

public class Data {
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("roleType")
    @Expose
    private String roleType;
    @SerializedName("children")
    @Expose
    private List<Data> dataList = new ArrayList<>();

    private boolean isCardAdded = false;

    public boolean isCardAdded() {
        return isCardAdded;
    }

    public void setCardAdded(boolean cardAdded) {
        isCardAdded = cardAdded;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }
}
