package com.campray.lesswalletandroid.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.campray.lesswalletandroid.R;


/**
 * RecyclerView的列表对象空白分隔线绘制类
 * 通过RecyclerView.addItemDecoration(new RecyclerViewItemDecoration())方式添加
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    //分隔线高度
    int mSpace;
    public SpaceItemDecoration(int space) {
        mSpace = space;
    }



    /**
     * 可以设置item的偏移量(padding)，偏移的部分用于填充间隔样式，即设置分割线的宽、高;
     * (分隔线其实显示的是背景颜色)
     * @param outRect item对象的矩形显示区域
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;

    }
}
