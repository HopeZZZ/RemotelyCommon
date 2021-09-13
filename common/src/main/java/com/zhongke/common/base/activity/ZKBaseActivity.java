package com.zhongke.common.base.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zhongke.common.base.viewmodel.ZKBaseViewModel;
import com.zhongke.common.base.viewmodel.ZKIBaseView;
import com.zhongke.common.utils.ZKDialogUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 一个拥有DataBinding框架的基Activity
 */
public abstract class ZKBaseActivity<V extends ViewDataBinding, VM extends ZKBaseViewModel> extends AppCompatActivity
        implements ZKIBaseView, EasyPermissions.PermissionCallbacks {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    protected Context mContext;
    protected Activity mActivity;
    protected Bundle savedInstanceState;
//    protected SwipeConsumer swipeConsumer;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mActivity = this;
        //页面接受的参数方法
        initParam();
        // 设置透明状态栏
        setStatusBar();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //侧滑返回界面
        initSmartSwipe();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        onCreateObserver();

        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = ZKBaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }


    public void showDialog(String title) {
        if (dialog == null) {
            dialog = ZKDialogUtils.loadingDialogs(this,title);
        }
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void initParam() {

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }

    /**
     * 设置透明状态栏
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isStatusBarWhite()) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }

    protected boolean isStatusBarWhite() {
        return false;
    }


    //1.权限请求需要先重写onRequestPermissionsResult()方法，把结果转发给EasyPermissions框架
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到 EasyPermissions框架，固定写法。
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //2.发起权限请求，在此之前先判断是否已经请求过了
    protected void requestPermission(List<String> list) {
        if (EasyPermissions.hasPermissions(this, list.toArray(new String[list.size()]))) {
            //具备权限 直接进行操作
            onPermissionSuccess();
        } else {
            //权限拒绝 申请权限
            EasyPermissions.requestPermissions(this, "为了正常使用，需要获取以下权限", 1001, list.toArray(new String[list.size()]));
        }

    }

    //3.请求成功的权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {
        onPermissionSuccess();
    }

    //4.被拒绝的权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {
        /**
         * 若是在权限弹窗中，用户勾选了 '不再提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //拒绝了权限，而且选择了不再提醒，需要去手动设置
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限请求")
                    .setRationale("需要在设置中开启权限")
                    .build()
                    .show();
        } else {
            //拒绝了权限，重新申请
            onPermissionFail();
        }
    }

    /**
     * 权限申请成功执行方法
     */
    protected void onPermissionSuccess() {

    }

    /**
     * 权限申请失败
     */
    protected void onPermissionFail() {

    }

    protected void onCreateObserver(){

    }

    /**
     * 初始化滑动返回
     */
    private void initSmartSwipe() {
//        if (isSupportSwipeBack()) {
//            swipeConsumer = SmartSwipe.wrap(this)//view
//                    .addConsumer(new ActivitySlidingBackConsumer(this))//关闭界面的样式
//                    .setRelativeMoveFactor(0.5f)//设置抽屉视图相对于内容视图的移动因子
//                    .setScrimColor(Color.TRANSPARENT)//松开的界面颜色
//                    .setShadowColor(0x80000000)//内容视图边缘阴影的颜色
//                    .setShadowSize(SmartSwipe.dp2px(10, this))//内容视图边缘阴影的大小
//                    .setEdgeSize(SmartSwipe.dp2px(100, this))// //边缘触发区域尺寸像素值（dp需转换为px），若设置为0，则表示整个activity区域都可触发
//                    .enableDirection(SwipeConsumer.DIRECTION_LEFT)//方向
//                    .addListener(new SimpleSwipeListener() {
//                        @Override
//                        public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
//                            finish();
//                            overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
//                        }
//                    });
//        }
    }

    /**
     * 是否支持滑动返回。
     * true 支持滑动返回，
     * false 支持滑动返回则重写该方法返回
     */
    public boolean isSupportSwipeBack() {
        return true;
    }

}
