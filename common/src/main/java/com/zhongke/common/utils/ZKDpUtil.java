package com.zhongke.common.utils;

import android.content.Context;

import com.zhongke.common.base.application.ZKBaseApplication;

/**
 * Created by cxf on 2017/8/9.
 * dp转px工具类
 */

public class ZKDpUtil {

    private static float scale;

    static {
        scale = ZKBaseApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dpVal) {
        return (int) (scale * dpVal + 0.5f);
    }

    public static int dp2px(float dp) {
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
