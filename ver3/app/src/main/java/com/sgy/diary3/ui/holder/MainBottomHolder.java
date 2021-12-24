package com.sgy.diary3.ui.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgy.diary3.R;
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

    public void bind(MainBottomItem item) {
        binding.ivIcon.setImageDrawable(item.icon);
        binding.tvTitle.setText(item.title);

        Utils.mLog(Utils.getTag(MyApplication.context), "title : " + item.title);
    }
}
