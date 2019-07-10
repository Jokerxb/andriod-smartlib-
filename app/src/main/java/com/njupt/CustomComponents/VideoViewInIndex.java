package com.njupt.CustomComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoViewInIndex extends VideoView {

    public VideoViewInIndex(Context context) {
        super(context);
    }

    public VideoViewInIndex(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewInIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //  DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

}
