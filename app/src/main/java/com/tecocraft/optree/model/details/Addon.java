package com.tecocraft.optree.model.details;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 3/8/2017.
 */

public class Addon {

    @SerializedName("roleName")
    private String rolename;
    @SerializedName("description")
    private String description;

    private boolean isCardAdd = false;

    public boolean isCardAdd() {
        return isCardAdd;
    }

    public void setCardAdd(boolean cardAdd) {
        isCardAdd = cardAdd;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
