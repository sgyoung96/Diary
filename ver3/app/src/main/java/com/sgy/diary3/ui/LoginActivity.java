package com.sgy.diary3.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.BaseActivity;
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

        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        Glide.with(this).load(R.drawable.splash_view_circle).apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1))).into(binding.ivView);

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

    public void loginKakao() {
        binding.ivKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil util = new LoginUtil();
                loginCallback = util.loginCallback;
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(context, loginCallback);
                    util.getKakaoUserInfo();
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(context, loginCallback);
                }
            }
        });
    }
}