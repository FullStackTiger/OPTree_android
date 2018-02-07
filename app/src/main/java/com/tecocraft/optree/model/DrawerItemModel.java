package com.tecocraft.optree.model;

/**
 * Created by agile-01 on 7/26/2016.
 */
public class DrawerItemModel {

    int itemPos;
    String name;
    int image;

    public DrawerItemModel(int itemPos,String name, int image) {
        this.itemPos = itemPos;
        this.name = name;
        this.image = image;
    }

    public int getItemPos() {
        return itemPos;
    }

    public void setItemPos(int itemPos) {
        this.itemPos = itemPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
