package com.zhongke.common.base.application;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.zhongke.common.constant.ZKConstant;
import com.zhongke.core.init.ZKInitTrigger;
import com.zhongke.common.init.tasks.ZKApiHostTask;
import com.zhongke.common.init.tasks.ZKLogTask;
import com.zhongke.common.init.tasks.ZKRouterTask;
import com.zhongke.common.utils.ZKSpUtil;



public abstract class ZKBaseApplication extends MultiDexApplication {

    private static ZKBaseApplication mInstance;

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        initTask();

    }

    public static ZKBaseApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    public abstract void initModuleApp();

    public abstract void initModuleAppData();

    //初始化启动时的各种task
    private void initTask() {
        ZKInitTrigger.Companion.getInstance().prepare(mInstance);
        ZKInitTrigger.Companion.getInstance()
                .addTask(new ZKLogTask())
                .addTask(new ZKApiHostTask())
                .addTask(new ZKRouterTask()).bind();
        if (ZKSpUtil.getInstance().getBooleanValue(ZKConstant.ZKSP.IS_AGREE_PRIVACY_POLICY)) {
            initOtherTask();
        }
    }

    /**
     * 初始化涉及用到隐私相关内容的第三方（必须要同意隐私条款了才能初始化）
     */
    public void initOtherTask() {

    }
}
