package com.sgy.diary3.ui;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.sgy.diary3.R;
import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.databinding.ActivityLoginBinding;
import com.sgy.diary3.util.Utils;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.vgMain.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        // 뷰에 블러 주기 - Glide.with(this).load(R.drawable.splash_view_circle).bitmapTransForm(new BlurTransformation(context)).into(binding.ivView);
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