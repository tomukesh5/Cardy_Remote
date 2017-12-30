package com.cardyapp.Views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.cardyapp.R;


/**
 * Created by Umesh on 06-10-2016.
 */
public class CardyProgressBar extends ProgressBar {
    public CardyProgressBar(Context context) {
        super(context);
        this.setIndeterminate(true);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    public CardyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    public CardyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
