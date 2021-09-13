package com.zhongke.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * Created by cxf on 2018/9/22.
 */

public class ZKTabButtonGroup extends LinearLayout implements View.OnClickListener {

    private ZKTabButton[] mTabButtons;
    private ViewPager mViewPager;
    private int mCurPosition;

    private TabButtonGroupListener mTabSelectedListener;

    public void setTabSelectedListener(TabButtonGroupListener listener) {
        this.mTabSelectedListener = listener;
    }

    public ZKTabButtonGroup(Context context) {
        this(context, null);
    }

    public ZKTabButtonGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKTabButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            mTabButtons = new ZKTabButton[childCount];
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                v.setTag(i);
                v.setOnClickListener(this);
                mTabButtons[i] = (ZKTabButton) v;
            }
        }
    }

    public void setCurPosition(int position) {
        if (position == mCurPosition) {
            return;
        }
        if (mTabSelectedListener != null){
            mTabSelectedListener.onTabSelected(position);
        }

        mTabButtons[mCurPosition].setChecked(false);
        mTabButtons[position].setChecked(true);
        mCurPosition = position;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }

    }

    public int getCurPosition() {
        return mCurPosition;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            setCurPosition((int) tag);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }


    public void cancelAnim() {
        if (mTabButtons != null) {
            for (ZKTabButton tbn : mTabButtons) {
                if (tbn != null) {
                    tbn.cancelAnim();
                }
            }
        }
    }

    public interface TabButtonGroupListener{
        public void onTabSelected(int index);
    }

    public ZKTabButton[] getTabButtons() {
        return mTabButtons;
    }
}
