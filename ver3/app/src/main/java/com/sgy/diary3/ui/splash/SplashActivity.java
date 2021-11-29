package com.sgy.diary3.ui.splash;

import android.content.Intent;
import android.os.Handler;

import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.databinding.ActivitySplashBinding;
import com.sgy.diary3.ui.MainActivity;
import com.sgy.diary3.util.Utils;
// TODO Deprecated 된 Intent 찾기, 웹포토샵작업(배경 밝게) + Lottie Animation 추가하기 + Navigation bar 는 회색으로 나타나도록 수정
public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding = null;

    @Override
    public void setStatusbarPadding(int height) {
        binding.vgMain.setPadding(0, getStatusbarHeight(), 0, 0);
    }

    @Override
    protected String getActivity() {
        return Utils.getTag(this);
    }

    @Override
    protected void completeBinding() {
        MyApplication.context = getApplicationContext(); // init context
        binding = ActivitySplashBinding.inflate(getLayoutInflater()); // init binding

        binding.splashLottie.playAnimation(); // lottie animation start

        new Handler().postDelayed(new Runnable() {
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

    }

    @Override
    protected void backButtonPressed() {
        finish();
    }
}