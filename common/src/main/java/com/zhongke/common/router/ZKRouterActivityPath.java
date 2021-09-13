package com.zhongke.common.router;

/**
 * 用于组件开发中，ARouter单Activity跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 */

public class ZKRouterActivityPath {
    /**
     * 主业务组件
     */
    public static class Home {
        private static final String HOME = "/home";
        /*主业务界面*/
        public static final String PAGER_MAIN = HOME + "/home";

    }


    /**
     * Im组件
     */
    public static class Im {
        private static final String Im = "/im";
        /*Im*/
        public static final String PAGER_Im = Im + "/Im";

        public static final String PAGER_jave = Im + "/Jave";
    }

    /**
     * 身份验证组件
     */
    public static class Sign {
        private static final String SIGN = "/sign";
        /*登录界面*/
        public static final String PAGER_LOGIN = SIGN + "/Login";
        /*绑定手机界面*/
        public static final String PAGER_BINDING = SIGN + "/Binding";
    }


    public static class Base {
        private static final String BASE = "/base";
        /*webview*/
        public static final String PAGER_WEBVIEW = BASE + "/WebviewNobar";
    }


    public static class Mine {
        private static final String MINE = "/Mine";
        public static final String HOME_MINE = MINE + "/mine";
    }

    public static class Test {
        private static final String TEST = "/TEST";
        public static final String PAGER_TEST = TEST + "/Test";
        public static final String HOME_TEST = TEST + "/http/ttt";
    }
    public static class Video {
        private static final String VIDEO = "/video";
        public static final String PAGER_RECORD = VIDEO + "/record";
    }



}
