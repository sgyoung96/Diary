package com.sgy.diary3.ui.activty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityLoginBinding;
import com.sgy.diary3.util.LoginUtil;
import com.sgy.diary3.util.Utils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding = null;
    private Function2<OAuthToken, Throwable, Unit> loginCallback = null;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* init view - 1. [BaseActivity] StatusBar, NavigationBar 높이 만큼 전체 콘텐츠에 padding 2. [Custom] blur */
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        Glide.with(this).load(R.drawable.splash_view_circle).apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1))).into(binding.ivView);

        registerAccount(); // 계정 생성
        loginKakao(); // 카카오톡 연계 로그인
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() {
        finishActivity();
    }

    @Override
    protected void destroyedActivity() {

    }

    private void loginKakao() { // 카카오 로그인 클릭했을 시
        binding.ivKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil util = new LoginUtil();
                loginCallback = util.loginCallback;
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) { // 카카오톡 설치 되어 있을 시 카카오톡 로그인
                    UserApiClient.getInstance().loginWithKakaoTalk(context, loginCallback);
                    util.getKakaoUserInfo();
                } else { // 카카오톡 미설치 시 카카오 계정으로 로그인 - test 안 해봄
                    UserApiClient.getInstance().loginWithKakaoAccount(context, loginCallback);
                }
            }
        });
    }

    private void registerAccount() { // 계정 생성 버튼 클릭했을 시 -> 화면 전환
        binding.tvMakeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}