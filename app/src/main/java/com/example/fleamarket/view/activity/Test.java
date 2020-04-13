package com.example.fleamarket.view.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.fleamarket.R;
import com.example.fleamarket.view.BaseActivity;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.zia.toastex.ToastEx;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.library.adapter.SimpleBulletinAdapter;
import me.bakumon.library.view.BulletinView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class Test extends BaseActivity {
    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;
    @Override
    protected int initLayout() {
        return R.layout.test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mBasIn = new BounceTopEnter();
        mBasOut = new SlideBottomExit();

    }

    public void aaa(View view){
        NormalDialogCustomAttr();

    }
    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#FFFFFF"))//
                .cornerRadius(10)//
                .content("是否确定退出程序?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#000000"))//
                .dividerColor(Color.parseColor("#80222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#000000"), Color.parseColor("#000000"))//
                .btnPressColor(Color.parseColor("#80222222"))//
                .widthScale(0.85f)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                       ToastEx.success(Test.this,"111").show();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        ToastEx.success(Test.this,"222").show();
                        dialog.dismiss();
                    }
                });
    }
    private void MaterialDialogDefault() {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                "嗨！这是一个 MaterialDialogDefault. 它非常方便使用，你只需将它实例化，这个美观的对话框便会自动地显示出来。"
                        + "它简洁小巧，完全遵照 Google 2014 年发布的 Material Design 风格，希望你能喜欢它！^ ^")//
                .btnText("取消", "确定")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        ToastEx.success(Test.this,"111").show();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        ToastEx.success(Test.this,"222").show();
                        dialog.dismiss();
                    }
                }
        );
    }
}
