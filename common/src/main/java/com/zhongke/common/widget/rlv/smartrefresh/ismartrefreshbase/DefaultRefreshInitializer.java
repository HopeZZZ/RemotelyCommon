package com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 默认全局初始化器
 * Created by scwang on 2018/5/29 0029.
 */
public interface DefaultRefreshInitializer {
    void initialize(@NonNull Context context, @NonNull RefreshLayout layout);
}
