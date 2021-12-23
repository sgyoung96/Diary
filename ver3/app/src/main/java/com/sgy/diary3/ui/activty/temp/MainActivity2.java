package com.sgy.diary3.ui.activty.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.sgy.diary3.R;
import com.sgy.diary3.base.ui.BaseActivity;
import com.sgy.diary3.databinding.ActivityMain2Binding;
import com.sgy.diary3.util.Utils;

public class MainActivity2 extends BaseActivity {

    private ActivityMain2Binding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        setClickListener();
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

    private void initView () {
        /* padding 설정 */
        binding.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, 0); //
        binding.vgMain.setPadding(0, 0, 0, Utils.getNavigationBarHeight());
        binding.drawerContainer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        /* bottom navigation view 에 메뉴 붙이기 */
        binding.vgBottomNavigation.inflateMenu(R.menu.menu_bottom_navigation);
        /* bottom fab */

    }

    private void setClickListener () {
        binding.ivLogoTop.setOnClickListener(v -> gotoMain(Utils.getTag(this)));
        binding.ivMenuTop.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, true));
        binding.drawer.ivClose.setOnClickListener(v -> setDrawerVisible(binding.drawerContainer, binding.drawerMain, false));
    }
}