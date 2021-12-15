package com.sgy.diary3.ui.activty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.util.Utils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // TODO : 커스텀뷰로 만들기 (공통 배경 화면)
        /* init view - 1. [BaseActivity] StatusBar, NavigationBar 높이만큼 padding 2. [DrawerLayout] 닫힌 상태 3. [Layout] 상단 아이템 클릭 리스너 설정 */
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.drawer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        setDrawerVisible(binding.drawer, binding.drawerMain, false);
        binding.vgTop.topContainer.ivLogoTop.setOnClickListener(v -> gotoMain(Utils.getTag(MainActivity.this)));
        binding.vgTop.topContainer.ivMenuTop.setOnClickListener(v -> setDrawerVisible(binding.drawer, binding.drawerMain, true));
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
}