package com.zhongke.common.init.tasks

import android.text.TextUtils
import com.zhongke.common.constant.ZKConstant
import com.zhongke.core.BuildConfig
import com.zhongke.core.constant.GlobalConstant
import com.zhongke.core.log.ZKLog
import com.zhongke.common.utils.ZKSpUtil
import com.zhongke.core.init.ZKIInitTask
import com.zhongke.core.init.ZKInitTaskProduct


/**
 * api域名初始化
 */
class ZKApiHostTask: ZKIInitTask {

    override fun exe(chain: ZKIInitTask.Chain?): ZKInitTaskProduct? {
        if (!GlobalConstant.isRelease) { //非线上包
            //App内切换的环境
            var hostType = ZKSpUtil.getInstance().getStringValue(ZKConstant.ZKHost.API_HOST_KEY)
            if (TextUtils.isEmpty(hostType)){//如果App内没有切换，则取BUILD_ENV变量配置的
                hostType = "TEST"
            }
            ZKConstant.ZKHost.APP_HOST = getApiHost(hostType)
        }
        ZKLog.d("task Env=${BuildConfig.BUILD_TYPE} 域名=${ZKConstant.ZKHost.APP_HOST}")
        return chain?.process()
    }

    private fun getApiHost(type: String):String{
        return when(type){
            ZKConstant.ZKHost.DEV -> ZKConstant.ZKHost.DEV_HOST
            ZKConstant.ZKHost.TEST -> ZKConstant.ZKHost.TEST_HOST
            ZKConstant.ZKHost.PRE -> ZKConstant.ZKHost.PRE_HOST
            ZKConstant.ZKHost.RELEASE -> ZKConstant.ZKHost.RELEASE
            else -> ZKConstant.ZKHost.RELEASE_HOST
        }
    }

}