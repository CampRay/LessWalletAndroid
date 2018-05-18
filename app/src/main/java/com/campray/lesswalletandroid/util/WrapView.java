package com.campray.lesswalletandroid.util;

import android.view.View;

/**
 * View控件属性封装类
 * (对于属性动画，大多需要设置View控件的宽和高，但很多控件的属性没有getXXX或SetXXX，这样就不能配置属性动画了。
 * 如果为View控件用此类进行封装，就可以配置属性动画)
 * 包装类用于封装View的with、height,
 * 你可以通过getXxx以及setXxx方法设置View的宽和高
 * Created by Phills on 11/30/2017.
 */

public class WrapView {
    private View view;
    private int width;
    private int height;
    public WrapView(View view){
        this.view=view;
    }
    public int getWidth() {
        return view.getLayoutParams().width;
    }
    public void setWidth(int width) {
        this.width = width;
        view.getLayoutParams().width=width;//修改View的高度
        view.requestLayout();//刷新View的布局
    }
    public int getHeight() {
        return view.getLayoutParams().height;
    }
    public void setHeight(int height) {
        this.height=height;
        view.getLayoutParams().height = height;
        view.requestLayout();
    }
}
