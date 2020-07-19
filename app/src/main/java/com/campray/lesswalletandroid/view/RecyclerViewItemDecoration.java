package com.campray.lesswalletandroid.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.campray.lesswalletandroid.R;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * RecyclerView的列表对象分隔线绘制类
 * 通过RecyclerView.addItemDecoration(new RecyclerViewItemDecoration())方式添加
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    //分隔线高度
    private int dividerHeight;
    //分隔线画笔
    private Paint dividerPaint;
    public RecyclerViewItemDecoration(Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.y1);
    }

    /**
     * 可以绘制一个RecyclerView中item的分隔画布，分隔画布显示在item的后面
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//        final int childCount = parent.getChildCount();
//        // 画每个item的分割线
//        for (int i = 0; i < childCount-1; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
//            final int bottom = top + dividerHeight;
//            c.drawRect(left,top,right,bottom,dividerPaint);
//        }
    }

    /**
     * 可以绘制一个RecyclerView中item的分隔画布，分隔画布显示item在的前面
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //super.onDrawOver(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        // 画每个item的分割线
        for (int i = 0; i < childCount-1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + dividerHeight;
            c.drawRect(left,top,right,bottom,dividerPaint);
        }
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
        //设置RecyclerView中item的显示区域底部偏移指定高度,类似添加了一个bottom padding,
        //outRect.bottom=dividerHeight;
    }
}
