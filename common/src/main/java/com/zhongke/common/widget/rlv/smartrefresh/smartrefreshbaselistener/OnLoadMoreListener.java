package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaselistener;


import androidx.annotation.NonNull;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;


/**
 * 加载更多监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
