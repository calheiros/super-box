package com.jefferson.superbox.br;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * An image button that uses a blue highlight (@link android.R.attr.selectableItemBackground} to
 * indicate pressed and focused states.
 */
public class ImageTouch extends ImageButton {
   
    private Drawable mForegroundDrawable;

    /**
     * The cached bounds of the view.
     */
    private Rect mCachedBounds = new Rect();

    public ImageTouch(Context context) {
        super(context);
        init();
    }
	
    public ImageTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageTouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    /**
     * General view initialization used common to all constructors of the view.
     */
    private void init() {
        // Reset default ImageButton background and padding.
        setBackgroundColor(0);
        setPadding(0, 0, 0, 0);

        // Retrieve the drawable resource assigned to the android.R.attr.selectableItemBackground
        // theme attribute from the current theme.
        TypedArray a = getContext()
			.obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
        mForegroundDrawable = a.getDrawable(0);
        mForegroundDrawable.setCallback(this);
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        // Update the state of the highlight drawable to match
        // the state of the button.
        if (mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
        }

        // Trigger a redraw.
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // First draw the image.
        super.onDraw(canvas);

        // Then draw the highlight on top of it. If the button is neither focused
        // nor pressed, the drawable will be transparent, so just the image
        // will be drawn.
        mForegroundDrawable.setBounds(mCachedBounds);
        mForegroundDrawable.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Cache the view bounds.
        mCachedBounds.set(0, 0, w, h);
		
    }
}
