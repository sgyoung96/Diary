package com.sgy.diary3.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.databinding.ItemBottomMenuBinding;
import com.sgy.diary3.ui.data.MainBottomItem;
import com.sgy.diary3.util.Utils;

public class MainBottomHolder extends RecyclerView.ViewHolder {

    private ItemBottomMenuBinding binding = null;

    public MainBottomHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemBottomMenuBinding.bind(itemView);
    }

    public void bind(MainBottomItem item, int position) {
        /* init - 첫번째 아이콘 선택 모드 */
        if (position == 0) {
            binding.ivIcon.setImageDrawable(item.iconOn);
        } else {
            binding.ivIcon.setImageDrawable(item.iconOff);
        }

        /* 메뉴 텍스트 설정 */
        binding.tvTitle.setText(item.title);


    }
}
