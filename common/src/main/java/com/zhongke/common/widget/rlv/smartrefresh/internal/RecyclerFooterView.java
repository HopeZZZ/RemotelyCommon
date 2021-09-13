package com.zhongke.common.widget.rlv.smartrefresh.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.zhongke.common.R;
import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaseconstant.RefreshState;

/**
 * @Author: Administrator
 * @Date: 2021/8/4
 * @Description :
 */
public class RecyclerFooterView extends InternalAbstract {


    private FrameLayout fl_main;

    public RecyclerFooterView(Context context) {
        this(context, null);
    }

    public RecyclerFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.module_recycler_footer_view, this);
        fl_main = findViewById(R.id.fl_root);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        super.onFinish(layout, success);
        return 500; //延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None: //加载完成
                fl_main.setVisibility(GONE);
                break;
            case PullUpToLoad:
            case Loading:
                fl_main.setVisibility(VISIBLE);
                break;
        }
    }
}