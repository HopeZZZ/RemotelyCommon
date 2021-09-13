package com.zhongke.common.http

import com.google.gson.GsonBuilder
import com.zhongke.common.constant.ZKConstant
import com.zhongke.common.http.interceptor.ZKCommonParamsInterceptor
import com.zhongke.common.http.interceptor.ZKAppHeaderInterceptor
import com.zhongke.core.http.HttpsUtils
import com.zhongke.core.http.httpbase.PersistentCookieJar
import com.zhongke.core.http.httpbase.SharedPrefsCookiePersistor
import com.zhongke.core.http.httpbase.appContext
import com.zhongke.core.http.httpbase.cache.SetCookieCache
import com.zhongke.core.http.httpbase.ext.RetrofitUrlManager
import com.zhongke.core.http.httpbase.interceptor.CacheInterceptor
import com.zhongke.core.http.httpbase.interceptor.loging.LogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 作者　: wcj
 * 时间　: 2021/7/19
 * 描述　: 网络请求Api工具类
 * 功能 : 后续网络全部调用这里的Api即可
 */


class ZKHttpApi {

    private val maxSizeCache: Long = 10 * 1024 * 1024               //最大缓存

    companion object {

        private val BASE_URL: String = ZKConstant.ZKHost.APP_HOST
        val INSTANCE: ZKHttpApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ZKHttpApi()
        }
    }

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
            builder = setHttpClientBuilder(builder)

            //给client的builder添加了一个socketFactory
            builder.sslSocketFactory(
                HttpsUtils.getSslSocketFactory().sSLSocketFactory,
                HttpsUtils.getSslSocketFactory().trustManager
            )
            return builder.build()
        }

    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(appContext)
        )
    }

    /**
     * 网络请求的Api调用方法，不需要传入根路径
     */
    fun <T> getApi(serviceClass: Class<T>): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    /**
     * 网络请求Api调用方法，传入根路径
     */
    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    private fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
            cache(Cache(File(appContext.cacheDir, "cxk_cache"), maxSizeCache))
            //添加Cookies自动持久化
            cookieJar(cookieJar)
            //添加共用参数
            addInterceptor(ZKCommonParamsInterceptor())
            /**
             * 1.添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
             * 2.添加在共用参数之后，因为header里的sign依赖共用参数
             */

            addInterceptor(ZKAppHeaderInterceptor())
//            addInterceptor(MyAppHeaderInterceptor())
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor())
            // 日志拦截器
            addInterceptor(LogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    private fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

}