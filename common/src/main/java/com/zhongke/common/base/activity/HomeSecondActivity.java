package com.zhongke.common.base.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zhongke.common.BR;
import com.zhongke.common.R;
import com.zhongke.common.base.fragment.ZKBaseFragment;
import com.zhongke.common.base.viewmodel.ZKHomeSecondViewModel;
import com.zhongke.common.databinding.ActivityHomeSecondBinding;
import com.zhongke.common.utils.ZKStatusBarUtil;

import java.lang.ref.WeakReference;


/**
 * 承载多个Fragment的Activity
 * Created by Fushize on 2021/7/9.
 */

public class HomeSecondActivity extends ZKBaseActivity<ActivityHomeSecondBinding, ZKHomeSecondViewModel> implements View.OnClickListener {
    private static final String FRAGMENT_TAG = "HomeSecondActivity";
    public static final String FRAGMENT = "fragment";
    public static final String TITLE = "title";
    public static final String BUNDLE = "BUNDLE";
    public static final String RIGHT_TEXT = "rightText";
    protected WeakReference<Fragment> mFragment;


    //构建Activity需要的数据
    private String title;
    private String rightText;
    private String fragmentName;
    private Bundle bundle;
    Fragment fragment = null;

    public static HomeSecondActivity build() {
        return new HomeSecondActivity();
    }

    /**
     * 必传，否则会抛异常
     *
     * @param fragment 这里设置的是Fragment.class.canonicalName （路径名）
     */
    public HomeSecondActivity setFragment(String fragment) {
        this.fragmentName = fragment;
        return this;
    }

    /**
     * @param title 顶部标题
     */
    public HomeSecondActivity setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @param bundle 往Fragment传数据
     */
    public HomeSecondActivity setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public HomeSecondActivity setRightText(String text) {
        this.rightText = text;
        return this;
    }

    /**
     * 最后才调用此方法启动Activity
     */
    public void startActivity(Context context) {
        Intent intent = new Intent(context, HomeSecondActivity.class);
        intent.putExtra(FRAGMENT, fragmentName);
        intent.putExtra(TITLE, title);
        intent.putExtra(RIGHT_TEXT, rightText);
        if (bundle != null) {
            intent.putExtra(BUNDLE, bundle);
        }
        context.startActivity(intent);
    }


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_home_second;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        binding.include.tvTitle.setText(getIntent().getStringExtra("title"));
        //顶部白色
        ZKStatusBarUtil.setColor(this, Color.parseColor("#ffffff"));
        ZKStatusBarUtil.setDarkMode(this);

        String rightText = getIntent().getStringExtra("rightText");
        if (!TextUtils.isEmpty(rightText)) {
            binding.include.textRight.setVisibility(View.VISIBLE);
            binding.include.textRight.setText(rightText);
        }

        //右边按钮
        binding.include.textRight.setOnClickListener(this);
        binding.include.ivHelp.setOnClickListener(this);
        binding.include.llRight.setOnClickListener(this);

        //关闭按钮
        binding.include.btnBack.setOnClickListener(this);

        //替换Fragment
        ReplaceFragment(savedInstanceState);
    }

    /**
     * 如果Fragment需要监听onActivityResult，则需要在这里做一层中转。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 保存实例状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment.get());
    }

    /**
     * 根据Fragment名字获取相应的Fragment对象（反射获取）
     */
    protected Fragment initFromIntent(Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        try {
            String fragmentName = data.getStringExtra(FRAGMENT);
            if (fragmentName == null || "".equals(fragmentName)) {
                throw new IllegalArgumentException("can not find page fragmentName");
            }
            Class<?> fragmentClass = Class.forName(fragmentName);
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            Bundle args = data.getBundleExtra(BUNDLE);
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("fragment initialization failed!");
    }


    /**
     * 替换Fragment
     */
    private void ReplaceFragment(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState != null) {
            fragment = fm.getFragment(savedInstanceState, FRAGMENT_TAG);
        }
        if (fragment == null) {
            fragment = initFromIntent(getIntent());
        }
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        trans.replace(R.id.mFL_content, fragment);
        trans.commitAllowingStateLoss();
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_right || v.getId() == R.id.iv_help || v.getId() == R.id.ll_right) {
            //通知Fragment右边的按钮被点击
            ((ZKBaseFragment) fragment).onRightClick();
        } else if (v.getId() == R.id.btn_back) {
            finish();
        }
    }


}
