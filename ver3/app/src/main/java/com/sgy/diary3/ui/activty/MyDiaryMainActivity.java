package com.sgy.diary3.ui.activty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.sgy.diary3.R;
import com.sgy.diary3.base.UserProfile;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityMyDiaryMainBinding;
import com.sgy.diary3.ui.activty.temp.MainActivity;
import com.sgy.diary3.ui.adapter.MainBottomAdapter;
import com.sgy.diary3.ui.data.MainBottomItem;
import com.sgy.diary3.util.LoginUtil;
import com.sgy.diary3.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

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
    }

    @Override
    protected void resumeActivity() {
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    protected void backButtonPressed() {

    }

    @Override
    protected void destroyedActivity() {

    }

    private void initView () {
        /* padding 설정 */
        binding.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, 0);
        binding.vgMain.setPadding(0, 0, 0, Utils.getNavigationBarHeight());
        binding.drawerContainer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        /* bottom menu RecyclerView */
        initBottomMenu();
    }

    private void setClickListener () {
        binding.ivLogoTop.setOnClickListener(v -> gotoMain(Utils.getTag(this)));
        binding.ivMenuTop.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, true));
        binding.drawer.ivClose.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, false));
    }

    private void initBottomMenu() {
        /* set data */
        items = new ArrayList<>();
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent)));    // 최근목록
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_list), getString(R.string.menu_my_list)));     // 내 일기 목록
        items.add(new MainBottomItem(null, null));                                                          // 빈 공간
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_drawer), getString(R.string.menu_drawer)));    // 내 서랍
        items.add(new MainBottomItem(getDrawable(R.drawable.icon_menu_memo), getString(R.string.menu_1_line)));      // 한줄 일기

//        MainBottomItem item1 = new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent));
//        item1.setIcon(getDrawable(R.drawable.icon_menu_recent));
//        item1.setTitle(getString(R.string.menu_recent));
//
//        MainBottomItem item2 = new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent));
//        item2.setIcon(getDrawable(R.drawable.icon_menu_recent));
//        item2.setTitle(getString(R.string.menu_recent));
//
//        MainBottomItem item3 = new MainBottomItem(null, null);
//        item3.setIcon(getDrawable(R.drawable.icon_menu_recent));
//        item3.setTitle(getString(R.string.menu_recent));
//
//        MainBottomItem item4 = new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent));
//        item4.setIcon(getDrawable(R.drawable.icon_menu_recent));
//        item4.setTitle(getString(R.string.menu_recent));
//
//        MainBottomItem item5 = new MainBottomItem(getDrawable(R.drawable.icon_menu_recent), getString(R.string.menu_recent));
//        item5.setIcon(getDrawable(R.drawable.icon_menu_recent));
//        item5.setTitle(getString(R.string.menu_recent));
//
//        items = new ArrayList<>(Arrays.asList(item1, item2, item3, item4, item5));

        /* init adapter */
        mainAdapter = new MainBottomAdapter(this, items);
        binding.rvMainMenu.setAdapter(mainAdapter);
    }

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
                binding.drawer.tvNickName.setText(UserProfile.getInstance().nickName);
                Utils.mLog(Utils.getTag(MyDiaryMainActivity.this), UserProfile.getInstance().nickName);
            }
        }, 500);
    }
}