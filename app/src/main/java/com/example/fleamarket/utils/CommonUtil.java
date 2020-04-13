package com.example.fleamarket.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 常见工具类
 *
 * @author zhangwade
 */
public class CommonUtil {
    private static CommonUtil singleton;

    private CommonUtil() {

    }

    /**
     * 获取CommonUtil实例对象
     *
     * @return 实例对象
     */
    public static CommonUtil getInstance() {
        if (singleton == null) {
            synchronized (CommonUtil.class) {
                if (singleton == null) {
                    singleton = new CommonUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文对象
     * @return 版本名称
     */
    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文对象
     * @return 状态栏高度
     */
    public int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }


    /**
     * 获取屏幕宽度
     *
     * @param context 上下文对象
     * @return 屏幕宽度
     */
    public int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度（不包含虚拟按键）
     *
     * @param context 上下文对象
     * @return 屏幕高度
     */
    public int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 显示软键盘
     *
     * @param context 上下文对象
     * @param view    获取焦点的视图
     */
    public void showSoftInput(Context context, View view) {
        ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文对象
     * @param view    获取焦点的视图
     */
    public void hideSoftInput(Context context, View view) {
        ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * DP转为PX
     *
     * @param context 上下文对象
     * @param dpValue DP值
     * @return PX值
     */
    public int dp2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 获取SHA1值
     *
     * @param context 上下文对象
     * @return SHA1值
     */
    public String getSHA1String(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] publicKey = MessageDigest.getInstance("SHA1").digest(info.signatures[0].toByteArray());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param content 待加密内容
     * @return
     */
    public String md5(String content) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            return "";
        }
    }
}