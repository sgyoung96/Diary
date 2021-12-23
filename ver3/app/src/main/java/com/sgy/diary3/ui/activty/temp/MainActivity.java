package com.sgy.diary3.ui.activty.temp;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.sgy.diary3.R;
import com.sgy.diary3.base.UserProfile;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.util.LoginUtil;
import com.sgy.diary3.util.Utils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        // setBaseClickListener();
        getKakaoUserInfo();
        setUserInfo();
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
//    private void setBaseClickListener() {
//        binding.vgCustom.setOnBaseClickListener(new OnBaseClickListener() {
//            @Override
//            public void setBaseClickListener(String getViewClick) {
//                switch (getViewClick) {
//                    /* 로고 클릭 */
//                    case ClickFlag.TOP_LOGO_CLICK:
//                        gotoMain(Utils.getTag(MainActivity.this));
//                        break;
//                    /* 상단 메뉴 아이콘 */
//                    case ClickFlag.TOP_MENU_CLICK:
//                        setDrawerVisible(binding.vgCustom.binding.drawerContainer, binding.vgCustom.binding.drawerMain, true);
//                        break;
//                    /* 드로어 닫기 아이콘 */
//                    case ClickFlag.DRAWER_ICON_CLOSE:
//                        setDrawerVisible(binding.vgCustom.binding.drawerContainer, binding.vgCustom.binding.drawerMain, false);
//                        break;
//                }
//            }
//        });
//    }

    /**
     * kakao 로그인 시 사용자 정보 받아오기
     * - 카카오 로그인 버튼 클릭 시만 사용자 정보 받아오는 함수를 타므로, 시작점이 되는 곳에서 한 번 더 호출해 준다.
     */
    private void getKakaoUserInfo () {
        LoginUtil loginUtil = new LoginUtil(this);
        loginUtil.getKakaoUserInfo(this);
    }

    /**
     * 사용자 정보 화면에 나타내기
     */
    private void setUserInfo () {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // binding.vgCustom.binding.drawer.tvNickName.setText(UserProfile.getInstance().nickName);
                Utils.mLog(Utils.getTag(MainActivity.this), UserProfile.getInstance().nickName);
            }
        }, 500);
    }
}