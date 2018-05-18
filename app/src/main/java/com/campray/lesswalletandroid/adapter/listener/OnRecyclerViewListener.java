package com.campray.lesswalletandroid.adapter.listener;

/**
 * 为RecycleView添加点击事件
 * @author Phills
 * @project OnRecyclerViewListener
 * @date 2017-11-03-16:39
 */
public interface OnRecyclerViewListener {
    void onItemClick(int position);
    boolean onItemLongClick(int position);
}
