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

        /* init View -- 1. base 2. custom */
        binding.layoutDefaultBg.layoutHeader.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.layoutDefaultBg.layoutHeader.ivMenuTop.setVisibility(View.GONE);
        binding.layoutDefaultBg.layoutHeader.ivLogoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMain(Utils.getTag(RegisterActivity.this));
            }
        });
        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
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