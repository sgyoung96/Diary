package com.sgy.diary3.ui.activty;

import android.os.Bundle;
import android.view.View;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.databinding.ActivityRegisterBinding;
import com.sgy.diary3.util.Utils;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* init View -- 1. [BaseActivity] StatusBar, NavigationBar 높이 만큼 전체 콘텐츠에 padding 2. [DrawerLayout] GONE 처리 3. [Layout] 상단 아이템 클릭 리스너 설정 */
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.vgBgContainer.binding.ivMenuTop.setVisibility(View.GONE);
        binding.vgBgContainer.binding.ivLogoTop.setOnClickListener( v -> gotoMain(Utils.getTag(RegisterActivity.this))); // 로고 클릭시 메인 화면으로 이동 (drawer 없음)
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() {

    }

    @Override
    protected void destroyedActivity() {

    }
}