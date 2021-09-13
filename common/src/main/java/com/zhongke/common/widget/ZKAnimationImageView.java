package com.zhongke.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.zhongke.common.widget.anim.ZKFrameAnimation;


/**
 * @Author: Administrator
 * @Date: 2021/8/4
 * @Description :
 */
public class ZKAnimationImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Context context;
    private ZKFrameAnimation animation;

    public ZKAnimationImageView(Context context) {
        this(context, null);
    }

    public ZKAnimationImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKAnimationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAnim();
    }

    private int[] getGiftRes(int count, String title) {
        final int[] resIds = new int[count];
        Resources res = context.getResources();
        final String packageName = context.getPackageName();
        for (int i = 0; i < resIds.length; i++) {
            String resName = title + (i);
            int imageResId = res.getIdentifier(resName, "drawable", packageName);
            resIds[i] = imageResId;
        }
        return resIds;
    }

    private void initAnim() {
        int[] resId = getGiftRes(15, "anim_");
        animation = new ZKFrameAnimation(this, resId, 30, true, true);
    }
}
