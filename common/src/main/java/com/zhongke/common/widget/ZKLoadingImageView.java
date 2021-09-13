package com.zhongke.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zhongke.common.R;
import com.zhongke.common.widget.anim.ZKFrameAnimation;

/**
 * 实现了Loading动画的ImageView
 * 可以动态设置图片title,张数，是否自动播放，是否重复播放
 * Created by Fushize on 2021/06/08.
 */

public class ZKLoadingImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Context context;
    private ZKFrameAnimation animation;

    //播放时长
    private int duration;
    //是否重复播放
    private boolean isRepeat;
    //是否自动播放
    private boolean isAutoPlay;
    //动画文件名前缀
    private String title_pre = "anim_";
    //动画张数
    private int count;

    public ZKLoadingImageView(Context context) {
        this(context, null);
    }

    public ZKLoadingImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKLoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZKLoadingView);
        duration = ta.getInt(R.styleable.ZKLoadingView_loading_duration, 30);
        count = ta.getInt(R.styleable.ZKLoadingView_loading_duration, 15);
        isRepeat = ta.getBoolean(R.styleable.ZKLoadingView_loading_isRepeat,true);
        isAutoPlay = ta.getBoolean(R.styleable.ZKLoadingView_loading_isAutoPlay,true);
        String temp = ta.getString(R.styleable.ZKLoadingView_loading_title);
        if (!TextUtils.isEmpty(temp)){
            title_pre = temp;
        }
        ta.recycle();
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
        Log.d("ZKLoadingView","title=" + title_pre + ",count=" + count + ",isAutoPlay=" + isAutoPlay + ",isRepeat=" +isRepeat);
        int[] resId = getGiftRes(count, title_pre);
        animation = new ZKFrameAnimation(this, resId, duration, isRepeat, isAutoPlay);
    }
}
