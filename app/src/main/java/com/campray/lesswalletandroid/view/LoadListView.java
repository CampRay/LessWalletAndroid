package com.campray.lesswalletandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.campray.lesswalletandroid.R;

/**
 * 上拉加载下一页数据列表
 * Created by Phills on 11/20/2017.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    View footer;
    int totalItemCount;
    int lastVisibleItem;
    boolean isLoading;
    ILoadListener2 iListener2;
    public LoadListView(Context context) {
        super(context);
        initView(context);
    }
    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.listview_footer, null);
        footer.findViewById(R.id.ll_load).setVisibility(GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    /*
     * ListView的手指操作处理
     * SCROLL_STATE_FLING 手指做出抛的动作
     * SCROLL_STATE_IDLE 停止滚动
     * SCROLL_STATE_TOUCH_SCROLL 手指在屏幕上，屏幕滚动
     * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollStatus) {
        if(totalItemCount == lastVisibleItem
                && scrollStatus == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading = true;
                footer.findViewById(R.id.ll_load).setVisibility(VISIBLE);
                //加载更多数据
                iListener2.onLoad();
            }
        }
    }

    /**
     * 加载新页面数据完成后，调用此方法隐藏底部加载显示块
     */
    public void loadComplete(){
        /**
         * 设置默认显示为Listview最后一行
         */
        this.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        this.setStackFromBottom(true);
        isLoading = false;
        footer.findViewById(R.id.ll_load).setVisibility(GONE);
    }

    /**
     * 设置加载新页面数据侦听接口实现实例对象
     * @param iListener2
     */
    public void setInterface(ILoadListener2 iListener2){
        this.iListener2 = iListener2;
    }

    /**
     * 加载新页面数据侦听接口，需在此View控件的实现Activity中实现
     */
    public interface ILoadListener2{
        public void onLoad();
    }

}
