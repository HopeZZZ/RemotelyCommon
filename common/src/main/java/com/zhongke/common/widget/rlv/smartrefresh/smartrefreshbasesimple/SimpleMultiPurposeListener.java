package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbasesimple;


import androidx.annotation.NonNull;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshFooter;
import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshHeader;
import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshLayout;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaseconstant.RefreshState;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbaselistener.OnMultiListener;


/**
 * 多功能监听器
 * Created by scwang on 2017/5/26.
 */
public class SimpleMultiPurposeListener implements OnMultiListener {

    private RefreshLayout refreshLayout;
    private OnMultiListener listener;

    public SimpleMultiPurposeListener() {

    }

    public SimpleMultiPurposeListener(OnMultiListener listener, RefreshLayout refreshLayout) {
        this.listener = listener;
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onHeaderMoving(header, isDragging, percent, offset, headerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onHeaderMoving(refreshLayout.getRefreshHeader(), isDragging, percent, offset, headerHeight, maxDragHeight);
        }
    }


    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onHeaderReleased(header, headerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onHeaderReleased(refreshLayout.getRefreshHeader(), headerHeight, maxDragHeight);
        }
    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onHeaderStartAnimator(header, headerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onHeaderStartAnimator(refreshLayout.getRefreshHeader(), headerHeight, maxDragHeight);
        }
    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {
        if (listener != null) {
            listener.onHeaderFinish(header, success);
        }
        if (refreshLayout != null) {
            onHeaderFinish(refreshLayout.getRefreshHeader(), success);
        }
    }

    @Override
    public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onFooterMoving(footer, isDragging, percent, offset, footerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onFooterMoving(refreshLayout.getRefreshFooter(), isDragging, percent, offset, footerHeight, maxDragHeight);
        }
    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onFooterReleased(footer, footerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onFooterReleased(refreshLayout.getRefreshFooter(), footerHeight, maxDragHeight);
        }
    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
        if (listener != null) {
            listener.onFooterStartAnimator(footer, footerHeight, maxDragHeight);
        }
        if (refreshLayout != null) {
            onFooterStartAnimator(refreshLayout.getRefreshFooter(), footerHeight, maxDragHeight);
        }
    }


    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {
        if (listener != null) {
            listener.onFooterFinish(footer, success);
        }
        if (refreshLayout != null) {
            onFooterFinish(refreshLayout.getRefreshFooter(), success);
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (listener != null) {
            listener.onRefresh(refreshLayout);
        }
        if (this.refreshLayout != null) {
            onRefresh(this.refreshLayout);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (listener != null) {
            listener.onLoadMore(refreshLayout);
        }
        if (this.refreshLayout != null) {
            onLoadMore(this.refreshLayout);
        }
    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (listener != null) {
            listener.onStateChanged(refreshLayout, oldState, newState);
        }
        if (this.refreshLayout != null) {
            onStateChanged(this.refreshLayout, RefreshState.from(oldState), RefreshState.from(newState));
        }
    }

}
