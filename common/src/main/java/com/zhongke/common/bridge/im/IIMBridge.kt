package com.zhongke.common.bridge.im

import com.zhongke.common.bridge.ZKIBridge

/**
 *    author : wpt
 *    date   : 2021/7/3010:21
 *    desc   :
 */
interface IIMBridge: ZKIBridge {

    fun isLoginIm():Boolean
}