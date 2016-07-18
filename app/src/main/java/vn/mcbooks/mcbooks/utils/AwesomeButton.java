package vn.mcbooks.mcbooks.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class AwesomeButton extends Button {

    public AwesomeButton(Context context) {
        super(context);
        init();
    }

    public AwesomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AwesomeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        Typeface typeface =
//                Typeface.createFromAsset(getContext().getAssets(),
//                        "fontawesome_webfont.ttf");
//        setTypeface(typeface);
    }
}
