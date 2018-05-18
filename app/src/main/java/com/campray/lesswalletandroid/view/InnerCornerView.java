package com.campray.lesswalletandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.campray.lesswalletandroid.R;
/**
 * 自定义的可以设置颜色和方向的内凹90度边角样式的View
 * Created by Phills on 11/29/2017.
 */

public class InnerCornerView extends View {
    //显示角度
    private int angle;
    //边角圆弧半径
    private float cornerRadius;
    //票上部分背景颜色
    private int cornerColor;

    private Paint paint;
    private Path path;


    public InnerCornerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InnerCornerView(Context context) {
        this(context, null);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getCornerColor() {
        return cornerColor;
    }

    public void setCornerColor(int cornerColor) {
        this.cornerColor = cornerColor;
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public InnerCornerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获得我们所定义的自定义样式属性
        TypedArray typedArray= context.obtainStyledAttributes(attrs, R.styleable.InnerCornerView, defStyle, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.InnerCornerView_Angle:
                    // 默认颜色设置为绿色
                    angle = typedArray.getInt(attr,0);
                    break;
                case R.styleable.InnerCornerView_Radius:
                    // 默认颜色设置为绿色
                    cornerRadius = typedArray.getDimension(attr,15);
                    break;
                case R.styleable.InnerCornerView_CornerColor:
                    // 默认颜色设置为绿色
                    cornerColor = typedArray.getColor(attr, Color.GREEN);
                    break;
            }
        }
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿效果
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * 重载View开始布局的方法
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int mViewWidth = getWidth();
        int mViewHeight = getHeight();
        //添加票的上部圆角矩形路径
        path = new Path();
        switch (angle){
            case 0:
                path.moveTo(cornerRadius,0);
                path.cubicTo(cornerRadius,0,cornerRadius,cornerRadius,0,cornerRadius);
                path.lineTo(0,mViewHeight);
                path.lineTo(mViewWidth,mViewHeight);
                path.lineTo(mViewWidth,0);
                path.lineTo(cornerRadius,0);
                break;
            case 90:
                path.moveTo(mViewWidth,cornerRadius);
                path.cubicTo(mViewWidth,cornerRadius,mViewWidth-cornerRadius,cornerRadius,mViewWidth-cornerRadius,0);
                path.lineTo(0,0);
                path.lineTo(0,mViewHeight);
                path.lineTo(mViewWidth,mViewHeight);
                path.lineTo(mViewWidth,cornerRadius);
                break;
            case 180:
                path.moveTo(mViewWidth-cornerRadius,mViewHeight);
                path.cubicTo(mViewWidth-cornerRadius,mViewHeight,mViewWidth-cornerRadius,mViewHeight-cornerRadius,mViewWidth,mViewHeight-cornerRadius);
                path.lineTo(mViewWidth,0);
                path.lineTo(0,0);
                path.lineTo(0,mViewHeight);
                path.lineTo(mViewWidth-cornerRadius,mViewHeight);
                break;
            case 270:
                path.moveTo(0,mViewHeight-cornerRadius);
                path.cubicTo(0,mViewHeight-cornerRadius,cornerRadius,mViewHeight-cornerRadius,cornerRadius,mViewHeight);
                path.lineTo(mViewWidth,mViewHeight);
                path.lineTo(mViewWidth,0);
                path.lineTo(0,0);
                path.lineTo(0,mViewHeight-cornerRadius);

                break;
        }


    }

    /**
     * 重载View开始绘制的方法
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(cornerColor);
        canvas.drawPath(path,paint);
    }
}
