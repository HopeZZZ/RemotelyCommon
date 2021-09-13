package com.zhongke.common.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import com.zhongke.common.base.application.ZKBaseApplication

object ZKAppUtils {
    private var sVersion: String? = null
    private var sVersionCode: String? = null
    private var sVersionName: String? = null

    /**
     * 获取版名称
     */
    fun getVersion(context: Context): String? {
        if (TextUtils.isEmpty(sVersion)) {
            try {
                val manager: PackageManager = context.packageManager
                val info: PackageInfo =
                    manager.getPackageInfo(context.packageName, 0)
                sVersion = info.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return sVersion
    }

    /**
     * 获取版本号
     */
    fun getVersionCode(context: Context): String {
        if (TextUtils.isEmpty(sVersionCode)) {
            try {
                val manager: PackageManager = context.packageManager
                val info: PackageInfo =
                    manager.getPackageInfo(context.packageName, 0)
                sVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    info.longVersionCode.toString() + ""
                } else {
                    info.versionCode.toString() +""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return if (!TextUtils.isEmpty(sVersionCode)) sVersionCode!! else ""
    }

    /**
     * 获取版本号
     */
    fun getVersionName(context: Context): String {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                val manager: PackageManager = context.packageManager
                val info: PackageInfo =
                    manager.getPackageInfo(context.packageName, 0)
                sVersionName = info.versionName + ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return if (!TextUtils.isEmpty(sVersionName)) sVersionName!! else ""
    }

    //    1、获得当前activity的名字
    fun getRunningActivityName(context: Context): String {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //完整类名
        val runningActivity =
            activityManager.getRunningTasks(1)[0].topActivity!!.className
        return runningActivity.substring(runningActivity.lastIndexOf(".") + 1)
    }

    //    2、获得当前应用包名
    fun getAppPackageName(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = activityManager.getRunningTasks(1)
        val componentInfo = taskInfo[0].topActivity
        return if (componentInfo !== null) componentInfo.packageName else ""
    }

    fun isAppExist(packageName: String): Boolean {
        if (!TextUtils.isEmpty(packageName)) {
            val manager: PackageManager = ZKBaseApplication.getContext().getPackageManager()
            val list = manager.getInstalledPackages(0)
            for (info in list) {
                if (packageName.equals(info.packageName, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

}