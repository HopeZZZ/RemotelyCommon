package com.zhongke.common.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author: user LiLinHua
 * @date: 2021/8/20
 * @description 贝塞尔曲线封装
 */
public class ZKBezierUtils {

    //贝塞尔曲线中间过程的点的坐标
    private float[] mCurrentPosition = new float[2];
    private Context mContext;
    private AnimListener mListener;    //动画监听
    private View startView;  //开始坐标位置
    private View endView;  //结束坐标位置
    private ViewGroup parentView;   //控制点坐标位置
    private int imageWidth;
    private int imageHeight;
    private View animView;
    private int imageUrl;
    private long time;    //动画时间
    private AnimModule animModule = AnimModule.CIRCLE;

    public ZKBezierUtils(Builder builder) {
        this.mContext = builder.context;
        this.startView = builder.startView;
        this.endView = builder.endView;
        this.parentView = builder.parentView;
        this.time = builder.time;
        this.mListener = builder.listener;
        this.animView = builder.animView;
        this.imageUrl = builder.imageUrl;
        this.animModule = builder.animModule;
        this.imageWidth = builder.imageWidth;
        this.imageHeight = builder.imageHeight;
        this.time = builder.time;
    }


    /**
     * 开始动画
     */
    public void startAnim() {
        if (startView == null || endView == null || parentView == null) {
            return;
        }
        ImageView imageView = new ImageView(mContext);
        Glide.with(mContext).load(imageUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(imageWidth, imageHeight);
        parentView.addView(imageView, params);
        PathMeasure pathMeasure = getPathMeasure(startView, endView, parentView);
        ValueAnimator valueAnimator = initAnimation(imageView, pathMeasure);
        valueAnimator.start();
    }

    /**
     * 设置贝塞尔曲线
     * @param startView  起点View
     * @param endView    终点View
     * @param parentView 控制点View
     */
    @NotNull
    private PathMeasure getPathMeasure(View startView, View endView, View parentView) {
        int startA[] = new int[2];
        startView.getLocationInWindow(startA);
        // 获取终点的坐标
        int endB[] = new int[2];
        endView.getLocationInWindow(endB);
        // 父布局控制点坐标
        int parentC[] = new int[2];
        parentView.getLocationInWindow(parentC);
//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startA[0] - parentC[0];
        float startY = startA[1] - parentC[1];
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endB[0] - parentC[0];
        float toY = endB[1] - parentC[1];
        // 四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo(toX, startY, toX, toY);
//        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        PathMeasure mPathMeasure = new PathMeasure(path, false);
        return mPathMeasure;
    }

    /**
     * 创建动画
     */
    private ValueAnimator initAnimation(ImageView imageView, PathMeasure mPathMeasure) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());  // 匀速线性插值器
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mListener != null) {
//                    mListener.setAnimUpdate(animation, mPathMeasure, imageView);
                    updateAnimation(animation, mPathMeasure, imageView);
                }
            }
        });
        //六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                parentView.removeView(imageView);
                if (mListener != null) {
                    mListener.setAnimEnd(ZKBezierUtils.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        return valueAnimator;
    }

    /**
     * 更新动画
     */
    private void updateAnimation(ValueAnimator animation, PathMeasure mPathMeasure, ImageView imageView) {
        // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
        float value = (Float) animation.getAnimatedValue();
        // ★★★★★获取当前点坐标封装到mCurrentPosition
        // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
        mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
        // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
        imageView.setTranslationX(mCurrentPosition[0]);
        imageView.setTranslationY(mCurrentPosition[1]);
    }

    public static final class Builder {
        View startView;
        View endView;
        ViewGroup parentView;
        View animView;
        int imageUrl;
        long time;
        double scale;
        int imageWidth;
        int imageHeight;
        AnimListener listener;
        Context context;
        private AnimModule animModule = AnimModule.CIRCLE;

        public Builder() {
            this.time = 1000;
            this.scale = 1;
            this.imageHeight = 100;
            this.imageWidth = 100;
        }

        public Builder animModule(AnimModule animModule) {
            this.animModule = animModule;
            return this;
        }

//      public Builder with(Activity activity) {
//            this.activity = new WeakReference<>(activity);
//            return this;
//      }
        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder startView(View startView) {
            if (startView == null) {
                throw new NullPointerException("startView is null");
            }
            this.startView = startView;
            return this;
        }

        public Builder endView(View endView) {
            if (endView == null) {
                throw new NullPointerException("endView is null");
            }
            this.endView = endView;
            return this;
        }

        public Builder parentView(ViewGroup parentView) {
            if (parentView == null) {
                throw new NullPointerException("endView is null");
            }
            this.parentView = parentView;
            return this;
        }

        public Builder animView(View animView) {
            if (animView == null) {
                throw new NullPointerException("animView is null");
            }
            this.animView = animView;
            return this;
        }

        public Builder listener(AnimListener listener) {
            if (listener == null) {
                throw new NullPointerException("listener is null");
            }
            this.listener = listener;
            return this;
        }

        public Builder imageUrl(int imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder time(long time) {
            if (time <= 0) {
                throw new IllegalArgumentException("time must be greater than zero");
            }
            this.time = time;
            return this;
        }

        public Builder scale(double scale) {
            this.scale = scale;
            return this;
        }

        public Builder animWidth(int width) {
            if (width <= 0) {
                throw new IllegalArgumentException("width must be greater than zero");
            }
            this.imageWidth = width;
            return this;
        }

        public Builder animHeight(int height) {
            if (height <= 0) {
                throw new IllegalArgumentException("height must be greater than zero");
            }
            this.imageHeight = height;
            return this;
        }

        public ZKBezierUtils build() {
            return new ZKBezierUtils(this);
        }
    }


    //回调监听
    public interface AnimListener {
        //      void setAnimBegin(BezierUtils a);
        void setAnimEnd(ZKBezierUtils a);
//      void setAnimUpdate(ValueAnimator animation, PathMeasure mPathMeasure, ImageView imageView);
    }

    //图像形状
    public enum AnimModule {
        CIRCLE,//大的圆形
        SMALL//小的（默认）
    }

}
