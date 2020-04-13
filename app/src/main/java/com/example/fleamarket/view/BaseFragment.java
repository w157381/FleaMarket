package com.example.fleamarket.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 *
 * 所有Fragment基类
 *
 * Created by 王鹏飞
 * on 2020-02-13
 * **/
public abstract class BaseFragment extends Fragment {

    /**
     * 绑定布局
     *
     * **/
    public abstract int bindLaout();

    /**
     * 初始化视图
     *
     *
     * **/
    public abstract void initView(View view, Bundle bundle);

    /**
     * 业务逻辑操作
     *
     *
     * **/
    public abstract void doBussiness();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(bindLaout(),container,false);
        initView(view,savedInstanceState);
        doBussiness();
        return view;
    }
}
