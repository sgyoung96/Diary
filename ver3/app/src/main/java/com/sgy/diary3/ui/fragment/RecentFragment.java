package com.sgy.diary3.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.sgy.diary3.base.ui.BaseFragment;
import com.sgy.diary3.databinding.FragmentRecentBinding;

public class RecentFragment extends BaseFragment {

    private FragmentRecentBinding binding = null;

    @Override
    protected View createFragment() {
        binding = FragmentRecentBinding.inflate(LayoutInflater.from(requireContext()), container, false);
        return binding.getRoot();
    }

    @Override
    protected void fragmentCreated() {

    }

    @Override
    protected void resumeFragment() {

    }

    @Override
    protected void destroyedFragment() {

    }
}