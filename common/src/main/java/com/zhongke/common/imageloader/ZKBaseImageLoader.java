package com.zhongke.common.imageloader;

/**
 * @author: 陈元旺
 * @date: 2021/8/30
 * @description 图片加载接口，具体实现图片加载功能的类必须实现本接口
 */
public interface ZKBaseImageLoader {

    void loadNormal(ZKImageLoadBuilder img);

    void loadRound(ZKImageLoadBuilder img);

    void loadCorners(ZKImageLoadBuilder img);
}
