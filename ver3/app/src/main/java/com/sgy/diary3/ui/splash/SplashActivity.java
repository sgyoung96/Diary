package com.sgy.diary3.ui.splash;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.util.Utils;
// TODO Deprecated 된 Intent 찾기, 웹포토샵작업(배경 밝게) + Lottie Animation 추가하기 + Navigation bar 는 회색으로 나타나도록 수정
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