package com.sgy.diary3.ui;

import android.widget.Toast;

import androidx.viewbinding.ViewBinding;

import com.sgy.diary3.base.BaseActivity;
import com.sgy.diary3.util.Utils;

public class MainActivity extends BaseActivity {

    @Override
    public void setStatusbarPadding(int height) {

    }

    @Override
    protected String getActivity() {
        return Utils.getTag(this);
    }

    @Override
    protected void completeBinding() {

    }

    @Override
    protected void resumeActivity() {

    }

    @Override
    protected void backButtonPressed() {

    }
}