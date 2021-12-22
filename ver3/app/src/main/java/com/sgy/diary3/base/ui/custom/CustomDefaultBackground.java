package com.sgy.diary3.base.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sgy.diary3.base.contract.ClickFlag;
import com.sgy.diary3.base.contract.OnBaseClickListener;
import com.sgy.diary3.databinding.LayoutDefaultBgTopBinding;
import com.sgy.diary3.util.Utils;

/**
 * 모든 화면에 사용할 기본적인 배경과 UI (상단 메뉴) 설정
 */
public class CustomDefaultBackground extends ConstraintLayout {

    /* View Binding */
    public LayoutDefaultBgTopBinding binding = null;

    /* Interface */
    public OnBaseClickListener onBaseClickListener;

    public void setOnBaseClickListener(OnBaseClickListener onBaseClickListener) {
        this.onBaseClickListener = onBaseClickListener;
    }

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
     * Init View
     * @param context
     */
    private void initCustomBg(Context context) {
        /* init View Binding */
        binding = LayoutDefaultBgTopBinding.inflate(LayoutInflater.from(context), this, true);

        /* set Padding */
        binding.vgHeader.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());
        binding.drawerContainer.setPadding(0, Utils.getStatusbarHeight(), 0, Utils.getNavigationBarHeight());

        /* set Click Listener */
        binding.ivLogoTop.setOnClickListener(v -> onBaseClickListener.setBaseClickListener(ClickFlag.TOP_LOGO_CLICK));
        binding.ivMenuTop.setOnClickListener(v -> onBaseClickListener.setBaseClickListener(ClickFlag.TOP_MENU_CLICK));
        binding.drawer.ivClose.setOnClickListener(v -> onBaseClickListener.setBaseClickListener(ClickFlag.DRAWER_ICON_CLOSE));
    }
}
