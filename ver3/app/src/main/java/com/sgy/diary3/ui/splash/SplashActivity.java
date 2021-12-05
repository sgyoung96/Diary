package com.sgy.diary3.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.databinding.ActivitySplashBinding;
import com.sgy.diary3.ui.LoginActivity;
import com.sgy.diary3.util.Utils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.context = getApplicationContext(); // init context
        binding = ActivitySplashBinding.inflate(getLayoutInflater()); // init binding
        setContentView(binding.getRoot());

        /* init view - 1. lottie 2. blur */
        binding.splashLottie.playAnimation();
        Glide.with(this).load(R.drawable.splash_view_circle).apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1))).into(binding.ivView);

        MyApplication.isLogin = 0; // 로그인 상태 초기화 - 0 : 로그아웃 상태 1 : 로그인 상태

        Utils.getAppHashKey(); // 카카오 로그인 연동을 위한 앱 해시 키 Log 로 찍기

        autoLoginGetTokenCheck();
    }

    @Override
    protected void resumeActivity() {
        // 화면 전환 시 finish() 를 시켜버렸으므로 이 함수 안 탐
    }

    @Override
    protected void backButtonPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());	// 이 액티비티 종료 후 로그인 액티비티 뜨는 것을 방지하기 위해 App Process 완전 종료
    }

    @Override
    protected void destroyedActivity() {

    }

    private void autoLoginGetTokenCheck() {
        /* Kakao 자동로그인 - Token 체크 */
        UserApiClient.getInstance().accessTokenInfo((tokenInfo, error) -> {
            if (error != null) {
                Utils.mLog(getString(R.string.kakao_token_info_fail));
            } else if (tokenInfo != null) {
                Utils.mLog(getString(R.string.kakao_token_info_success));
                MyApplication.isLogin = 1; // 토큰값을 가지고 있으므로 자동로그인 플래그 변경 (0 : 로그아웃 상태 1 : 로그인 상태)
            }
            return null;
        });

        new Handler().postDelayed(new Runnable() { // gotoMain
            @Override
            public void run() {
                if (MyApplication.isLogin == 0) { // 로그아웃 상태일 시 로그인 화면으로 이동
                    gotoMain(Utils.getTag(SplashActivity.this));
                } else { // 로그인 상태일 시 메인 화면으로 이동
                    gotoMain(Utils.getTag(SplashActivity.this));
                }
            }
        }, 1500);


    }
}