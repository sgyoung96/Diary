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

        /* init view - 1. [BaseActivity] StatusBar, NavigationBar 높이만큼 padding 2. [DrawerLayout] 닫힌 상태 3. [Layout] 상단 아이템 클릭 리스너 설정 */
        /**
         * init view
         * 1. [BaseActivity] StatusBar, NavigationBar 높이만큼 padding 설정
         * 2. [BaseLayout] - 로고, 메뉴, 드로어 메뉴 클릭 리스너 설정
         */
        binding.vgSub.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.vgMain.binding.ivLogoTop.setOnClickListener(v -> gotoMain(Utils.getTag(MainActivity.this)));
        binding.vgMain.binding.ivMenuTop.setOnClickListener(v -> setDrawerVisible(binding.vgMain.binding.drawerContainer, binding.vgMain.binding.drawerMain, true));
        binding.vgMain.binding.drawer.ivClose.setOnClickListener(v -> setDrawerVisible(binding.vgMain.binding.drawerContainer, binding.vgMain.binding.drawerMain, true));
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