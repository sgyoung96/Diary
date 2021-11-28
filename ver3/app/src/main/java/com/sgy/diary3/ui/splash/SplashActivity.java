package com.sgy.diary3.ui.splash;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.util.Utils;

public class SplashActivity extends BaseActivity {
    @Override
    public void setStatusbarPadding(int height) {
        // No Contents
    }

    @Override
    protected String getActivity() {
        return Utils.getTag(this);
    }

    @Override
    protected void completeBinding() {
        MyApplication.context = getApplicationContext(); // init context
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void destroyActivity() {

    }
}