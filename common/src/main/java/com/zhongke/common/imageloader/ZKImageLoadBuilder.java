package com.zhongke.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.zhongke.common.R;


/**
 * @author: 陈元旺
 * @date: 2021/8/30
 * @description 图片加载各属性构建类（Builder模式）
 */
public class ZKImageLoadBuilder {

    public Object loadObj;//加载的图片对象，可以是图片url、图片文件等
    private int placeHolder;//占位图片
    private int error;//加载出错后显示的图片
    private ImageView imgView;//图片控件
    private Context context;//设置Context使Glide加载图片跟随Context生命周期
    private Fragment fragment;//设置Fragment使Glide加载图片跟随Fragment生命周期
    private int overWidth;//指定图片加载的宽
    private int overHeight;//指定图片加载的高
    private ZKImageCornerType cornerType;//图片圆角类型
    private int radius;//图片圆角值

    public ZKImageLoadBuilder() {
        this.placeHolder = R.drawable.icon_load_default;
    }

    public ZKImageLoadBuilder load(Object loadObj) {
        this.loadObj = loadObj;
        return this;
    }

    public ZKImageLoadBuilder with(Context context) {
        this.context = context;
        return this;
    }

    public ZKImageLoadBuilder with(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public ZKImageLoadBuilder placeHolder(int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public ZKImageLoadBuilder error(int error) {
        this.error = error;
        return this;
    }

    public ZKImageLoadBuilder imgView(ImageView imgView) {
        this.imgView = imgView;
        return this;
    }

    public ZKImageLoadBuilder overWidth(int width) {
        this.overWidth = width;
        return this;
    }

    public ZKImageLoadBuilder overHeight(int height) {
        this.overHeight = height;
        return this;
    }

    public ZKImageLoadBuilder cornerType(ZKImageCornerType cornerType) {
        this.cornerType = cornerType;
        return this;
    }

    public ZKImageLoadBuilder radius(int radius) {
        this.radius = radius;
        return this;
    }


    public Object getLoadObj() {
        return loadObj;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public Context getContext() {
        return context;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getOverWidth() {
        return overWidth;
    }

    public int getOverHeight() {
        return overHeight;
    }

    public int getError() {
        return error;
    }

    public ZKImageCornerType getCornerType() {
        return cornerType;
    }

    public int getRadius() {
        return radius;
    }
}
