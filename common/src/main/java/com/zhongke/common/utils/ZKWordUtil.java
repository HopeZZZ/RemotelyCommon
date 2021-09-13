package com.zhongke.common.utils;

import android.content.res.Resources;

import com.zhongke.common.base.application.ZKBaseApplication;


/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class ZKWordUtil {

    private static Resources sResources;

    static {
        sResources = ZKBaseApplication.getContext().getResources();
    }

    public static String getString(int res) {
        if (res == 0) {
            return "";
        }
        return sResources.getString(res);
    }


    public static String getString(int res, Object... formatArgs) {
        if (res == 0) {
            return "";
        }
        return sResources.getString(res, formatArgs);
    }
}
