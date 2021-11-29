package com.sgy.diary3.ui;

import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.R;
import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.databinding.ActivityMainBinding;
import com.sgy.diary3.util.Utils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding =  null;

    @Override
    protected String getActivity() {
        return Utils.getTag(this);
    }

    @Override
    protected void completeBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setScreenPadding(getStatusbarHeight(), getNavigationBarHeight(), binding.vgMain);
        binding.tvTop.setText(getResources().getString(R.string.app_name));
    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() {

    }
}