package com.sgy.diaryrenewal.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sgy.diaryrenewal.MyApplication;
import com.sgy.diaryrenewal.base.BaseActivity;
import com.sgy.diaryrenewal.base.ScreenId;
import com.sgy.diaryrenewal.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {

    private final String TAG = SplashActivity.class.getName();
    private ActivitySplashBinding binding = null;

    @Override
    protected String createActivity() {
        return ScreenId.ACTIVITY_SPLASH;
    }

    @Override
    protected void compelteBinding() {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onstartActivity() {

    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void pauseActivity() {

    }

    @Override
    protected void stopActivity() {

    }

    @Override
    protected void restartActivity() {

    }

    @Override
    protected void destroyActivity() {

    }

    @Override
    protected void clickView(View view) {

    }
}