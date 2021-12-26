package com.sgy.diary3.ui.activty;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.UserProfile;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityMyDiaryMainBinding;
import com.sgy.diary3.ui.adapter.MainBottomAdapter;
import com.sgy.diary3.ui.data.MainBottomItem;
import com.sgy.diary3.ui.fragment.RecentFragment;
import com.sgy.diary3.util.LoginUtil;
import com.sgy.diary3.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Unit;
import okhttp3.internal.Util;

public class MyDiaryMainActivity extends BaseActivity {

    private ActivityMyDiaryMainBinding binding = null;

    /* Bottom Menu - RecyclerView */
    private MainBottomAdapter mainAdapter;
    public ArrayList<MainBottomItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyDiaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        setClickListener();
        getKakaoUserInfo();
        setUserInfo();

        setScreen();
    }

    @Override
    protected void resumeActivity() {
        getKakaoUserInfo();
        setUserInfo();
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    protected void backButtonPressed() {
        finishActivity();
    }

    @Override
    protected void destroyedActivity() {

    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************

    private void initView() {
        /* padding 설정 */
        binding.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, 0);
        binding.vgMain.setPadding(0, 0, 0, Utils.getNavigationBarHeight());
        binding.drawerContainer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        /* bottom menu RecyclerView */
        initBottomMenu();
    }

    private void setClickListener() {
        binding.ivLogoTop.setOnClickListener(v -> gotoMain(Utils.getTag(this)));
        binding.ivMenuTop.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, true));
        binding.drawer.ivClose.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, false));
        binding.drawer.vgLogout.setOnClickListener(v -> kakaoLogout());
    }

    /**
     * Fragment 관련 - 하단 RecyclerView 로 구성한 메뉴 클릭 시마다 Fragment 전환
     */
    public void setScreen() {
        getSupportFragmentManager().beginTransaction().replace(binding.vgMainContainer.getId(), new RecentFragment()).commit();
    }

    /**
     * 하단 메뉴 RecyclerView 데이터 생성 및 어댑터 붙이기
     */
    private void initBottomMenu() {
        /* set data */
        items = new ArrayList<>();
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent)));     // 최근목록
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_list), getString(R.string.menu_my_list)));        // 내 일기 목록
        items.add(new MainBottomItem(null, null));                                   // 빈 공간
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_drawer), getString(R.string.menu_drawer)));      // 내 서랍
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_memo), getString(R.string.menu_1_line)));       // 한줄 일기

        /* init adapter */
        mainAdapter = new MainBottomAdapter(this, items);
        binding.rvMainMenu.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
    }

    // ****************************************************************************************
    // Drawer Layout 관련
    // ****************************************************************************************

    /**
     * kakao 로그인 시 사용자 정보 받아오기
     * - 카카오 로그인 버튼 클릭 시만 사용자 정보 받아오는 함수를 타므로, 시작점이 되는 곳에서 한 번 더 호출해 준다.
     */
    private void getKakaoUserInfo() {
        LoginUtil loginUtil = new LoginUtil(this);
        loginUtil.getKakaoUserInfo(this);
    }

    /**
     * 사용자 정보 화면에 나타내기
     */
    private void setUserInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.drawer.tvNickName.setText(UserProfile.getInstance().nickName);
                Utils.mLog(Utils.getTag(MyDiaryMainActivity.this), UserProfile.getInstance().nickName);
            }
        }, 500);
    }

    /**
     * 카카오 로그아웃
     */
    private void kakaoLogout() {
        UserApiClient.getInstance().logout(error -> {
            if (error != null) {
                Utils.mToast(getString(R.string.system_kakao_logout_fail));
                Utils.mLog(Utils.getTag(this), getString(R.string.kakao_logout_fail));
            } else {
                Utils.mToast(getString(R.string.system_kakao_logout_success));
                gotoMain(Utils.getTag(this));
            }
            return null;
        });
    }
}