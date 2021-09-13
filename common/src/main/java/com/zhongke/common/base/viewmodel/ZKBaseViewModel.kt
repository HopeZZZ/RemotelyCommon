package com.zhongke.common.base.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.Serializable
import java.lang.Exception

/**
 *
 */
open class ZKBaseViewModel<M : ZKBaseModel?> @JvmOverloads constructor(protected var model: M? = null) :
    ViewModel(), ZKIBaseViewModel, Serializable {

    private val error by lazy { MutableLiveData<Exception>() }
    private val finally by lazy { MutableLiveData<Int>() }


    //运行在UI线程的协程
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            withTimeout(5000){
                block()
            }
        } catch (e: Exception) {
            error.value = e
        } finally {
            finally.value = 200
        }
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {}
    override fun onCreate() {}
    override fun onDestroy() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun registerRxBus() {}
    override fun removeRxBus() {}
    override fun onCleared() {
        super.onCleared()
        if (model != null) {
            model!!.onCleared()
        }
    }
}