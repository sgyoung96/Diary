package com.sgy.diary3.ui.data;

import android.graphics.drawable.Drawable;

public class MainBottomItem {
    public Drawable iconOn = null;
    public Drawable iconOff = null;
    public String title = "";

    public MainBottomItem(Drawable iconOn, Drawable iconOff, String title) {
        this.iconOn = iconOn;
        this.iconOff = iconOff;
        this.title = title;
    }
}
