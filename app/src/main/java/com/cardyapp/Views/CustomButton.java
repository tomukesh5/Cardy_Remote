package com.cardyapp.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.cardyapp.Utils.TypefaceLoader;

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        super(context);
        doInit(null, context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit(attrs, context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doInit(attrs, context);
    }

    private void doInit(AttributeSet attrs, Context context) {

        /*if (isInEditMode() || null == attrs) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BvgButton);
        int index = styledAttrs.getInt(R.styleable.BvgButton_customTypeFace, 1);
        String font;
        switch (index) {
            case 1:
                font = "Dosis-Medium.ttf";
                break;

            case 2:
                font = "Dosis-Book.ttf";
                break;

            case 3:
                font = "Dosis-Light.ttf";
                break;

            case 4:
                font = "Dosis-ExtraLight.ttf";
                break;

            default:
                font = "Dosis-Medium.ttf";
        }
        styledAttrs.recycle();
        Typeface typeface = TypefaceLoader.get(context, "fonts/" + font);
        setTypeface(typeface);*/
    }
}
