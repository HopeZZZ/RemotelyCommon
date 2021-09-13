package com.zhongke.common.init.tasks

import com.zhongke.core.log.ZKLog
import com.zhongke.core.init.ZKIInitTask
import com.zhongke.core.init.ZKInitTaskProduct


/**
 * Log初始化
 */
class ZKLogTask: ZKIInitTask {

    override fun exe(chain: ZKIInitTask.Chain?): ZKInitTaskProduct? {
        ZKLog.init()
        return chain?.process()
    }

}