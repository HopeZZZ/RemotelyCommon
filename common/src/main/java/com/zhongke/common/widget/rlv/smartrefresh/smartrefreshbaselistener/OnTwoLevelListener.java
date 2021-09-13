package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaselistener;


import androidx.annotation.NonNull;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;


/**
 * 二级刷新监听器
 */
public interface OnTwoLevelListener {
    /**
     * 二级刷新触发
     *
     * @param refreshLayout 刷新布局
     * @return true 将会展开二楼状态 false 关闭刷新
     */
    boolean onTwoLevel(@NonNull RefreshLayout refreshLayout);
}