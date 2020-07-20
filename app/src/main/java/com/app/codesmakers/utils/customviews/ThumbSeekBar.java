package com.app.codesmakers.utils.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

public class ThumbSeekBar extends AppCompatSeekBar {

    public ThumbSeekBar(Context context) {
        super(context);
    }
    public ThumbSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ThumbSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    Drawable mThumb;
    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }
    public Drawable getSeekBarThumb() {
        return mThumb;
    }

}
