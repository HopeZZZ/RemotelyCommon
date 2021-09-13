package com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbasewraper;

import android.annotation.SuppressLint;
import android.view.View;

import com.zhongke.common.widget.rlv.smartrefresh.ismartrefreshbase.RefreshHeader;
import com.zhongke.common.widget.rlv.smartrefresh.smartrefreshbasesimple.SimpleComponent;


/**
 * 刷新头部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends SimpleComponent implements RefreshHeader {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
