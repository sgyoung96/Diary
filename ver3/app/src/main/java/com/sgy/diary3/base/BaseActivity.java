package com.sgy.diary3.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.databinding.ActivitySplashBinding;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * default setting
     * 1. binding
     * 2. Tag
     * 3. StatusBar
     */
    public static ViewBinding binding = null;
    public static String TAG = null;

    // Layout 에 statusbar 높이만큼 padding 주기 (Main ViewGroup(; ContraintLayout) 말고!) -> view 가 statusbar 와 겹쳐 보이지 않도록
    public abstract void setStatusbarPadding(int height);
    public int getStatusbarHeight() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return this.getResources().getDimensionPixelOffset(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * LifeCycle - onCreate, onResume
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getActivity();
        setActivityBinding();

        // StatusBar 투명 설정 (1. 스크린 확장 2. StatusBar 높이만큼 Padding 줘서 StatusBar Contents 보이게끔 처리)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setStatusbarPadding(getStatusbarHeight());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resumeActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.backButtonPressed();
    }

    /**
     * show Activity by TAG
     */
    abstract protected String getActivity();
    public void setActivityBinding() {
        if (TAG.equals(ScreenId.TAG_ACT_SPLASH)) {
            binding = ActivitySplashBinding.inflate(getLayoutInflater());
        } else if (TAG.equals(ScreenId.TAG_ACT_MAIN)) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
        }

        setContentView(binding.getRoot());
        this.completeBinding();
    }

    /**
     * Binding 처리 끝난 후 로직
     */
    protected abstract void completeBinding();

    /**
     * Custom LifeCycle
     */
    protected abstract void resumeActivity();
    protected abstract void backButtonPressed();

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
}
