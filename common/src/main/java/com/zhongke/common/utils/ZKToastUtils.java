package com.zhongke.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by wpt on 2021/8/11.
 * Toast工具类，可以在任何线程进行Toast
 * 内部处理如果不是主线程调用的，则切到主线程调用
 */
public class ZKToastUtils {

    private static Handler mHandler;

    static {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * {@link Toast#LENGTH_SHORT}+{@link CharSequence}
     *
     * @param context
     * @param text
     */
    public static void showShort(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * {@link Toast#LENGTH_SHORT}+resId
     *
     * @param context
     * @param resId
     */
    public static void showShort(Context context, @StringRes int resId) {
        showShort(context, context.getResources().getText(resId));
    }

    /**
     * {@link Toast#LENGTH_LONG}+{@link CharSequence}
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    /**
     * {@link Toast#LENGTH_LONG}+resId
     *
     * @param context
     * @param resId
     */
    public static void showLong(Context context, @StringRes int resId) {

    }


    /**
     * toast封装，可以在子线程内toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void show(final Context context, final CharSequence text, final int duration) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Toast.makeText(context, text, duration).show();
        } else {
            mHandler.post(() -> Toast.makeText(context, text, duration).show());
        }
    }

    /**
     * 自定义toast
     * @param context
     * @param view
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static Toast showCustomToast(final Context context, final View view, final int duration, final int gravity, final int xOffset, final int yOffset){
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(view);
        toast.setGravity(gravity,xOffset,yOffset);
        toast.show();
        return toast;

    }

}
