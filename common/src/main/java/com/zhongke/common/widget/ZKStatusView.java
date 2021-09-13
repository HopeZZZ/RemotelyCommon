package com.zhongke.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhongke.common.R;


/**
 * @Author: wcj
 * @Date: 2021/8/12
 * @Description :   状态自定义View，无数据状态，无网络状态，网络异常状态等等
 */
public class ZKStatusView extends FrameLayout {

    public static final int STATUS_LOADING = 101;             //请求数据
    public static final int STATUS_NO_DATA = 100;             //无数据状态
    public static final int STATUS_NETWORK_ERROR = 102;       //网络异常

    private ZKLoadingImageView ivLoading;
    private FrameLayout flNoData;
    private TextView tvNoData;
    private ImageView ivNoDataIcon;
    private LinearLayout llLoadFailure;
    private TextView tvReLoad;

    public ZKStatusView(@NonNull Context context) {
        this(context, null);
    }

    public ZKStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView(context);
    }

    private void createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_status_view_layout, this, true);
        ivLoading = view.findViewById(R.id.iv_loading);
        flNoData = view.findViewById(R.id.fl_no_data);
        llLoadFailure = view.findViewById(R.id.ll_load_failure);
        tvNoData = view.findViewById(R.id.tv_no_data);
        ivLoading = view.findViewById(R.id.iv_loading);
        tvReLoad = view.findViewById(R.id.tv_reload);

    }

    /**
     * 设置状态View的显示状态
     *
     * @param statusType 状态类型
     */
    public void setStatusType(int statusType) {
        if (statusType == STATUS_LOADING) {
            ivLoading.setVisibility(VISIBLE);
            flNoData.setVisibility(GONE);
            llLoadFailure.setVisibility(GONE);
        } else if (statusType == STATUS_NO_DATA) {
            ivLoading.setVisibility(GONE);
            flNoData.setVisibility(VISIBLE);
            llLoadFailure.setVisibility(GONE);
        } else if (statusType == STATUS_NETWORK_ERROR) {
            ivLoading.setVisibility(GONE);
            flNoData.setVisibility(GONE);
            llLoadFailure.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置重新刷新的点击事件
     * @param clickListener 点击事件
     */
    public void setReLoadClickListener(OnClickListener clickListener) {
        if (tvReLoad != null) {
            tvReLoad.setOnClickListener(clickListener);
        }
    }


}
