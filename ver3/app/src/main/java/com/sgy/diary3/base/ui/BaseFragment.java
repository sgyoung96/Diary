package com.sgy.diary3.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment extends Fragment {

    /* 하위 Fragment 에서 inflate 할 때 필요 */
    protected ViewGroup container = null;

    /**
     * LifeCycle - onCreateView, onViewCreated, onResume, onDestroy
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = container;
        return this.createFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.resumeFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.destroyedFragment();
    }

    protected abstract View createFragment();
    protected abstract void fragmentCreated();
    protected abstract void resumeFragment();
    protected abstract void destroyedFragment();


}
