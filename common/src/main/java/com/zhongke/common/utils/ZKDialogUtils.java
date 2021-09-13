package com.zhongke.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongke.common.R;
import com.zhongke.common.widget.ZKSimpleRoundProgress;


/**
 * @author: user DialogUtils
 * @date: 2021/8/23
 * @description
 */
public class ZKDialogUtils {


    /**
     * 网络请求对话框
     */
    public static Dialog loadingDialogs(Context context, String title) {
        Dialog dialog = new Builder(context)
                .setContentView(R.layout.dialog_loading)
                .setBackgroundDimEnabled(true)
                .setCancelable(true)
                .build();
        //测试使用
        dialog.show();
        return dialog;
    }

    /**
     * 有标题对话框
     */
    public static void showTiTileDialog(Context context) {
        Dialog dialog = new Builder(context)
                .setContentView(R.layout.dialog_title_two_btn)
                .setContent("确定删除")
                .setGravity(GravityView.CENTER)
                .setDialogStyle(DialogStyle.TITLE)
                .setClickCallback(isConfirm -> {
                    Toast.makeText(context, "isConfirm==" + isConfirm, Toast.LENGTH_SHORT).show();
                })
                .build();
        dialog.show();
    }

    /**
     * 圆形、扇形进度条对话框
     *
     * @param context
     * @param progress
     */
    public static Dialog showSimpleRoundDialog(Context context, int progress) {
        Dialog dialog = new Builder(context)
                .setContentView(R.layout.dialog_round_progress)
                .setCancelable(true)
                .build();
        ZKSimpleRoundProgress roundProgress = dialog.findViewById(R.id.simple_round);
        roundProgress.setProgress(progress);
        dialog.show();
        return dialog;
    }


