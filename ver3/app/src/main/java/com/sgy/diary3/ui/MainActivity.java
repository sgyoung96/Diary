package com.sgy.diary3.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sgy.diary3.R;
import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.util.Utils;

import jp.wasabeef.glide.transformations.BlurTransformation;

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