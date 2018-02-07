package com.tecocraft.optree.model.details;

import com.tecocraft.optree.model.category.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 3/8/2017.
 */

public class TreeModel {

    private String roleName;
    private String dataInJson;
    private List<Data> dataArrayList = new ArrayList<>();

    public String getDataInJson() {
        return dataInJson;
    }

    public void setDataInJson(String dataInJson) {
        this.dataInJson = dataInJson;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Data> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(List<Data> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }
}