    /**
     * 用户隐私协议对话框
     */
    public static void showPrivateDialog(Context context, DialogCallback callback) {
        Dialog dialog = new Builder(context)
                .setContentView(R.layout.dialog_privacy)
                .setGravity(GravityView.CENTER)
                .setTouchOutsideCancel(false)
                .build();
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        dialog.findViewById(R.id.tv_define).setOnClickListener(v -> {
            dialog.dismiss();
            if (callback != null) {
                callback.onConfirmClick(true);
            }
        });
        dialog.findViewById(R.id.tv_quit).setOnClickListener(v -> {
            dialog.dismiss();
            if (callback != null) {
                callback.onConfirmClick(false);
            }
        });
        setUserPrivate(context, tvTitle);
        dialog.show();
    }
    /**
     * 发布须知
     *
     * @param context
     * @param content
     * @param callback
     */
    public static void issueDialog(Context context, String content, final SimpleCallback callback) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_issue);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.content);
        TextView confirm = dialog.findViewById(R.id.btn_confirm);

        if (!TextUtils.isEmpty(content)) {
//            title.setText(content);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onConfirmClick(dialog, "");
                }
            }
        });
        dialog.show();
    }

    /**
     * 拍摄视频退出 弹框
     *
     * @param context
     * @param callback3
     */
    public static void exitDialog(Context context, SimpleCallback3 callback3) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        TextView tvrestart = dialog.findViewById(R.id.tvrestart);//重新拍摄
        TextView tvexit = dialog.findViewById(R.id.tvexit);//退出
        TextView tvcancel = dialog.findViewById(R.id.tvcancel);//取消
        tvrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback3.onConfirmClick(dialog, "");

            }
        });


        tvexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback3.onCancelClick(dialog);
            }
        });

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    /**
     * 通用的弹框，有两个按钮  有标题和内容的提示框
     */
    public static void titleDialog(Context context, String titles, String content, String leftText, int leftColor, String rightText, int rightColor,
                                   final SimpleCallback3 callback3) {
        final Dialog dialog = new Dialog(context, R.style.dialog3);
        dialog.setContentView(R.layout.dialog_title_two_btn);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView cancels = dialog.findViewById(R.id.btn_dialog_cancel);
        TextView topTitle = dialog.findViewById(R.id.title);
        TextView tvContent = dialog.findViewById(R.id.content);
        TextView confirm = dialog.findViewById(R.id.btn_confirm);
        //标题文字粗体、内容不要粗体
        if (!TextUtils.isEmpty(titles)) {
            topTitle.setVisibility(View.VISIBLE);
            topTitle.setText(titles);
            topTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置为加粗
            tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));  //设置不为加粗
        }

        tvContent.setText(content);
        cancels.setText(leftText);
        cancels.setTextColor(context.getResources().getColor(leftColor));
        confirm.setText(rightText);
        confirm.setTextColor(context.getResources().getColor(rightColor));
        cancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback3 != null) {
                    callback3.onCancelClick(dialog);
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback3 != null) {
                    callback3.onConfirmClick(dialog, "");
                }
            }
        });
        dialog.show();
    }

    /**
     * 构建调用的dialog
     * 后续方便替换不同的dialog
     */
    public static class Builder {
        private Context mContext;
        private int mContentView;//布局id
        private String mTitle;//标题
        private String mContent;//对话框的内容
        private String mConfirmString;//确定的按钮
        private String mCancelString;//取消的按钮
        private boolean mCancelable;//是否可以通过返回按钮取消isCance true 为不能
        private boolean mTouchOutsideCancel = true;//点击外部是否取消
        private boolean mBackgroundDimEnabled;//显示区域以外是否使用黑色半透明背景
        private DialogCallback mClickCallback;//对话框的监听
        private GravityView mGravity = GravityView.CENTER; //Dialog弹出位置
        private DialogStyle mDialogStyle = DialogStyle.STYLE; //Dialog的样式
        private int mWindowAnimations;//底部弹出的动画

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setContentView(int contentView) {
            mContentView = contentView;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setTouchOutsideCancel(boolean touchOutsideCancel) {
            this.mTouchOutsideCancel = touchOutsideCancel;
            return this;
        }

        public Builder setWindowAnimations(int mWindowAnimations) {
            this.mWindowAnimations = mWindowAnimations;
            return this;
        }

        public Builder setGravity(GravityView gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder setDialogStyle(DialogStyle dialogStyle) {
            this.mDialogStyle = dialogStyle;
            return this;
        }

        public Builder setConfirmString(String confirmString) {
            mConfirmString = confirmString;
            return this;
        }

        public Builder setCancelString(String cancelString) {
            mCancelString = cancelString;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setBackgroundDimEnabled(boolean backgroundDimEnabled) {
            mBackgroundDimEnabled = backgroundDimEnabled;
            return this;
        }

        public Builder setClickCallback(DialogCallback clickCallback) {
            mClickCallback = clickCallback;
            return this;
        }

        //创建对话框
        public Dialog build() {
            final Dialog dialog = new Dialog(mContext, mBackgroundDimEnabled ? R.style.dialog2 : R.style.dialog);
            dialog.setContentView(mContentView);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mTouchOutsideCancel);
            Window window = dialog.getWindow();
            setLocationView(mGravity, window);
            if (mDialogStyle == DialogStyle.TITLE) {
                setDialogClick(dialog);
            }
            return dialog;
        }

        //通用的对话框样式
        private void setDialogClick(Dialog dialog) {
            TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
            if (!TextUtils.isEmpty(mTitle)) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(mTitle);
            }
            TextView tvContent = dialog.findViewById(R.id.tv_dialog_content);
            if (!TextUtils.isEmpty(mContent)) {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(mContent);
            }
            TextView btn_cancel = dialog.findViewById(R.id.btn_dialog_cancel);
            TextView btn_confirm = dialog.findViewById(R.id.btn_dialog_confirm);
            btn_cancel.setOnClickListener(v -> {
                if (mClickCallback != null) {
                    mClickCallback.onConfirmClick(false);
                }
                dialog.dismiss();
            });
            btn_confirm.setOnClickListener(v -> {
                if (mClickCallback != null) {
                    mClickCallback.onConfirmClick(true);
                }
                dialog.dismiss();
            });
        }
    }

    /**
     * 用户隐私协议字体颜色和点击事件
     */
    public static void setUserPrivate(Context context, TextView tvTitle) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tvTitle.getText().toString());
        final int start = tvTitle.getText().toString().indexOf("隐私政策");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ZKToastUtils.showShort(context, "隐私政策");
                //  BaseWebViewActivity.forward(context, Constants.TERMS_OF_SERVICE, "隐私政策", true);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#0EA1F0"));
                ds.setUnderlineText(false);
            }
        }, start, start + 4, 0);
        final int end = tvTitle.getText().toString().lastIndexOf("用户协议");//最后一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ZKToastUtils.showShort(context, "用户协议");
                // BaseWebViewActivity.forward(context, Constants.USER_POLICY, "用户协议", true);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#0EA1F0"));
                ds.setUnderlineText(false);
            }

        }, end, end + 4, 0);
        tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
        tvTitle.setText(ssb, TextView.BufferType.SPANNABLE);
    }


    public static void setLocationView(GravityView locationView, Window window) {
        switch (locationView) {
            case TOP:
                window.setGravity(Gravity.TOP);
                break;
            case CENTER:
                window.setGravity(Gravity.CENTER);
                break;
            case BOTTOM:
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.bottomToTopAnim);
                break;
        }
    }

    /**
     * 对话框的显示位置
     */
    public enum GravityView {
        CENTER, TOP, BOTTOM
    }

    /**
     * STYLE 自定义样式
     * TITLE 通用的标题模式
     */
    public enum DialogStyle {
        STYLE, TITLE
    }

    /**
     * 对话框的回调接口
     */
    public interface DialogCallback {
        void onConfirmClick(Boolean isConfirm);
    }
    public interface SimpleCallback {
        void onConfirmClick(Dialog dialog, String content);
    }
    public interface SimpleCallback3 extends SimpleCallback {
        void onConfirmClick(Dialog dialog, String content);

        void onCancelClick(Dialog dialog);
    }
}
