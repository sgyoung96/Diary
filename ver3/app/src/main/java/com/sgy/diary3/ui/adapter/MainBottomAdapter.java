package com.sgy.diary3.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgy.diary3.R;
import com.sgy.diary3.databinding.ItemBottomMenuBinding;
import com.sgy.diary3.ui.data.MainBottomItem;
import com.sgy.diary3.ui.holder.MainBottomHolder;
import com.sgy.diary3.util.Utils;

import java.util.ArrayList;

public class MainBottomAdapter extends RecyclerView.Adapter<MainBottomHolder> {

    private Context context;
    public ArrayList<MainBottomItem> bottomItems;
    public ItemBottomMenuBinding binding = null;

    public MainBottomAdapter(Context context, ArrayList<MainBottomItem> bottomItems) {
        this.context = context;
        this.bottomItems = bottomItems;
    }

    @NonNull
    @Override
    public MainBottomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBottomMenuBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MainBottomHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MainBottomHolder holder, int position) {
        Utils.mLog(Utils.getTag(context), "position : " + position);
        holder.bind(bottomItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        Utils.mLog(Utils.getTag(context), "bottom Items.size : " + bottomItems.size());
        return bottomItems.size();
    }
}
