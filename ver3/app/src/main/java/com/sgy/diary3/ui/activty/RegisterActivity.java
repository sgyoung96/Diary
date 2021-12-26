package com.sgy.diary3.ui.activty;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sgy.diary3.R;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityRegisterBinding;
import com.sgy.diary3.util.Utils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        setClickListener();
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void destroyedActivity() {

    }

    private void initView() {
        /* padding 설정 */
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
    }

    private void setClickListener() {
        /* 상단 로고 클릭시 Splash -> Login Activity */
        binding.ivLogoTop.setOnClickListener( v -> gotoMain(Utils.getTag(RegisterActivity.this))); // 로고 클릭시 메인 화면으로 이동 (drawer 없음)
    }
}