package com.sgy.diary3.base;

import android.app.Application;
import android.content.Context;

import com.kakao.sdk.common.KakaoSdk;
import com.sgy.diary3.R;

public class MyApplication extends Application {
    public static Context context = null;
    public static int isLogin = 0; // 0 : 로그아웃 상태 1 : 로그인 상태

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getResources().getString(R.string.native_key));
    }
}
