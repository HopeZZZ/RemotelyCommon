package com.zhongke.common.imageloader;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zhongke.common.imageloader.glidetransform.ZKRoundedCornersTransformation;


/**
 * @author: 陈元旺
 * @date: 2021/8/30
 * @description Glide加载图片的具体实现, 要实现黑白图片，高斯模糊图片等效果可参考：https://github.com/wasabeef/glide-transformations
 */
public class ZKGlideImageLoader implements ZKBaseImageLoader {


    /**
     * 构建Glide的 RequestBuilder{@link RequestBuilder}
     * @param builder:加载图片所需参数的构建着
     * @return
     */
    private RequestBuilder createRequestBuilder(ZKImageLoadBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("ImageLoadBuilder不能为空");
        }
        if (builder.getContext() == null && builder.getFragment() == null) {
            throw new NullPointerException("ImageLoadBuilder必须设置Context或Fragment的其中一个");
        }
        RequestManager requestManager;
        if (builder.getContext() != null) {
            requestManager = Glide.with(builder.getContext());
        } else {
            requestManager = Glide.with(builder.getFragment());
        }
        RequestBuilder requestBuilder = requestManager
                .load(builder.getLoadObj())
                .placeholder(builder.getPlaceHolder())
                .error(builder.getError());
        if (builder.getOverHeight() > 0 && builder.getOverWidth() > 0) {
            requestBuilder.override(builder.getOverWidth(), builder.getOverHeight());
        }
        return requestBuilder;
    }


    /**
     * 加载默认图片
     *
     * @param builder:加载图片所需参数的构建着
     */
    public void loadNormal(ZKImageLoadBuilder builder) {
        createRequestBuilder(builder).into(builder.getImgView());
    }


    /**
     * 加载圆形图片
     *
     * @param builder:加载图片所需参数的构建着
     */
    public void loadRound(ZKImageLoadBuilder builder) {
        BaseRequestOptions requestOptions = RequestOptions.circleCropTransform();
        createRequestBuilder(builder).apply(requestOptions).into(builder.getImgView());
    }

    /**
     * 加载圆角矩形图片
     *
     * @param builder:加载图片所需参数的构建着
     */
    public void loadCorners(ZKImageLoadBuilder builder) {
        if (builder == null || builder.getCornerType() == null) {
            throw new NullPointerException("圆角图片ImageLoadBuilder的cornerType属性不能为空");
        }
        BaseRequestOptions requestOptions = RequestOptions.bitmapTransform(new ZKRoundedCornersTransformation(builder.getRadius(), 0,
                builder.getCornerType()));
        createRequestBuilder(builder).apply(requestOptions).into(builder.getImgView());
    }


}
