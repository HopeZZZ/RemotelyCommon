package com.zhongke.common.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

/**
 * 加载中的TextView
 * Created by Fushize on 2021/1/19.
 */

public class ZKLoadingTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if ("加载中".equals(getText().toString())) {
                setText("加载中.");
            } else if ("加载中.".equals(getText().toString())) {
                setText("加载中..");
            } else if ("加载中..".equals(getText().toString())) {
                setText("加载中...");
            } else if ("加载中...".equals(getText().toString())) {
                setText("加载中");
            }
            mHandler.sendEmptyMessageDelayed(1001, 250);

        }
    };

    public ZKLoadingTextView(Context context) {
        this(context, null);
    }

    public ZKLoadingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKLoadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setLoadingText(String text) {
        if ("加载中".equals(text)) {
            setText("加载中");
            mHandler.sendEmptyMessageDelayed(1001, 250);
        } else {
            setText(text);
        }
    }
}
