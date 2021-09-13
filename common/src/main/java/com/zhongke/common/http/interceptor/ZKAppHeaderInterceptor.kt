package com.zhongke.common.http.interceptor

import android.os.Build
import com.zhongke.common.base.application.ZKBaseApplication
import com.zhongke.common.utils.ZKAppUtils
import com.zhongke.common.utils.ZKDeviceIdFactory
import com.zhongke.common.utils.ZKEncryptUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.util.*

/**
 * author : wpt
 * date   : 2021/7/2816:23
 * desc   : 共用header参数
 */
class ZKAppHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("device", Build.MANUFACTURER + " " + Build.MODEL)
            .addHeader("uuid", ZKDeviceIdFactory.getInstance(ZKBaseApplication.getContext()).deviceUuid)
            .addHeader("bundle-id", ZKAppUtils.getAppPackageName(ZKBaseApplication.getContext()))
            .addHeader("version", ZKAppUtils.getVersionName(ZKBaseApplication.getContext()))
            .addHeader("sign",
                ZKEncryptUtils.createSign(getAllParamHap(chain.request()),""))
            .addHeader("channel","")
            .build()
        return chain.proceed(builder.build())
    }

    /**
     * 获取所有参数
     * @param request
     * @return
     */
    private fun getAllParamHap(request: Request): TreeMap<Any, Any?> {
        val requestBody = request.body
        val rootMap = TreeMap<Any, Any?>()
        val httpUrl = request.url
        val method = request.method
        if (method == "GET") {
            // 通过请求地址(最初始的请求地址)获取到参数列表
            val parameterNames = httpUrl.queryParameterNames
            for (key in parameterNames) {  // 循环参数列表
                rootMap[key] = httpUrl.queryParameter(key)
            }
        } else if (method == "POST" || method == "PUT" || method == "DELETE") {
            if (requestBody is RequestBody) {
                // buffer流
                val buffer = Buffer()
                try {
                    requestBody.writeTo(buffer)
                    val oldParamsJson = buffer.readUtf8()
                    val splitAnd = oldParamsJson.split("&").toTypedArray()
                    for (str in splitAnd) {
                        val splitEqual = str.split("=").toTypedArray()
                        var i = 0
                        while (i < splitEqual.size) {
                            if (splitEqual.size > i + 1) {
                                rootMap[splitEqual[i]] = splitEqual[i + 1]
                            }
                            i += 2
                        }
                    }
                } catch (e: Exception) {
                    buffer.close()
                    e.printStackTrace()
                } finally {
                    buffer.close()
                }
            } else if (request.body is FormBody) {
                for (i in 0 until (requestBody as FormBody?)!!.size) {
                    rootMap[(requestBody as FormBody?)!!.encodedName(i)] =
                        (requestBody as FormBody?)!!.encodedValue(i)
                }
            }
            //            }
        }
        return rootMap
    }
}