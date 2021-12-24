package com.sgy.diary3.ui.data;

import android.graphics.drawable.Drawable;

public class MainBottomItem {
    public Drawable icon = null;
    public String title = "";

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MainBottomItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }
}
