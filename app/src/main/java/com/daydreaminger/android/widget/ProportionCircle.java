package com.daydreaminger.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;

/**
 * 圆形百分比占比控件
 *
 * @author : daydreaminger
 * @date : 2020/9/30 13:31
 */
public class ProportionCircle extends View {
    private static final String TAG = "ProportionCircle";

    private int type;
    private int innerPadding;

    private int realSize;

    public ProportionCircle(Context context) {
        this(context, null);
    }

    public ProportionCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ProportionCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        splitAttr(attrs);
    }

    public ProportionCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        splitAttr(attrs);
    }

    /**
     * 读取属性信息
     */
    private void splitAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.ProportionCircle);

        type = ta.getInt(R.styleable.ProportionCircle_circle_style, 0);
        innerPadding = ta.getDimensionPixelSize(R.styleable.ProportionCircle_inner_padding, 0);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        //按区域的最小边取正方形，根据具体内容绘制在正方形内部
        realSize = Math.min(sizeWidth, sizeHeight);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
