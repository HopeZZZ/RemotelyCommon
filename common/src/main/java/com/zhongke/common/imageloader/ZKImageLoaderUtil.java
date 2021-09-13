package com.zhongke.common.imageloader;


import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author: 陈元旺
 * @date: 2021/8/30
 * @description 图片加载工具类
 */
public class ZKImageLoaderUtil {

    private ZKBaseImageLoader imageLoader;


    private ZKImageLoaderUtil() {
        imageLoader = new ZKGlideImageLoader();
    }

    private static ZKImageLoaderUtil instance;

    public static ZKImageLoaderUtil getInstance() {
        if (instance == null) {
            synchronized (ZKImageLoaderUtil.class) {
                if (instance == null) {
                    instance = new ZKImageLoaderUtil();
                    return instance;
                }
            }
        }
        return instance;
    }

    /**
     * 加载普通图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param context：上下文（可以是Activity或者Application），当使用Glide加载图片时，加载与该上下文的生命周期绑定
     * @param imageView：图片控件
     */
    public void loadNormal(Object loadObj, Context context, ImageView imageView) {
        loadNormal(createBuilder(loadObj, context, imageView, null, 0));
    }


    /**
     * 加载普通图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param fragment：Fragment，当使用Glide加载图片时，加载与该Fragment的生命周期绑定
     * @param imageView：图片控件
     */
    public void loadNormal(Object loadObj, Fragment fragment, ImageView imageView) {
        loadNormal(createBuilder(loadObj, fragment, imageView, null, 0));
    }

    /**
     * 加载普通图片，当设置的属性较多时，可通过builder模式构建
     * @param builder：加载图片所需参数的构建着
     */
    public void loadNormal(ZKImageLoadBuilder builder) {
        imageLoader.loadNormal(builder);
    }

    /**
     * 加载圆形图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param context：上下文（可以是Activity或者Application），当使用Glide加载图片时，加载与该上下文的生命周期绑定
     * @param imageView：图片控件
     */
    public void loadRound(Object loadObj, Context context, ImageView imageView) {
        loadRound(createBuilder(loadObj, context, imageView, null, 0));
    }

    /**
     * 加载圆形图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param fragment：Fragment，当使用Glide加载图片时，加载与该Fragment的生命周期绑定
     * @param imageView：图片控件
     */
    public void loadRound(Object loadObj, Fragment fragment, ImageView imageView) {
        loadRound(createBuilder(loadObj, fragment, imageView, null, 0));
    }

    /**
     * 加载圆形图片，当设置的属性较多时，可通过builder模式构建
     * @param builder：加载图片所需参数的构建着
     */
    public void loadRound(ZKImageLoadBuilder builder) {
        imageLoader.loadRound(builder);
    }

    /**
     * 加载圆角图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param context：上下文（可以是Activity或者Application），当使用Glide加载图片时，加载与该上下文的生命周期绑定
     * @param imageView：图片控件
     * @param cornerType：圆角的类型
     * @param radius：圆角程度
     */
    public void loadCorners(Object loadObj, Context context, ImageView imageView,
                            @NonNull ZKImageCornerType cornerType, int radius) {
        loadCorners(createBuilder(loadObj, context, imageView, cornerType, radius));
    }

    /**
     * 加载圆角图片
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param fragment：Fragment，当使用Glide加载图片时，加载与该Fragment的生命周期绑定
     * @param imageView：图片控件
     * @param cornerType：圆角的类型
     * @param radius：圆角程度
     */
    public void loadCorners(Object loadObj, Fragment fragment, ImageView imageView,
                            @NonNull ZKImageCornerType cornerType, int radius) {
        loadCorners(createBuilder(loadObj, fragment, imageView, cornerType, radius));
    }

    /**
     * 加载圆角图片，当设置的属性较多时，可通过builder模式构建
     * @param builder：加载图片所需参数的构建着
     */
    public void loadCorners(ZKImageLoadBuilder builder) {
        imageLoader.loadCorners(builder);
    }


    /**
     * 这里可以设置或者切换图片加载库
     * @param loader：图片加载库
     */
    public void setLoadImgStrategy(ZKBaseImageLoader loader) {
        imageLoader = loader;
    }


    /**
     *  构建图片加载所需属性
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param fragment：Fragment，当使用Glide加载图片时，加载与该Fragment的生命周期绑定
     * @param imageView：图片控件
     * @param cornerType：圆角的类型(可为空，加载圆角图片时才需要传入)
     * @param radius：圆角程度(加载圆角图片时才有效)
     * @return {@link ZKImageLoadBuilder} 图片构建者
     */
    private ZKImageLoadBuilder createBuilder(Object loadObj, Fragment fragment, ImageView imageView,
                                             @Nullable ZKImageCornerType cornerType, int radius) {
        ZKImageLoadBuilder loadBuilder = new ZKImageLoadBuilder();
        return loadBuilder.load(loadObj).with(fragment).imgView(imageView).cornerType(cornerType).radius(radius);
    }

    /**
     *
     * @param loadObj：资源文件id，图片URL，图片路径等
     * @param context：上下文（可以是Activity或者Application），当使用Glide加载图片时，加载与该上下文的生命周期绑定
     * @param imageView：图片控件
     * @param cornerType：圆角的类型(可为空，加载圆角图片时才需要传入)
     * @param radius：圆角程度(加载圆角图片时才有效)
     * @return {@link ZKImageLoadBuilder} 图片构建者
     */
    private ZKImageLoadBuilder createBuilder(Object loadObj, Context context, ImageView imageView,
                                             @Nullable ZKImageCornerType cornerType, int radius) {
        ZKImageLoadBuilder loadBuilder = new ZKImageLoadBuilder();
        return loadBuilder.load(loadObj).with(context).imgView(imageView).cornerType(cornerType).radius(radius);
    }

}
