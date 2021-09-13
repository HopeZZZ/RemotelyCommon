package com.zhongke.common.widget.rlv;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import static androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zhongke.common.R;
import com.zhongke.common.widget.ZKStatusView;
import com.zhongke.common.widget.rlv.adapter.BaseQuickAdapter;
import com.zhongke.common.widget.rlv.rlvinterface.ICommonRecyclerView;
import com.zhongke.common.widget.rlv.smartrefresh.SmartRefreshLayout;
import com.zhongke.common.widget.rlv.smartrefresh.internal.RecyclerFooterView;
import com.zhongke.common.widget.rlv.smartrefresh.internal.RecyclerHeaderView;
import com.zhongke.core.http.httpbase.exception.AppException;

import java.util.List;

/**
 * @author: wcj
 * @date: 2021/8/3
 * @description : 根据爱玩宝项目里的CommonRefreshView简化而来的RecyclerView封装
 * 注意，使用该框架，所有的适配器都要继承 BaseQuickAdapter
 */
public class CommonRecyclerView extends FrameLayout {

    public static final int STATUS_ON_REFRESH_SUCCESS = 100;         //下拉刷新成功状态值
    public static final int STATUS_ON_REFRESH_FAILUER = 101;         //下拉刷新失败状态值
    public static final int STATUS_ON_LOAD_MORE_SUCCESS = 102;       //上来加载更多成功状态值
    public static final int STATUS_ON_LOAD_MORE_FAILUER = 103;       //上来加载更多失败状态值
    public static final int STATUS_ON_REQUEST_DATA = 104;            //正在请求数据状态值
    public static final int STATUS_NO_DATA = 105;                    //没有数据
    public static final int STATUS_NET_WORK_ERROR = 106;             //网络异常

    private final int REQUEST_DATA_SIZE = 20;                        //默认一次请求数据20条

    private boolean refreshEnable;                                   //下拉刷新是否可用
    private boolean loadMoreEnable;                                  //上拉加载是否可用
    private boolean isDataEnd = false;                               //已经没有下一页数据了
    private RecyclerView rlvData;                                    //承载数据的RecyclerView
    private FrameLayout flContainer;                                 //承载布局的容器，空状态等
    private RecyclerHeaderView headerView;                           //头布局
    private RecyclerFooterView footerView;                           //底部布局
    private SmartRefreshLayout smartRefreshLayout;                   //上拉，下拉是刷新布局
    private int currentPage = 1;                                     //当前页数 必须小心维护
    private ICommonRecyclerView iCommonRecyclerView;                 //接口
    private ZKStatusView statusView;                                   //状态显示View


