package com.sgy.diary3.ui.activty;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sgy.diary3.R;
import com.sgy.diary3.base.contract.ClickFlag;
import com.sgy.diary3.base.contract.OnBaseClickListener;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.util.Utils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        setBaseClickListener();
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

    /**
     * init view
     * 1. [BaseActivity] StatusBar, NavigationBar 높이만큼 padding 설정
     * 2. [CustomBottomNavigation] menu 연결
     */
    private void initView () {
        /* set padding */
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        /* custom bottom navigation view 메뉴 및 padding 설정 */
        binding.vgBottomNavigation.inflateMenu(R.menu.menu_bottom_navigation);
        binding.vgBottom.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
    }

    /**
     * 공통 Click Listener 설정
     * 1. 상단 로고 및 메뉴
     * 2. 드로어 레이아웃
     */
    private void setBaseClickListener() {
        binding.vgCustom.setOnBaseClickListener(new OnBaseClickListener() {
            @Override
            public void setBaseClickListener(String getViewClick) {
                switch (getViewClick) {
                    /* 로고 클릭 */
                    case ClickFlag.TOP_LOGO_CLICK:
                        gotoMain(Utils.getTag(MainActivity.this));
                        break;
                    /* 상단 메뉴 아이콘, 드로어 닫기 아이콘 */
                    case ClickFlag.TOP_MENU_CLICK:
                    case ClickFlag.DRAWER_ICON_CLOSE:
                        setDrawerVisible(binding.vgCustom.binding.drawerContainer, binding.vgCustom.binding.drawerMain, true);
                        break;
                }
            }
        });
    }
}