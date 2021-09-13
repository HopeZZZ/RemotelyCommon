package com.zhongke.common.base.viewmodel;

/**
 *
 */

public interface ZKIBaseView {
    /**
     * 初始化界面传递参数
     */
    void initParam();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
