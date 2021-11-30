package com.sgy.diary3.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.databinding.ActivitySplashBinding;
import com.sgy.diary3.ui.MainActivity;
import com.sgy.diary3.util.Utils;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.context = getApplicationContext(); // init context
        binding = ActivitySplashBinding.inflate(getLayoutInflater()); // init binding
        setContentView(binding.getRoot());

        Utils.getAppHashKey(); // 카카오 로그인 연동을 위한 앱 해시 키 Log 로 찍기

        binding.splashLottie.playAnimation(); // lottie animation start

        new Handler().postDelayed(new Runnable() { // gotoMain
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }, 1500);
    }

    @Override
    protected void resumeActivity() {
        // 화면 전환 시 finish() 를 시켜버렸으므로 이 함수 안 탐
    }

    @Override
    protected void backButtonPressed() {
        finish();
    }

    @Override
    protected void destroyedActivity() {

    }
}