package com.sgy.diary3.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sgy.diary3.databinding.LayoutDefaultBgTopBinding;
import com.sgy.diary3.util.Utils;

/**
 * 모든 화면에 사용할 기본적인 배경과 UI (상단 메뉴) 설정
 */
public class CustomDefaultBackground extends ConstraintLayout {

    /* View Binding */
    public LayoutDefaultBgTopBinding binding = null;

    // ************************************************************************
    // Contructor
    // ************************************************************************

    public CustomDefaultBackground(@NonNull Context context) {
        super(context);
        initCustomBg(context);
    }

    public CustomDefaultBackground(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomBg(context);
    }

    // ************************************************************************
    // Methods
    // ************************************************************************

    /**
     * initialize
     * @param context
     */
    private void initCustomBg(Context context) {
        /* init View Binding */
        binding = LayoutDefaultBgTopBinding.inflate(LayoutInflater.from(context), this, true);

        /* set Padding */
        binding.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.drawer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
    }
}
