package com.zhongke.common.widget.rlv.smartrefresh.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.zhongke.common.R;
import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaseconstant.RefreshState;

/**
 * @Author: Administrator
 * @Date: 2021/8/4
 * @Description :
 */
public class RecyclerHeaderView extends InternalAbstract {


    public RecyclerHeaderView(Context context) {
        this(context, null);
    }

    public RecyclerHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.module__recycler_header_view, this);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        super.onFinish(layout, success);
        return 500; //延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh: //下拉过程
                break;
            case ReleaseToRefresh: //松开刷新
                break;
            case Refreshing: //loading中
                break;
        }
    }
}