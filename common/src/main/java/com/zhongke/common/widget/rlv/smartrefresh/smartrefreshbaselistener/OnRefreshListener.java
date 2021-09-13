package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaselistener;


import androidx.annotation.NonNull;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;


/**
 * 刷新监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
