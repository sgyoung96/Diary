package com.sgy.diary3.base.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.sgy.diary3.R;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.base.contract.ScreenId;
import com.sgy.diary3.ui.activty.LoginActivity;
import com.sgy.diary3.ui.activty.temp.MainActivity2;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resumeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.destroyedActivity();
    }

    @Override
    public void onBackPressed() {
        this.backButtonPressed();
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
    public void finishActivity() {
        if (System.currentTimeMillis() - waitTime >= 1000) {
            waitTime = System.currentTimeMillis();
            Utils.mToast(MyApplication.context.getString(R.string.back_pressed));
        } else {
            /* 1. 액티비티 종료 2. 프로세스 종료 */
            ActivityCompat.finishAffinity(this);
            System.exit(0);
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

    /**
     * DrawerLayout 에 대한 visibility 처리
     * @param isVisible
     */
    public void setDrawerVisible(View drawer, DrawerLayout drawerContainer, boolean isVisible) {
        if ( !drawer.hasOnClickListeners() ) {
            drawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if ( null != drawerContainer && null != drawer ) {
            if (isVisible) {
                drawerContainer.openDrawer(drawer);
            } else {
                drawerContainer.closeDrawer(drawer);
            }
        }
    }

    // **********************************************************
    // Intent 공통 함수 (좌 상단 로고 클릭 시) TODO 화면 추가 될 때마다 이 곳에 작업, 추후 중복 코드 제거
    // **********************************************************

    /**
     * 1. 언제든지 이동할 수 있는 액티비티는 화면 전환 시 finish 시키고, SINGLE_TOP 플래그 사용하기 : 스택에 살아 있는 기존 액티비티를 최상단에 띄운다.
     * 2. 계정 생성 화면에서는 로그인 화면이 메인 화면 (로고 클릭 시 SplashActivity -> LoginActivity)
     * 3. LoginActivity 에는 로고 없으므로 정의하지 않음
     * @param tag
     */
    public void gotoMain(String tag) {
        if (tag.equals(ScreenId.TAG_ACT_SPLASH)) { // Splash Activity 로고 없음, 앱 실행 시 자동로그인 체크
            if (MyApplication.isKakaoLogin == 0) { // 로그아웃 상태 -> 로그인 화면으로 이동
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            } else { // 토큰 값이 있음 : 로그인 상태 (자동로그인) -> 메인 화면으로 이동 (tmep main activity)
                Intent intent = new Intent(this, MainActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        } else if (tag.equals(ScreenId.TAG_ACT_REGIST)) { // 회원가입 화면 - splash -> main
            Intent goSplash = new Intent(this, SplashActivity.class);
            goSplash.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(goSplash);
            overridePendingTransition(0,0);
            finish();
        } else if (tag.equals(ScreenId.TAG_ACT_MAIN2)) { // TODO 추수 후정 : 타 경로에서 메인이 되는 액티비티로 이동
            Intent goSplash = new Intent(this, SplashActivity.class);
            goSplash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(goSplash);
            overridePendingTransition(0,0);
        }
    }
}
