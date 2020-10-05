package com.daydreaminger.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通过不同颜色显示占比的线条
 *
 * @author : daydreaminger
 * @date : 2020/8/2 16:28
 */
public class ProportionLine extends View {
    private static final String TAG = "ProportionLine";

    private List<CharSequence> colorSeq = new ArrayList<>();
    private float[] proportions;
    private Paint linePaint;

    private float paddingLeft, paddingTop, paddingRight, paddingBottom;
    private float marginLeft, marginTop, marginRight, marginBottom;
    private int minHeight;

    public ProportionLine(Context context) {
        this(context, null);
    }

    public ProportionLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProportionLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
        initPaint();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProportionLine);
            colorSeq = new ArrayList<>(Arrays.asList(array.getTextArray(R.styleable.ProportionLine_colorSeq)));

            paddingLeft = array.getDimensionPixelSize(R.styleable.ProportionLine_android_paddingLeft, 0);
            paddingTop = array.getDimensionPixelSize(R.styleable.ProportionLine_android_paddingLeft, 0);
            paddingRight = array.getDimensionPixelSize(R.styleable.ProportionLine_android_paddingLeft, 0);
            paddingBottom = array.getDimensionPixelSize(R.styleable.ProportionLine_android_paddingLeft, 0);
            float padding = array.getDimensionPixelSize(R.styleable.ProportionLine_android_padding, -1);
            if (padding >= 0) {
                paddingLeft = paddingTop = paddingRight = paddingBottom = padding;
            }

            marginLeft = array.getDimensionPixelSize(R.styleable.ProportionLine_android_layout_marginStart, 0);
            marginTop = array.getDimensionPixelSize(R.styleable.ProportionLine_android_layout_marginTop, 0);
            marginRight = array.getDimensionPixelSize(R.styleable.ProportionLine_android_layout_marginRight, 0);
            marginBottom = array.getDimensionPixelSize(R.styleable.ProportionLine_android_layout_marginBottom, 0);
            float margin = array.getDimensionPixelSize(R.styleable.ProportionLine_android_layout_margin, -1);
            if (margin >= 0) {
                marginLeft = marginTop = marginRight = marginBottom = margin;
            }

            minHeight = array.getDimensionPixelSize(R.styleable.ProportionLine_android_minHeight,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));

            array.recycle();
        }

    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure layout size: " + widthSize + "," + heightSize);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (minHeight + paddingTop + paddingBottom), heightMode);
                break;
            default:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                break;
        }

        //不管如何，宽度占满父控件，高度自适应
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //定位可以不动
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (proportions == null || proportions.length == 0) {
            linePaint.setColor(Color.BLUE);
            canvas.drawRect(paddingLeft, paddingTop, getWidth() - paddingRight, getHeight() - paddingBottom, linePaint);
        } else {
            float start = 0 + paddingLeft;
            float nextStart;
            int realWidth = (int) (getWidth() - paddingRight - paddingLeft);
            for (int i = 0; i < proportions.length; i++) {
                linePaint.setColor(Color.parseColor((String) colorSeq.get(i)));
                nextStart = start + proportions[i] * realWidth;
                canvas.drawRect(start, paddingTop, nextStart, getHeight() - paddingBottom, linePaint);
                start = nextStart;
            }
        }
    }

    public void setProportions(float[] proportions) {
        checkSum(proportions);
        this.proportions = proportions;
    }

    private void checkSum(float[] proportions) {
        float sum = 0;
        for (int i = 0; i < proportions.length; i++) {
            sum += proportions[i];
        }
        if (sum < 0.99) {
            throw new IllegalArgumentException("占比总数小于100%.");
        }
    }
}
