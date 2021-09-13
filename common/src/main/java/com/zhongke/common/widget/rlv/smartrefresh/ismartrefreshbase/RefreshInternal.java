package com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase;


import androidx.annotation.NonNull;

import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaseconstant.SpinnerStyle;


/**
 * 刷新内部组件
 * Created by scwang on 2017/5/26.
 */
public interface RefreshInternal extends RefreshComponent {

    /**
     * 获取变换方式 {@link SpinnerStyle} 必须返回 非空
     * @return 变换方式
     */
    @NonNull
    SpinnerStyle getSpinnerStyle();
}
