package com.zhongke.common.bridge

/**
 * @author: shuYang
 * @date: 2021/7/28
 * @description 组件bridge管理类$
 */
object ZKBridgeManager {

    /**
     * 以 [IBridgeName,BridgeImpl]的形式全局缓存,例如: [ISampleBridge,SampleBridge]
     */
    private var bridgesStore : MutableMap<String,ZKIBridge>? = null

    /**
     * 提供servicesStore
     */
    fun getBridgesStore() : MutableMap<String,ZKIBridge>? {
        if(bridgesStore == null){
            bridgesStore = mutableMapOf()
        }
        return bridgesStore
    }

    /**
     * 添加module的Bridge到全局公用的BridgeStore中
     */
    inline fun <reified T : ZKIBridge> addComponentBridge(bridge: ZKIBridge) {
        getBridgesStore()?.put(T::class.java.simpleName,bridge)
    }

    /**
     * 根据IBridgeName获取某个module的Bridge
     */
    inline fun <reified T : ZKIBridge> getComponentBridge(): T? {
        val iBridge = getBridgesStore()?.get(T::class.java.simpleName)
        return if(iBridge is T) iBridge else null
    }

    /**
     * 清理
     */
    fun clear(){
        getBridgesStore()?.clear()
    }
}