package com.zhongke.common.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.zhongke.common.base.viewmodel.ZKBaseViewModel;
import com.zhongke.common.base.viewmodel.ZKIBaseView;
import com.zhongke.common.utils.ZKDialogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public abstract class ZKBaseFragment<V extends ViewDataBinding, VM extends ZKBaseViewModel> extends Fragment implements ZKIBaseView {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    protected Activity mActivity;
    private Dialog dialog;
    // ================ LazyLoadBaseFragment ================//
    private boolean mIsFirstVisible = true;
    private boolean isViewCreated;
    private boolean isSupportVisible;
    private Handler mHandler;

    private final Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    private final boolean isParentInvisible() {
        Fragment parentFragment = this.getParentFragment();
        if (parentFragment instanceof ZKBaseFragment) {
            ZKBaseFragment fragment = (ZKBaseFragment) parentFragment;
            return !fragment.isSupportVisible;
        } else {
            return false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isViewCreated) {
            if (isVisibleToUser && !this.isSupportVisible) {
                this.dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && this.isSupportVisible) {
                this.dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mActivity = getActivity();
        this.isViewCreated = true;
        // !isHidden() 默认为 true  在调用 hide show 的时候可以使用
        if (!this.isHidden() && this.getUserVisibleHint()) {
            // 这里的限制只能限制 A - > B 两层嵌套
            this.dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            this.dispatchUserVisibleHint(false);
        } else {
            this.dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.mIsFirstVisible && !this.isHidden() && !this.isSupportVisible && this.getUserVisibleHint()) {
            this.dispatchUserVisibleHint(true);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.isSupportVisible && this.getUserVisibleHint()) {
            this.dispatchUserVisibleHint(false);
        }

    }

    private final void dispatchUserVisibleHint(boolean visible) {
        if (!visible || !this.isParentInvisible()) {
            if (this.isSupportVisible != visible) {
                this.isSupportVisible = visible;
                if (visible) {
                    if (!this.isAdded()) {
                        return;
                    }

                    if (this.mIsFirstVisible) {
                        this.onFragmentFirstVisible();
                        this.onFragmentResume(true);
                        this.mIsFirstVisible = false;
                    } else {
                        this.onFragmentResume(false);
                    }

                    this.onFragmentResume();
                    this.enqueueDispatchVisible();
                } else {
                    this.dispatchChildVisibleState(false);
                    this.onFragmentPause();
                }

            }
        }
    }

    private final void enqueueDispatchVisible() {
        this.getHandler().post((Runnable) (new Runnable() {
            public final void run() {
                ZKBaseFragment.this.dispatchChildVisibleState(true);
            }
        }));
    }

    private final void dispatchChildVisibleState(boolean visible) {
        if (this.isAdded()) {
            FragmentManager childFragmentManager = this.getChildFragmentManager();
            List fragments = childFragmentManager.getFragments();
            if (!fragments.isEmpty()) {
                Iterator var5 = fragments.iterator();

                while (var5.hasNext()) {
                    Fragment child = (Fragment) var5.next();
                    if (child instanceof ZKBaseFragment && child.isAdded() && !child.isHidden() && child.getUserVisibleHint()) {
                        ((ZKBaseFragment) child).dispatchUserVisibleHint(visible);
                    }
                }
            }

        }
    }

    public final boolean hasFirstVisible() {
        return !this.mIsFirstVisible;
    }

    public void onFragmentFirstVisible() {
    }

    public void onFragmentResume() {
    }

    public final void onFragmentResume(boolean firstResume) {
    }

    public void onFragmentPause() {
    }
    // ================ LazyLoadBaseFragment ================//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
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
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);

    }

    public void showDialog(String title) {
        if (dialog == null) {
            dialog = ZKDialogUtils.loadingDialogs(getActivity(), title);
        }
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
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
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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

    public void onRightClick() {

    }

    public boolean isBackPressed() {
        return false;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }
}
