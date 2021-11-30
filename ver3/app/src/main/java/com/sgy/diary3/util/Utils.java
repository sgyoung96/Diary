package com.sgy.diary3.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.sgy.diary3.R;
import com.sgy.diary3.base.MyApplication;

import java.security.MessageDigest;

public class Utils {
    public static void getAppHashKey() { // 로그로 확인
        PackageInfo packageInfo = null;
        try {
            packageInfo = MyApplication.context.getPackageManager().getPackageInfo(MyApplication.context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (Exception e) {
            mLog(e.toString());
        }
        if (packageInfo == null) {
            mLog(MyApplication.context.getString(R.string.hash_key_null));
        }
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                mLog(Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            } catch (Exception e) {
                mLog( "Unable to get MessageDoigest.signature = " + signature + ":: + ::" + e.toString());
            }
        }
    }

    /**
     * Activity 혹은 Fragment 의 식별정보 (TAG) 를 가져온다.
     * @param context
     * @return
     */
    public static String getTag(Context context) {
        return context.getClass().getName();
    }

    /**
     * Custmom Log 생성 (:: MY DEBUG :: [TAG], "msg")
     * @param tag
     * @param msg
     */
    public static void rawLog(String tag, String msg) {
        Log.d(MyApplication.context.getString(R.string.debug_tag) + "[" + tag + "]", msg);
    }
    public static void mLog(String msg) {
        Utils.rawLog(Utils.getTag(MyApplication.context), msg);
    }

    /**
     * Custom Toast 생성 - 사용법 : 인자로 메시지만 입력하면 됨
     * @param msg
     */
    public static void mToast(String msg) { Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show(); }

    /**
     * StatusBar 높이 구하기
     * @return
     */
    public static int getStatusbarHeight() {
        int resourceId = MyApplication.context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return MyApplication.context.getResources().getDimensionPixelOffset(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * NavigationBar 높이 구하기
     * @return
     */
    public static int getNavigationBarHeight() {
        int resourceId = MyApplication.context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return MyApplication.context.getResources().getDimensionPixelOffset(resourceId);
        } else {
            return 0;
        }
    }
}
