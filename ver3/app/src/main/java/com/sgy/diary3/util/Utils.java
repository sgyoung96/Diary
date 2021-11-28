package com.sgy.diary3.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sgy.diary3.R;
import com.sgy.diary3.base.MyApplication;

public class Utils {
    public static String getTag(Context context) {
        return context.getClass().getName();
    }

    public static void rawLog(String tag, String msg) {
        Log.d(MyApplication.context.getString(R.string.debug_tag) + "[" + tag + "]", msg);
    }
    public static void mLog(String msg) {
        Utils.rawLog(Utils.getTag(MyApplication.context), msg);
    }
    public static void mToast(String msg) { Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show(); }
}