    public CommonRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonRecyclerView);
        refreshEnable = typedArray.getBoolean(R.styleable.CommonRecyclerView_is_pull_down_refresh, true);//默认是可以下拉刷新的
        loadMoreEnable = typedArray.getBoolean(R.styleable.CommonRecyclerView_is_load_more, true);//默认是可以上拉加载更多的
        typedArray.recycle();

        createView(context);
    }

    private void createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_common_recycler_view, this, true);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        headerView = view.findViewById(R.id.header_view);
        footerView = view.findViewById(R.id.footer_view);
        flContainer = view.findViewById(R.id.fl_container);
        rlvData = view.findViewById(R.id.rlv_data);
        statusView = view.findViewById(R.id.status_view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlvData.setHasFixedSize(true);
        initSmartLayoutParam(smartRefreshLayout);
    }

    private void initSmartLayoutParam(SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
        smartRefreshLayout.setEnableFooterFollowWhenNoMoreData(true);//是否在全部加载结束之后Footer跟随内容
        smartRefreshLayout.setEnableOverScrollBounce(false);//设置是否开启越界回弹功能（默认true）
        smartRefreshLayout.setEnableRefresh(refreshEnable);
        smartRefreshLayout.setEnableLoadMore(loadMoreEnable);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
//                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            if (iCommonRecyclerView != null) {
                currentPage = 1;
                iCommonRecyclerView.onRequestData(currentPage);
            } else {
                refreshLayout.finishRefresh(false);//传入false表示刷新失败
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            if (iCommonRecyclerView != null) {
                currentPage++;
                iCommonRecyclerView.onRequestData(currentPage);
            } else {
                refreshlayout.finishLoadMore(false);//传入false表示加载失败
            }
        });

        //重新加载，在网络异常状态View的重新加载点击事件
        statusView.setReLoadClickListener(v -> {
            if (iCommonRecyclerView != null) {
                currentPage = 1;
                iCommonRecyclerView.onRequestData(currentPage);
            }
        });

    }

    /**
     * 设置RecyclerView的管理器
     * 可以参考 {@link androidx.recyclerview.widget.LinearLayoutManager}
     *
     * @param layoutManager 管理器
     */
    public void setLayoutManager(LayoutManager layoutManager) {
        rlvData.setLayoutManager(layoutManager);
    }

    /**
     * 添加RecyclerView的分割线下
     * 参考{@link ItemDecoration}
     *
     * @param itemDecoration 分割线实现对象
     */
    public void setItemDecoration(ItemDecoration itemDecoration) {
        rlvData.addItemDecoration(itemDecoration);
    }

    /**
     * 是否启用下拉刷新功能
     * Api使用参考 https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
     *
     * @param enable true 为启用 false为关闭
     */
    public void setRefreshEnable(boolean enable) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableRefresh(enable);
        }
    }

    /**
     * 是否启用上拉加载功能
     * Api使用参考 https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
     *
     * @param enable ture为启用，false为关闭
     */
    public void setLoadMoreEnable(boolean enable) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableLoadMore(enable);
        }
    }


    /**
     * 设置请求数据接口
     *
     * @param iCommonRecyclerView 请求数据接口{@link ICommonRecyclerView}
     */
    public void setiCommonRecyclerView(ICommonRecyclerView iCommonRecyclerView) {
        this.iCommonRecyclerView = iCommonRecyclerView;
    }

    /**
     * 给RecyclerView设置适配器
     *
     * @param adapter 适配器，必须是继承{@link Adapter}
     */
    public void setAdapter(Adapter adapter) {
        if (adapter != null && rlvData != null) {
            rlvData.setAdapter(adapter);
        }
    }

    /**
     * * 使用新的数据集合，改变原有数据集合内容。
     * * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param dataList 数据源
     */
    public void setListData(List<Object> dataList) {
        if (currentPage == 1) {//第一页数据
            setData(dataList);
        } else {
            addMoreData(dataList);
        }
    }

    /**
     * * 使用新的数据集合，改变原有数据集合内容。
     * * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param dataList 数据源 第一页数据
     */
    private void setData(List<Object> dataList) {
        BaseQuickAdapter adapter = (BaseQuickAdapter) rlvData.getAdapter();
        if (adapter == null) {
            return;
        }
        adapter.setList(dataList);
        //第一页数据为空，表示，数据为空，显示空状态
        setStatus(dataList.size() <= 0 ? STATUS_NO_DATA : STATUS_ON_REFRESH_SUCCESS);

    }

    /**
     * 添加更多数据 下一页数据
     *
     * @param dataList 数据源 下一页数据
     */
    private void addMoreData(List<Object> dataList) {
        BaseQuickAdapter adapter = (BaseQuickAdapter) rlvData.getAdapter();
        if (adapter == null) {
            currentPage--;
            return;
        }
        if (dataList.size() <= 0) {//表示请求的下一页数据 无数据，此时需要把page重置回来
            currentPage--;
        }
        adapter.addData(dataList);
        setStatus(STATUS_ON_LOAD_MORE_SUCCESS);
    }

    /**
     * 设置状态
     *
     * @param status 状态值 {@link CommonRecyclerView}
     */
    private void setStatus(int status) {
        if (statusView == null) {
            throw new NullPointerException("statusView不能为空....");
        }
        //正在请求数据 显示动画
        if (status == STATUS_ON_REQUEST_DATA) {
            statusView.setStatusType(ZKStatusView.STATUS_LOADING);
            statusView.setVisibility(VISIBLE);
            return;
        }
        //网络请求成功，但是无数据
        if (status == STATUS_NO_DATA) {
            setRefreshFinish(true);
            setLoadMoreFinish(true);
            statusView.setStatusType(ZKStatusView.STATUS_NO_DATA);
            statusView.setVisibility(VISIBLE);
            return;
        }
        //请求数据成功（下拉/上拉刷新成功） 有数据状态
        if (status == STATUS_ON_REFRESH_SUCCESS || status == STATUS_ON_LOAD_MORE_SUCCESS) {
            statusView.setVisibility(GONE);
            if (status == STATUS_ON_REFRESH_SUCCESS) {
                setRefreshFinish(true);
            }
            if (status == STATUS_ON_LOAD_MORE_SUCCESS) {
                setLoadMoreFinish(true);
            }
            return;
        }

        //下拉刷新数据失败
        if (status == STATUS_ON_REFRESH_FAILUER) {
            setRefreshFinish(false);

            statusView.setStatusType(ZKStatusView.STATUS_NETWORK_ERROR);
            statusView.setVisibility(VISIBLE);
            return;
        }
        //上拉加载更多数据失败
        if (status == STATUS_ON_LOAD_MORE_FAILUER) {
            setLoadMoreFinish(false);
            statusView.setStatusType(ZKStatusView.STATUS_NETWORK_ERROR);//TODO 后续要添加状态，暂时使用这个
            statusView.setVisibility(VISIBLE);
            currentPage--;
            return;
        }
        //下拉刷新，网络状态异常
        if (status == STATUS_NET_WORK_ERROR) {
            setRefreshFinish(false);
            statusView.setStatusType(ZKStatusView.STATUS_NETWORK_ERROR);
            statusView.setVisibility(VISIBLE);
            return;
        }
    }

    /**
     * 设置下拉刷新结束状态
     * * 结束刷新
     * * Api使用参考 https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
     *
     * @param isSuccess true 成功 false 失败
     */
    public void setRefreshFinish(boolean isSuccess) {
        if (smartRefreshLayout == null) {
            throw new NullPointerException("smartRefreshLayout is not null...");
        }
        smartRefreshLayout.finishRefresh(isSuccess);
    }

    /**
     * 设置上来加载更多结束
     *
     * @param isSuccess true 成功 false 结束
     */
    public void setLoadMoreFinish(boolean isSuccess) {
        if (smartRefreshLayout == null) {
            throw new NullPointerException("smartRefreshLayout is not null...");
        }
        smartRefreshLayout.finishLoadMore(isSuccess);
    }

    /**
     * 设置网络请求异常状态
     *
     * @param appException 网络请求异常对象 {@link com.zhongke.core.http.httpbase.exception.AppException}
     */
    public void setAppException(AppException appException) {
        //1.要是已经有显示数据，状态View不给与显示
        Adapter adapter = rlvData.getAdapter();
        if (adapter == null) {
            throw new NullPointerException("RecyclerView adapter is null....");
        }
        if (!(adapter instanceof BaseQuickAdapter)) {
            throw new NullPointerException("RecyclerView adapter no instanceof BaseQuickAdapter....");
        }
        BaseQuickAdapter baseQuickAdapter = (BaseQuickAdapter) adapter;
        if (baseQuickAdapter.getItemCount() > 0) {//有数据，进行拦截
            return;
        }
        //2.第一次请求数据异常状态
        setStatus(STATUS_NET_WORK_ERROR);
    }

    public int getCurrentPage() {
        return currentPage;
    }
}



