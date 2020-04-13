package com.example.fleamarket.utils;


import android.content.Context;

import com.example.fleamarket.R;
import com.zyao89.view.zloading.ZLoadingDialog;

import static com.zyao89.view.zloading.Z_TYPE.STAR_LOADING;

public class LoadingUtil {

    public static ZLoadingDialog loading(Context context,String hintText) {
        ZLoadingDialog dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(STAR_LOADING)//设置类型
                .setLoadingColor(R.color.app_maincolor)//颜色
                .setHintTextSize(12)
                .setHintText(hintText)
                .show();
        return dialog;
    }
}
