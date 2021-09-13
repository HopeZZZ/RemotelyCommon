package com.zhongke.common.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.zhongke.common.base.application.ZKBaseApplication;
import com.zhongke.common.constant.ZKConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;

/**
 * Created by cxf on 2018/6/22.
 */

public class ZKBitmapUtil {

    private static ZKBitmapUtil sInstance;
    private Resources mResources;
    private BitmapFactory.Options mOptions;

    private ZKBitmapUtil() {
        mResources = ZKBaseApplication.getContext().getResources();
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inDither = true;
        mOptions.inSampleSize = 1;
    }

    public static ZKBitmapUtil getInstance() {
        if (sInstance == null) {
            synchronized (ZKBitmapUtil.class) {
                if (sInstance == null) {
                    sInstance = new ZKBitmapUtil();
                }
            }
        }
        return sInstance;
    }


    public Bitmap decodeBitmap(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return new SoftReference<>(bitmap).get();
    }


    public Bitmap decodeBitmap(int imgRes) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = toByteArray(mResources.openRawResource(imgRes));
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, mOptions);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "decodeBitmap: " + e.getMessage());
        }
        return new SoftReference<>(bitmap).get();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy((InputStream) input, (OutputStream) output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n;
        for (boolean var5 = false; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    /**
     * 把Bitmap保存成图片文件
     *
     * @param bitmap
     */
    public static String saveBitmap(Bitmap bitmap) {
        String path = null;
        File dir = new File(ZKConstant.ZKHost.CAMERA_IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imageFile = new File(dir, ZKDateFormatUtil.getCurTimeString() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            path = imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }


    /**
     * 把Bitmap保存成图片文件
     */
    public boolean saveBitmap(Bitmap bitmap, File imageFile) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            saveImageInfo(imageFile);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }


    /**
     * 把视频保存到ContentProvider,在选择上传的时候能找到
     */
    private void saveImageInfo(File file) {
        try {
            String fileName = file.getName();
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, fileName);
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeMillis);
            values.put(MediaStore.MediaColumns.DATE_ADDED, currentTimeMillis);
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.MediaColumns.SIZE, file.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            ZKBaseApplication.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * @param bm
     * @param ratio 缩放比例
     * @return
     */
    public Bitmap zoomImg(Bitmap bm, float ratio) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 获取视频第一帧图片
     *
     * @param videoPath
     * @return 当视频是竖屏的时候 orientation = 90，横屏 orientation = 0
     * String width = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); //宽
     * String height = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); //高
     * String rotation = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);//视频的方向角度
     * long duration = Long.valueOf(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;//视频的长度
     */
    public static MediaMetadataRetriever getVideoThumbnail(Context context, String videoPath) {
        MediaMetadataRetriever media = null;
        if(TextUtils.isEmpty(videoPath))return null;
        if (videoPath.contains("content:")) {
            try {
                videoPath = ZKPathUtils.getPath(context, Uri.parse(videoPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(TextUtils.isEmpty(videoPath))return null;
        try {
            media = new MediaMetadataRetriever();
            String absolutePath = new File(videoPath).getAbsolutePath();
            media.setDataSource(absolutePath);
            return media;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return media;
    }
}
