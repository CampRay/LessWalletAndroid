package com.campray.lesswalletandroid.adapter.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * 列表视图对象的显示支架
 * 与BaseRecyclerAdapter一起使用
 *
 */
public class BaseRecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;
    public  int layoutId;

    public BaseRecyclerHolder(int layoutId,View itemView) {
        super(itemView);
        this.layoutId =layoutId;
        this.mViews = new SparseArray<>(8);
    }

    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 根据View的资源ID获取View对象
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置View对象显示的文本
     * @param viewId
     * @param text
     * @return
     */
    public BaseRecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置View对象显示的文本
     * @param viewId
     * @param resid
     * @return
     */
    public BaseRecyclerHolder setText(int viewId, int resid) {
        TextView view = getView(viewId);
        view.setText(resid);
        return this;
    }

    /**
     * 设置View对象显示的背景色
     * @param viewId
     * @param color
     * @return
     */
    public BaseRecyclerHolder setBackgroundColor(int viewId, String color) {
        View view = getView(viewId);
        view.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    /**
     * 设置View对象显示的背景色
     * @param viewId
     * @param color
     * @return
     */
    public BaseRecyclerHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置View对象显示的背景资源
     * @param viewId
     * @param drawableId
     * @return
     */
    public BaseRecyclerHolder setBackgroundResource(int viewId, int drawableId) {
        View view = getView(viewId);
        view.setBackgroundResource(drawableId);
        return this;
    }

    /**
     * 设置View对象是否可用
     * @param viewId
     * @param enable
     * @return
     */
    public BaseRecyclerHolder setEnabled(int viewId,boolean enable){
        View v = getView(viewId);
        v.setEnabled(enable);
        return this;
    }

    /**
     * 设置View对象的点击事件
     * @param viewId
     * @param listener
     * @return
     */
    public BaseRecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener){
        View v = getView(viewId);
        v.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置View对象是否可见
     * @param viewId
     * @param visibility
     * @return
     */
    public BaseRecyclerHolder setVisible(int viewId,int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }
    /**
     * 设置View对象是否可见的开关，第一次调用是开，再调用就是关
     * @param viewId
     * @return
     */
    public BaseRecyclerHolder setVisibleToggle(int viewId) {
        View view = getView(viewId);
        if(view.isShown()) {
            view.setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置View对象显示的图像资源
     * @param viewId
     * @param drawableId
     * @return
     */
    public BaseRecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 设置View对象显示的图像
     * @param viewId
     * @param bm
     * @return
     */
    public BaseRecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 设置View对象显示的图像
     * @param viewId
     * @param url
     * @param defaultRes 默认显示图片
     * @return
     */
    public BaseRecyclerHolder setImageView(int viewId,String url, int defaultRes) {
        ImageView iv = getView(viewId);
        Picasso.with(LessWalletApplication.INSTANCE()).load(url).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv);
        //iv.setImageBitmap(BitmapFactory.decodeFile(url));
        return this;
    }

    /**
     * 为View对象设置点击监听器
     * @param viewId
     * @param listener
     */
    public void setOnclickListener(int viewId,View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }

    /**
     * 为View对象设置点击监听器
     * @param viewId
     * @param listener
     */
    public void setOnTouchListener(int viewId,View.OnTouchListener listener){
        View view = getView(viewId);
        view.setOnTouchListener(listener);
    }
}