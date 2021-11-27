package com.sgy.diaryrenewal.base;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.sgy.diaryrenewal.databinding.ActivitySplashBinding;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    ViewBinding binding = null;

    protected abstract String createActivity();
    protected abstract void compelteBinding();
    protected abstract void onstartActivity();
    protected abstract void resumeActivity();
    protected abstract void pauseActivity();
    protected abstract void stopActivity();
    protected abstract void restartActivity();
    protected abstract void destroyActivity();

    protected abstract void clickView(View view);

    // ************************************************************************************
    // Life Cycle
    // ************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState); // 재부팅 등등의 상황에서 예전에 받은 인텐트를 재사용하기 위해 사용하는 함수
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActivityType(this.createActivity());
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.onstartActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resumeActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.pauseActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.stopActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.restartActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.destroyActivity();
    }

    // ************************************************************************************
    // COMMON EVNET
    // ************************************************************************************

    @Override
    public void onClick(View v) {
        this.clickView(v);
    }

    /**
     * Activity type 추가
     * @param screenId
     */
    private void showActivityType(String screenId) {
        switch (screenId) {
            case ScreenId.ACTIVITY_SPLASH: {
                binding = ActivitySplashBinding.inflate(getLayoutInflater());
            }
        }
        setContentView(binding.getRoot());
        this.compelteBinding();
    }

    /**
     * 키보드 자판 내리기
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

    class EditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            return false;
        }
    }
}
