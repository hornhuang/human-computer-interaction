package com.example.mindhlju.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

/**
 * Created by xiang
 * on 2018/11/14 15:51
 */
public class BlockView extends View {
    private Paint mpaint;
    private RectF rectF;
    private float radius = 10f;
    private Paint mtitlePaint;
    private Rect mtitleBound;
    private int starColor = Color.parseColor("#f0b9cc");

    public BlockView(Context context) {
        super(context);
        init();
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        mpaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mpaint.setAntiAlias(true);
        mpaint.setColor(Color.parseColor("#FFA7A0A3"));
        rectF = new RectF();

        mtitlePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mtitlePaint.setAntiAlias(true);
        mtitlePaint.setTextSize(dip2px(getContext(), 18));

        mtitleBound = new Rect();
        mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);

//        GradientDrawable gd = new GradientDrawable();//创建drawable
//        gd.setColor(Color.parseColor("#FFA7A0A3"));
//        gd.setCornerRadius(10f);
//
//        setBackground(gd);

    }

    public void setBackgroundColor(@ColorInt int bgcolor) {
//        GradientDrawable myGrad = (GradientDrawable) getBackground();
//        myGrad.setColor(bgcolor);
        this.bgcolor = bgcolor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mtitlePaint.setTextSize(dip2px(getContext(), 18));
            mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);
            int desired = getPaddingLeft() + mtitleBound.width() + getPaddingRight();
            width = desired <= widthSize ? desired : widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mtitlePaint.setTextSize(dip2px(getContext(), 18));
            mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);
            int desired = getPaddingTop() + mtitleBound.height() + getPaddingBottom();
            height = desired <= heightSize ? desired : heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rec = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mpaint.setColor(bgcolor);
        if (text.equals("") || text.equals("0")) {
            return;
        }
        canvas.drawRoundRect(rec, radius, radius, mpaint);
        mtitlePaint.setColor(Color.parseColor("#FFFFFF"));
        Paint.FontMetricsInt fontMetrics = mtitlePaint.getFontMetricsInt();
        mtitlePaint.setTextAlign(Paint.Align.CENTER);
        int baseliney = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        //设置了 setTextAlign(Paint.Align.CENTER); drawText的第二三个参数表示文字中心的x,y
        canvas.drawText(text + "", getMeasuredWidth() / 2, baseliney, mtitlePaint);

    }

    private String text = "";
    private int bgcolor = Color.parseColor("#FFA7A0A3");

    public void setText(int text) {
        this.text = text + "";
        float n = (float) (Math.log(text) / Math.log(2));
        //背景色递增
        this.bgcolor = n > 1 ? getColorChanges(starColor, n * 6f) : starColor;
        invalidate();
    }

    public void setScale(float scale){
        setScaleX(scale);
        setScaleY(scale);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getColorChanges(int startcolor, float angle) {
        int R, G, B;
        // 颜色的渐变，应该把分别获取对应的三基色，然后分别进行求差值；这样颜色渐变效果最佳
        R = (int) (Color.red(startcolor) + Color.red(startcolor) * angle);
        G = (int) (Color.green(startcolor) + Color.green(startcolor) * angle);
        B = (int) (Color.blue(startcolor) + Color.blue(startcolor) * angle);

        return Color.rgb(R, G, B);
    }
    private int getColorChanges(int startcolor, int endcolor, float angle) {
        int R, G, B;
        // 颜色的渐变，应该把分别获取对应的三基色，然后分别进行求差值；这样颜色渐变效果最佳
        R = (int) (Color.red(startcolor) + (Color.red(endcolor) - Color.red(startcolor)) * angle);
        G = (int) (Color.green(startcolor) + (Color.green(endcolor) - Color.green(startcolor)) * angle);
        B = (int) (Color.blue(startcolor) + (Color.blue(endcolor) - Color.blue(startcolor)) * angle);

        return Color.rgb(R, G, B);
    }
}
