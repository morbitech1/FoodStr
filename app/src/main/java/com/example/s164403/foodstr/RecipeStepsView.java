package com.example.s164403.foodstr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.*;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;


/**
 * TODO: document your custom view class.
 */
public class RecipeStepsView extends View {

    private Paint PaintBrush = new Paint();
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public RecipeStepsView(Context context) {
        super(context);
        init(null, 0);
    }

    public RecipeStepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RecipeStepsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        PaintBrush.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawLine(0,1,getWidth(),1,PaintBrush);


    }

}
