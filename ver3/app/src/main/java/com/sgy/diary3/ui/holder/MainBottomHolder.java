package com.sgy.diary3.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgy.diary3.databinding.ItemBottomMenuBinding;
import com.sgy.diary3.ui.data.MainBottomItem;

public class MainBottomHolder extends RecyclerView.ViewHolder {

    private ItemBottomMenuBinding binding = null;

    public MainBottomHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemBottomMenuBinding.bind(itemView);
    }

    public void bind(MainBottomItem item) {
        binding.ivIcon.setImageDrawable(item.icon);
        binding.tvTitle.setText(item.title);
    }
}
