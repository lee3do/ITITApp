package io.itit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.trinea.android.common.util.ToastUtils;
import io.itit.domain.Item;
import io.itit.http.HttpUtils;
import io.itit.ui.NewsAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends Fragment implements BGARefreshLayout
        .BGARefreshLayoutDelegate {
    @Bind(R.id.news_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_modulename_refresh)
    BGARefreshLayout mRefreshLayout;
    private List<Item.ItemsBean> newsList = new ArrayList<>();
    int index = 1;
    int pos = 1;

    private NewsAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            pos = getArguments().getInt("POS");
            setRetainInstance(true);
        }
    }

    private void loadList(int pageIndex) {
        index = pageIndex;
        Logger.d("load index:" + index);
        if (pos == 1) {
            HttpUtils.appApis.getRecommends(index).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(info -> {
                if (info.getItems().size() == 0) {
                    ToastUtils.show(getActivity(), "文章列表为空!");
                } else {
                    mAdapter.updateNewsList(info.getItems(), index == 1);
                    index++;
                }
                mRefreshLayout.endRefreshing();
            }, error -> {
                Logger.e(error.getLocalizedMessage());
                ToastUtils.show(getActivity(), "获取文章失败!");
                mRefreshLayout.endRefreshing();
            });
        } else if (pos == 2) {
            HttpUtils.appApis.getNews(index).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(info -> {
                if (info.getItems().size() == 0) {
                    ToastUtils.show(getActivity(), "文章列表为空!");
                } else {
                    mAdapter.updateNewsList(info.getItems(), index == 1);
                    index++;
                }
                mRefreshLayout.endRefreshing();
            }, error -> {
                Logger.e(error.getLocalizedMessage());
                ToastUtils.show(getActivity(), "获取文章失败!");
                mRefreshLayout.endRefreshing();
            });
        } else if (pos == 3) {
            HttpUtils.appApis.getLikes(index).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(info -> {
                if (info.getItems().size() == 0) {
                    ToastUtils.show(getActivity(), "文章列表为空!");
                } else {
                    mAdapter.updateNewsList(info.getItems(), index == 1);
                    index++;
                }
                mRefreshLayout.endRefreshing();
            }, error -> {
                Logger.e(error.getLocalizedMessage());
                ToastUtils.show(getActivity(), "获取文章失败!");
                mRefreshLayout.endRefreshing();
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        assert view != null;
        ButterKnife.bind(this, view);
        initListView();
        initPullToRefresh();
        loadList(1);
        return view;
    }

    private void initPullToRefresh() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("载入更多");
        // 设置整个加载更多控件的背景颜色资源id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景drawable资源id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景drawable资源id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    private void initListView() {
        mRecyclerView.setHasFixedSize(false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new NewsAdapter(newsList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        loadList(1);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        loadList(index);
        return false;
    }
}
