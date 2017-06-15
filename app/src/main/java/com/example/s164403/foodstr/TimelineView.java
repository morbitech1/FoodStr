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

import com.example.s164403.foodstr.database.Model.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class TimelineView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Paint mLinePaint;
    private Paint mLinePaint2;

    private Paint mHotPaint;
    private Paint mColdPaint;

    private int mHotColor = Color.RED;
    private int mColdColor = Color.BLUE;
    private float mStepSpacing = 0;

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

        mExampleString = a.getString(
                R.styleable.TimelineView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.TimelineView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.TimelineView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.TimelineView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TimelineView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setTextSize(30);

        mLinePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint2.setStrokeWidth(2);
        mLinePaint2.setTextSize(20);

        mHotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHotPaint.setStrokeWidth(5);
        mHotColor = a.getColor(R.styleable.TimelineView_hotColor, mHotColor);
        mHotPaint.setColor(mHotColor);

        mColdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColdPaint.setStrokeWidth(5);
        mColdColor = a.getColor(R.styleable.TimelineView_coldColor, mColdColor);
        mColdPaint.setColor(mColdColor);

        mStepSpacing = a.getDimension(R.styleable.TimelineView_stepSpacing, mStepSpacing);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        loadTimeline(Timeline.getTestTimeline());
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    private ArrayList<RecipeStep> steps;
    private int finishtime;
    public void loadTimeline(Timeline timeline){
        steps = timeline.getSteps();
        finishtime = timeline.getFinishTime();
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

        // Draw the text.
        //canvas.drawText(mExampleString,
               // paddingLeft + (contentWidth - mTextWidth) / 2,
                //paddingTop + (contentHeight + mTextHeight) / 2,
                //mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            //mExampleDrawable.draw(canvas);
        }

        if (steps != null && finishtime > 0){
            canvas.drawLine(paddingLeft, 40, paddingLeft, 60, mLinePaint);
            canvas.drawText("0", paddingLeft - 10, 35, mLinePaint);

            canvas.drawLine(contentWidth + paddingLeft, 40, contentWidth + paddingLeft, 60, mLinePaint);
            canvas.drawText(String.valueOf(finishtime), contentWidth + paddingLeft - 10, 35, mLinePaint);

            float dist = (float)contentWidth/finishtime;
            int linedist = 20;

            for (int i = 10; i < finishtime; i+= 10){
                canvas.drawLine(i*dist + paddingLeft, 53, i*dist + paddingLeft, 60, mLinePaint2);
                canvas.drawText(String.valueOf(i), i*dist + paddingLeft - 10, 48, mLinePaint2);
            }

            for (RecipeStep step : steps){
                int end = finishtime - step.getStartTime();
                int start = end - step.getTime();
                int height = 80 + step.getLine()*linedist;

                canvas.drawLine(start*dist + paddingLeft + mStepSpacing/2, height, end*dist + paddingLeft - mStepSpacing/2, height, step.getHot() ? mHotPaint : mColdPaint);
            }
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
