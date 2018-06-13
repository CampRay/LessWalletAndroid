package com.campray.lesswalletandroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campray.lesswalletandroid.R;

/**
 * 自定义带圆角的ImageView
 * Created by Phills on 18-4-6.
 */
@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView{

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;

    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;

    private RectF mRoundRect;

    public RoundImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);

        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BODER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));// 默认为10dp
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        if (getDrawable() == null)
        {
            return;
        }
        setUpShader();

        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else
        {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
            // drawSomeThing(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE)
        {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    public void setBorderRadius(int borderRadius)
    {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal)
        {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    /**
     * 动态改变圆角类型
     * @param type
     */
    public void setType(int type)
    {
        if (this.type != type)
        {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE)
            {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader()
    {
        //获取设置的src图片
        Drawable drawable = getDrawable();
        if (drawable == null)
        {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float xScale = 1.0f;
        float yScale = 1.0f;
        if (type == TYPE_CIRCLE)
        {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            xScale = mWidth * 1.0f / bSize;
            yScale=xScale;

        } else if (type == TYPE_ROUND)
        {
            if(getScaleType()==ScaleType.FIT_XY) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                xScale = getWidth() * 1.0f / bmp.getWidth();
                yScale =getHeight()* 1.0f / bmp.getHeight();
            }
            else if(getScaleType()==ScaleType.FIT_START) {
                xScale=bmp.getWidth()* 1.0f;
                yScale =bmp.getHeight()* 1.0f;
            }
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(xScale, yScale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
