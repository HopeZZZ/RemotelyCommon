package com.zhongke.common.router;

/**
 * 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 */

public class ZKRouterFragmentPath {
    /**
     * 直播组件
     */
    public static class Live {
        private static final String LIVE = "/live";
        /*直播*/
        public static final String FRAGMENT_LIVE = LIVE + "/Fragment_Live";
        /*直播*/
        public static final String PAGER_LIVE = LIVE + "/Live";
        /*搜索*/
        public static final String PAGER_SEARCH = LIVE + "/Search";
        public static final String PAGER_WATCH_HISTORY = LIVE + "/watchhistory";
    }

    /**
     * 视频组件
     */
    public static class Video {
        private static final String VIDEO = "/video";
        /*视频*/
        public static final String PAGER_VIDEO = VIDEO + "/Video";
    }

    /**
     * IM组件
     */
    public static class Im {
        private static final String IM = "/im";
        /*Im*/
        public static final String PAGER_IM = IM + "/im/Im";

        /*聊天*/
        public static final String FRAGMENT_CHAT = IM + "/Fragment_Chat";
        /*圈子*/
        public static final String FRAGMENT_MOMENTS = IM + "/Fragment_Moments";
        public static final String FRAGMENT_MOMENTS_HOT = FRAGMENT_MOMENTS + "/hot";
        public static final String FRAGMENT_MOMENTS_DYNAMIC = IM + "/dynamic";
        /*圈子动态详情*/
        public static final String FRAGMENT_MOMENTS_DYNAMIC_DETAILS = FRAGMENT_MOMENTS_DYNAMIC + "/details";
        /*通讯录*/
        public static final String FRAGMENT_CONTACT_LIST = IM + "/Fragment_Contact_List";
        public static final String FRAGMENT_STRANGER_NEWS_LIST = IM + "/Fragment_Stranger_News_List";
        public static final String FRAGMENT_Service_NEWS_LIST = IM + "/Fragment_Service_News_List";
        public static final String NOTIFICATION_TEMP_ACTIVITY = IM + "/Notification_temp_activity";
        public static final String FRAGMENT_CIRCLE_CONTACT_LIST = IM + "/Fragment_Circle_Contact_list";

        public static final String FRAGMENT_CIRCLE_MSG = IM + "/Fragment_Circle_MSG";
    }

    /**
     * 福利组件
     */
    public static class Weal {
        private static final String WEAL = "/weal";
        /*福利*/
        public static final String PAGER_WEAL = WEAL + "/weal/Weal";
    }

    /**
     * 邀请组件
     */
    public static class Invite {
        private static final String INVITE = "/invite";
        /*邀请*/
        public static final String PAGER_INVITE = INVITE + "/invite/Invite";
    }

    /**
     * 我的组件
     */
    public static class Mine {
        private static final String MINE = "/user";
        /*我的*/
        public static final String PAGER_MINE = MINE + "/Mine";
        /*我的*/
        public static final String PAGER_SET = MINE + "/Set";
    }
}
