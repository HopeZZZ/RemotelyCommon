package com.zhongke.common.init.tasks

import com.alibaba.android.arouter.launcher.ARouter
import com.zhongke.common.BuildConfig
import com.zhongke.core.init.ZKIInitTask
import com.zhongke.core.init.ZKInitTaskProduct


/**
 * Router初始化
 */
class ZKRouterTask: ZKIInitTask {

    override fun exe(chain: ZKIInitTask.Chain?): ZKInitTaskProduct? {

        //腾讯bugly
//        CrashReport.initCrashReport(application);
//        CrashReport.setAppVersion(application,BuildConfig.VERSION_NAME);
        //初始化阿里路由框架
        if (BuildConfig.DEBUG) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(chain?.application)
        return chain?.process()
    }

}