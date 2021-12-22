package com.sgy.diary3.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.databinding.ActivitySplashBinding;
import com.sgy.diary3.util.LoginUtil;
import com.sgy.diary3.util.Utils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding = null;

    private Function2<OAuthToken, Throwable, Unit> loginCallback = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.context = getApplicationContext(); // init context
        binding = ActivitySplashBinding.inflate(getLayoutInflater()); // init binding
        setContentView(binding.getRoot());

        /* init view - 1. lottie 2. StatusBar, NavigationBar Height set padding 3. blur */
        binding.splashLottie.playAnimation();
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        Glide.with(this).load(R.drawable.splash_view_circle).apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1))).into(binding.ivView);

        MyApplication.isKakaoLogin = 0; // 로그인 상태 초기화 - 0 : 로그아웃 상태 1 : 로그인 상태

        Utils.getAppHashKey(); // 카카오 로그인 연동을 위한 앱 해시 키 Log 로 찍기

        autoLoginGetTokenCheck();
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() { // 1. 액티비티 종료 2. 프로세스 종료
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    @Override
    protected void destroyedActivity() {

    }

    private void autoLoginGetTokenCheck() {
        /* Kakao 자동로그인 - Token 체크 */
        UserApiClient.getInstance().accessTokenInfo((tokenInfo, error) -> {
            if (error != null) {
                Utils.mLog(Utils.getTag(this), getString(R.string.kakao_token_info_fail));
            } else if (tokenInfo != null) {
                Utils.mLog(Utils.getTag(this), getString(R.string.kakao_token_info_success));
                MyApplication.isKakaoLogin = 1; // 토큰값을 가지고 있으므로 자동로그인 플래그 변경 (0 : 로그아웃 상태 1 : 로그인 상태)
            }
            return null;
        });

        new Handler().postDelayed(new Runnable() { // gotoMain
            @Override
            public void run() {
                if (MyApplication.isKakaoLogin == 0) { // 로그아웃 상태일 시 로그인 화면으로 이동
                    gotoMain(Utils.getTag(SplashActivity.this));
                } else { // 로그인 상태일 시 메인 화면으로 이동
                    gotoMain(Utils.getTag(SplashActivity.this));
                }
            }
        }, 1500);
    }
}