package com.sgy.diary3.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.R;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.databinding.ActivitySplashBinding;
import com.sgy.diary3.ui.splash.SplashActivity;
import com.sgy.diary3.util.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * LifeCycle - onCreate, onResume
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 전체화면 설정 (StatusBar, NavigationBar 까지 화면 확장) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // 스크린 확장
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resumeActivity();
    }

    @Override
    public void onBackPressed() {
        this.backButtonPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.destroyedActivity();
    }

    /**
     * Custom LifeCycle
     */
    protected abstract void resumeActivity();
    protected abstract void backButtonPressed();
    protected abstract void destroyedActivity();

    /**
     * onBackPressed 함수 안에 삽입할 함수 : 두 번 클릭하면 어플리케이션 완전 종료
     */
    long waitTime = 0;
    public void finishActivity() { // onBackPressed 시 호출할 함수
        if (System.currentTimeMillis() - waitTime >= 1000) {
            waitTime = System.currentTimeMillis();
            Utils.mToast(MyApplication.context.getString(R.string.back_pressed));
        } else {
            /* 앱 프로세스 삭제 */
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * EditText 시 화면 터치했을 때 키보드 내리기
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!rect.contains(rawX, rawY)) {
                    view.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // **********************************************************
    // Intent 공통 함수 (좌 상단 로고 클릭 시) TODO 화면 추가 될 때마다 이 곳에 작업
    // **********************************************************

    public void gotoMain(String tag) {
        if (tag.equals(ScreenId.TAG_ACT_SPLASH)) {
            return; // Splash Activity 로고 없음
        } else if (tag.equals(ScreenId.TAG_ACT_LOGIN)) {
            return; // Login Activity 로고 없음
        } else if (tag.equals(ScreenId.TAG_ACT_MAIN)) { // TODO 추수 후정 : 타 경로에서 메인이 되는 액티비티로 이동
            Intent goSplash = new Intent(this, SplashActivity.class);
            goSplash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goSplash);
            overridePendingTransition(0,0);
        }
    }
}
