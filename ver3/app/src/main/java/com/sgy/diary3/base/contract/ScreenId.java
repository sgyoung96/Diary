package com.sgy.diary3.base.contract;

import com.sgy.diary3.ui.activty.LoginActivity;
import com.sgy.diary3.ui.activty.MyDiaryMainActivity;
import com.sgy.diary3.ui.activty.RegisterActivity;
import com.sgy.diary3.ui.activty.temp.TempActivity;
import com.sgy.diary3.ui.splash.SplashActivity;
// TODO 화면 추가 될 때마다 이 곳에 작업
public class ScreenId {
    // ********** Activity ***********
    public static final String TAG_ACT_SPLASH = SplashActivity.class.getSimpleName();
    public static final String TAG_ACT_LOGIN = LoginActivity.class.getSimpleName();
    public static final String TAG_ACT_REGIST = RegisterActivity.class.getSimpleName();
    public static final String TAG_ACT_MY_MAIN = MyDiaryMainActivity.class.getSimpleName();

    // ********** Fragment ***********
    public static final String TAG_FRG_MY_MAIN = MyDiaryMainActivity.class.getSimpleName();

    /* TEMP Activity */
    public static final String TAG_ACT_TEMP = TempActivity.class.getSimpleName();
}
