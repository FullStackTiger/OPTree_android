package com.tecocraft.optree.global;

import com.tecocraft.optree.model.name.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 3/8/2017.
 */

public class Conts {
    public static final int BANNER_TIME_RATE = 10000;
    public static final int SPLASH_TIME_RATE = 90; // sec
    public static final List<Code> listCart = new ArrayList<>();

    public static boolean checkAddedOrNot(String cname) {
        for (int i = 0; i < Conts.listCart.size(); i++) {
            if (Conts.listCart.get(i).getCodeName().equalsIgnoreCase(cname))
                return true;
        }
        return false;
    }

    public static void deleteCard(String cname) {
        for (int i = 0; i < Conts.listCart.size(); i++) {
            if (Conts.listCart.get(i).getCodeName().equalsIgnoreCase(cname)) {
                Conts.listCart.remove(i);
                return;
            }
        }
    }

    public static void addCart(String codeName, String codeDescription, String detailImg) {
        Code cart = new Code();
        cart.setCodeName(codeName);
        cart.setCodeDescription(codeDescription);
        cart.setDetailImg(detailImg);
        Conts.listCart.add(cart);
    }
}
