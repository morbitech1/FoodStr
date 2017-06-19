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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;


/**
 * TODO: document your custom view class.
 */
public class RecipeStepsView extends View {

    private Paint PaintBrush = new Paint();
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private boolean expanded;
    private View expandedview;

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

    private View root;
    private void init(AttributeSet attrs, int defStyle) {
        PaintBrush.setColor(Color.BLACK);

        root = inflate(getContext(), R.layout.sample_recipe_steps_view, null);
        TextView name = (TextView)root.findViewById(R.id.nametext);
        name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = new ExpandAnimation(200, 60);
                anim.setDuration(500);
                startAnimation(anim);
                expanded = !expanded;

                if (expanded) {
                    expand(root.findViewById(R.id.description));
                }else{
                    collapse(root.findViewById(R.id.description));
                }
            }
        });

        TextView desc = (TextView) root.findViewById(R.id.description);
        desc.setVisibility(View.GONE);
        //desc.setText("");
    }
    public View getRootView(){return root;}

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawLine(0,1,getWidth(),1,PaintBrush);
    }

    private class ExpandAnimation extends Animation {
        private final int start;
        private final int delta;

        public ExpandAnimation(int start, int end) {
            this.start = start;
            delta = end - start;
        }

        @Override
        protected void applyTransformation(float dt, Transformation t) {
            android.view.ViewGroup.LayoutParams param = getLayoutParams();
            //Change the height over the time
            param.height = (int) (start + delta * dt);
            setLayoutParams(param);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
