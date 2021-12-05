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

        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        // Glide.with(this).load(R.drawable.title_view_blur).apply(RequestOptions.bitmapTransform(new BlurTransformation(23, 3))).into(binding.ivViewTitle);

        binding.ivLogoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMain(Utils.getTag(MainActivity.this));
            }
        });

        //TODO DrawerLayout 디자인 및 기능 추가
        binding.ivMenuTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerMain.openDrawer(Gravity.RIGHT);
                if (binding.drawerMain.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerMain.closeDrawer(GravityCompat.START);
                }
            }
        });
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