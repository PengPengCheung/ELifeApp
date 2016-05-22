package com.pengpeng.elifelistenapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.pengpeng.elifelistenapp.R;

/**
 * Created by pengpeng on 16-3-29.
 */
public class LineEditText extends EditText {

    private int mColor = Color.BLACK;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;
    private EditText t;

    public LineEditText(Context context) {
        super(context);
        init(context);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineEditText);
        mColor = typedArray.getColor(R.styleable.LineEditText_line_color, Color.BLUE);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        t = new EditText(mContext);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isEnabled()){
            int numWidth = 0;
            int w = this.getMeasuredWidth();
            int x = this.getScrollX();
            canvas.drawLine(getPaddingLeft() + numWidth, this.getHeight() - this.getCompoundPaddingBottom(), w + x +numWidth - getPaddingRight(),
                    this.getHeight() - this.getCompoundPaddingBottom(), mPaint);
        }
    }
}
