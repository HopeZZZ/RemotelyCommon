package com.zhongke.common.constant;

import android.os.Environment;
/**
 * author : wpt
 * date   : 2021/8/413:34
 * desc   : 常量类
 */
public class ZKConstant {

    public static class ZKHost {

        public static final String API_HOST_KEY = "apiHost";
        public static String DEV = "DEV";
        public static String TEST = "TEST";
        public static String PRE = "PRE";
        public static String RELEASE = "RELEASE";
        //开发域名
        public static String DEV_HOST = "http://172.29.151.140";

        //测试域名
        public static String TEST_HOST = "http://172.29.151.141";

        //预发布域名
        public static String PRE_HOST = "http://172.29.151.142";

        //正式域名
        public static String RELEASE_HOST = "http://172.29.151.143";

        public static String RELEASE_HOST1 = "http://172.29.153.104:23301/";

        public static String APP_HOST = DEV_HOST;
        public static final String DIRECTORY_DOCUMENTS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();

        public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        //文件夹名字
        private static final String DIR_NAME = "zhongke";
        public static final String VIDEO_PATH = DCMI_PATH + "/" + DIR_NAME + "/video/";
        public static final String FILE_NAME = "file_key";
        public static final String UID = "uid";
        public static final String TOKEN = "token";
        public static final String VIDEO_RECORD_TEMP_PATH = VIDEO_PATH + "recordParts";
        //下载音乐的时候保存的路径
        public static final String FILE_PATH = DIRECTORY_DOCUMENTS + "/";
        //拍照时图片保存路径
        public static final String CAMERA_IMAGE_PATH = DCMI_PATH + "/" + DIR_NAME + "/camera/";
        public static final String HAS_ENTER_VIDEO_RECORD = "has_enter_video_record";//第一次进入视频录制界面
    }

    /**
     * SharedPreferences常量
     */
    public static class ZKSP {
        //是否同意隐私政策
        public static String IS_AGREE_PRIVACY_POLICY = "isAgreePrivacyPolicy";
        //token
        public static String TOKEN = "token";
    }

    public static class Umeng {
        public static String CHANNEL = "com.zhongke.inisland";
        public static String Appkey = "610c9924063bed4d8c0c262b";
        public static String MESSAGE_SECRET = "933cde445d9fb3278a2c3e63d49f7403";

    }

    public static class Push {
        public static String OPPO_KEY = "dbcf85e0668041789464b0bd890037dc";
        public static String OPPO_SECRET = "085e050ab4cf4a598e13d99fc093c13c";
        public static String MEI_ZU_ID = "143615";
        public static String MEI_ZU_KEY = "7ba9e8f23d3b4b4b983d67b093743e98";
    }

    public static class Mob {
        public static String APPID = "34144fd44da5a";
        public static String APPKEY = "e9bb7f74ff349e0c25f546bc944ad086";
    }

    //厂商通道key
    public static class Oppo {
        public static String APPKEY = "dbcf85e0668041789464b0bd890037dc";
        public static String APPSERET = "085e050ab4cf4a598e13d99fc093c13c";
    }

    public static class MeiZu {
        public static String APPID = "143615";
        public static String APPKEY = "7ba9e8f23d3b4b4b983d67b093743e98";
    }

    public static class XiaoMi {
        public static String APPID = "2882303761520028386";
        public static String APPKEY = "5462002886386";
    }

}
