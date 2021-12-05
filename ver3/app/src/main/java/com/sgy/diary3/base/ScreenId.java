package com.sgy.diary3.base;

import com.sgy.diary3.ui.activty.LoginActivity;
import com.sgy.diary3.ui.activty.MainActivity;
import com.sgy.diary3.ui.activty.RegisterActivity;
import com.sgy.diary3.ui.splash.SplashActivity;
// TODO 화면 추가 될 때마다 이 곳에 작업
public class ScreenId {
    // ********** Activity ***********
    public static final String TAG_ACT_SPLASH = SplashActivity.class.getName();
    public static final String TAG_ACT_LOGIN = LoginActivity.class.getName();
    public static final String TAG_ACT_REGIST = RegisterActivity.class.getName();

    /* TEMP Activity */
    public static final String TAG_ACT_MAIN = MainActivity.class.getName();
}
