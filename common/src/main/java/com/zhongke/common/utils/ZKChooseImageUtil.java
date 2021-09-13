package com.zhongke.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.zhongke.common.base.application.ZKBaseApplication;
import com.zhongke.common.bean.ChooseImageBean;
import com.zhongke.common.bridge.CommonCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/6/20.
 */

public class ZKChooseImageUtil {

    private ContentResolver mContentResolver;
    private ImageHandler mHandler;
    private CommonCallback<List<ChooseImageBean>> mCallback;
    private boolean mStop;

    public ZKChooseImageUtil() {
        mContentResolver = ZKBaseApplication.getContext().getContentResolver();
        mHandler = new ImageHandler(this);
    }

    public void getLocalImageList(CommonCallback<List<ChooseImageBean>> callback) {
        if (callback == null) {
            return;
        }
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mHandler != null) {
                    List<ChooseImageBean> imageList = getAllImage();
                    Message msg = Message.obtain();
                    msg.obj = imageList;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void imageCallback(List<ChooseImageBean> imageList) {
        if (!mStop && mCallback != null) {
            mCallback.callback(imageList);
        }
    }

    private List<ChooseImageBean> getAllImage() {
        List<ChooseImageBean> imageList = new ArrayList<>();
        Cursor cursor = null;
        try {
            //只查询jpeg和png的图片
            cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    "mime_type=? or mime_type=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            if (cursor != null) {
                while (!mStop && cursor.moveToNext()) {
                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (!imagePath.contains("/DCIM/")) {
                        continue;
                    }
                    File file = new File(imagePath);
                    if (!file.exists()) {
                        continue;
                    }
                    boolean canRead = file.canRead();
                    long length = file.length();
                    if (!canRead || length == 0) {
                        continue;
                    }
                    imageList.add(new ChooseImageBean(ChooseImageBean.FILE, file));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imageList;
    }


    public void release() {
        mStop = true;
        if (mHandler != null) {
            mHandler.release();
        }
        mCallback = null;
    }

    private static class ImageHandler extends Handler {

        private ZKChooseImageUtil mImageUtil;

        public ImageHandler(ZKChooseImageUtil util) {
            mImageUtil = new WeakReference<>(util).get();
        }

        @Override
        public void handleMessage(Message msg) {
            List<ChooseImageBean> imageList = (List<ChooseImageBean>) msg.obj;
            if (mImageUtil != null && imageList != null) {
                mImageUtil.imageCallback(imageList);
            }
        }

        public void release() {
            removeCallbacksAndMessages(null);
            mImageUtil = null;
        }
    }

    /**
     * 保存图像到本地
     *
     * @param bm
     * @return
     */
    public static String saveBitmapToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = ZKFileUtils.getInstance(context).createTempFile("IMG_", ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    /**
     * 按宽/高缩放图片到指定大小并进行裁剪得到中间部分图片 <br>
     * 方 法 名：zoomBitmap <br>
     * 创 建 人：楼翔宇 <br>
     * 创建时间：2018-4-7 下午12:02:52 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param bitmap 源bitmap
     * @param vw     缩放后指定的宽度
     * @param vh     缩放后指定的高度
     * @return 缩放后的中间部分图片 Bitmap
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, float vw, float vh) {
        float width = bitmap.getWidth();//获得图片宽高
        float height = bitmap.getHeight();

        float scaleWidht, scaleHeight, x, y;//图片缩放倍数以及x，y轴平移位置
        Bitmap newbmp = null; //新的图片
        Matrix matrix = new Matrix();//变换矩阵
        if ((width / height) <= vw / vh) {//当宽高比大于所需要尺寸的宽高比时以宽的倍数为缩放倍数
            scaleWidht = vw / width;
            scaleHeight = scaleWidht;
            y = ((height * scaleHeight - vh) / 2) / scaleHeight;// 获取bitmap源文件中y做表需要偏移的像数大小
            x = 0;
        } else {
            scaleWidht = vh / height;
            scaleHeight = scaleWidht;
            x = ((width * scaleWidht - vw) / 2) / scaleWidht;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        }
        matrix.postScale(scaleWidht / 1f, scaleHeight / 1f);
        try {
            if (width - x > 0 && height - y > 0 && bitmap != null)//获得新的图片 （原图，x轴起始位置，y轴起始位置，x轴结束位置，Y轴结束位置，缩放矩阵，是否过滤原图）为防止报错取绝对值
                newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x), (int) Math.abs(y), (int) Math.abs(width - x),
                        (int) Math.abs(height - y), matrix, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {//如果报错则返回原图，不至于为空白
            e.printStackTrace();
            return bitmap;
        }
        return newbmp;
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }
}
