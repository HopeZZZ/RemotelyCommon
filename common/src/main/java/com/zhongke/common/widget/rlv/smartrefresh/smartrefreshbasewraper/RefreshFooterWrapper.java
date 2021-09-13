package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbasewraper;

import android.annotation.SuppressLint;
import android.view.View;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshFooter;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbasesimple.SimpleComponent;


/**
 * 刷新底部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends SimpleComponent implements RefreshFooter {

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

}
