package com.example.s164403.foodstr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.s164403.foodstr.database.Model.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class TimelineView extends View {
    private Paint mLinePaint;
    private Paint mLinePaint2;

    private Paint mHotPaint;
    private Paint mColdPaint;

    private int mHotColor = Color.RED;
    private int mColdColor = Color.BLUE;
    private float mStepSpacing = 0;
    private int maxlines = 0;

    //ATTRIBUTES
    private int linedist = 30;
    private int markerdist = 10;

    public TimelineView(Context context) {
        super(context);
        init(null, 0);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TimelineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TimelineView, defStyle, 0);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(8);
        mLinePaint.setTextSize(40);

        mLinePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint2.setStrokeWidth(4);
        mLinePaint2.setTextSize(30);

        mHotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHotPaint.setStrokeWidth(8);
        mHotColor = a.getColor(R.styleable.TimelineView_hotColor, mHotColor);
        mHotPaint.setColor(mHotColor);

        mColdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColdPaint.setStrokeWidth(8);
        mColdColor = a.getColor(R.styleable.TimelineView_coldColor, mColdColor);
        mColdPaint.setColor(mColdColor);

        mStepSpacing = a.getDimension(R.styleable.TimelineView_stepSpacing, mStepSpacing);

        //loadTimeline(Timeline.getTestTimeline());

        a.recycle();
    }

    private ArrayList<RecipeStep> steps;
    private int finishtime;
    public void loadTimeline(Timeline timeline){
        steps = timeline.getSteps();
        finishtime = timeline.getFinishTime();

        maxlines = 0;
        for (RecipeStep step : steps){
            if (step.getLine() >= maxlines) maxlines = step.getLine()+1;
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (steps != null && finishtime > 0){
            canvas.drawLine(paddingLeft, 40, paddingLeft, 70, mLinePaint);
            canvas.drawText("0", paddingLeft - 10, 35, mLinePaint);

            canvas.drawLine(contentWidth + paddingLeft, 40, contentWidth + paddingLeft, 70, mLinePaint);
            canvas.drawText(String.valueOf(finishtime), contentWidth + paddingLeft - 20, 35, mLinePaint);

            float dist = (float)contentWidth/finishtime;
            markerdist = Math.max((finishtime/100)*10, 10);

            for (int i = markerdist; i < finishtime; i+= markerdist){
                canvas.drawLine(i*dist + paddingLeft, 53, i*dist + paddingLeft, 70, mLinePaint2);
                canvas.drawText(String.valueOf(i), i*dist + paddingLeft - 15, 48, mLinePaint2);
            }

            for (RecipeStep step : steps){
                int end = finishtime - step.getStartTime();
                int start = end - step.getTime();
                int height = 90 + step.getLine()*linedist;

                canvas.drawLine(start*dist + paddingLeft + mStepSpacing/2, height, end*dist + paddingLeft - mStepSpacing/2, height, step.getHot() ? mHotPaint : mColdPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int dheight = 90 + maxlines*linedist;
        System.out.println(""+dheight + ", " + maxlines);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(dheight, heightSize);
        } else {
            height = dheight;
        }
        setMeasuredDimension(width, height);
    }
}
