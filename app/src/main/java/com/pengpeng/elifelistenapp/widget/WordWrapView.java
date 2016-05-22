package com.pengpeng.elifelistenapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pengpeng.elifelistenapp.utils.Tools;

import java.util.List;

/**
 * Created by pengpeng on 16-3-29.
 */
public class WordWrapView extends ViewGroup {

    private static final int SIDE_MARGIN = 10;//左右间距
    private static final int TEXT_MARGIN = 5;

    private Context mContext;
    private EditText editText;

    public WordWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WordWrapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public WordWrapView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    /**
     *
     * @param changed View大小是否发生改变
     * @param l 相对于父容器的左坐标
     * @param t 相对于父容器的顶坐标
     * @param r 相对于父容器的右坐标
     * @param b 相对于父容器的底坐标
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int actualWidth = r - l;
        int x = SIDE_MARGIN;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
//            view.setBackgroundColor(Color.GREEN);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            x += width + TEXT_MARGIN;
            //此时的actualwidth为实际的布局宽度，而x是指相对于父容器的横坐标，所以需要减去多加的side_margin,因为这个值不属于布局实际宽度的一部分
            if (x - SIDE_MARGIN > actualWidth) {
                x = width + SIDE_MARGIN + TEXT_MARGIN;
                rows++;
            }
            y = rows * height;
            view.layout(x - width - TEXT_MARGIN, y - height, x - TEXT_MARGIN, y);
        }
    }

    /**
     *
     * @param widthMeasureSpec view自身的宽度规格说明
     * @param heightMeasureSpec view自身的高度规格说明
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 1;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int actualWidth = specWidth - SIDE_MARGIN * 2;//实际宽度
        int childCount = getChildCount();
        int index;
        for (index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            int widthSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            int heightSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            child.measure(widthSpec, heightSpec);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            x += width + TEXT_MARGIN;
            if (x > actualWidth) {//换行
                x = width;
                rows++;
            }
            y = rows * height;
        }
        setMeasuredDimension(actualWidth, y);//这里指整段布局所占的宽度和高度，而不是某个单独的textview或者edittext，y从布局的顶格开始，
    }

    public void initExercise(List<String> strlist, List<Integer> blanks, WordWrapView parentView) {
        int b = 0;
        for (int i = 0; i < strlist.size(); i=i+1) {
            editText = new LineEditText(mContext);
            int width = 0;
            if (b < blanks.size() && blanks.get(b) == i) {

                EditText t = new LineEditText(mContext);
                parentView.addView(t);
                String s = "("+(b+1)+")";
                t.setEnabled(false);//设置不可编辑
                t.setText(s);
                t.setBackgroundDrawable(null);
                t.setPadding(0, 3, 0, 3);
                t.setTextColor(Color.BLACK);
                t.setTextSize(20.0f);
                t.setHeight(Tools.dp2px(mContext, 40));


                parentView.addView(editText);
                editText.setTextSize(20.0f);
                String str = strlist.get(i);
                if(str!=null && !str.equals("")){
                    //挖空的单词，设置文本内容，以便测量挖空的长度
                    editText.setText(str.substring(0, str.length() - 1));
                }


                //强制对文本框进行测量，获取测量后的宽高
                int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
                editText.measure(widthMeasureSpec, heightMeasureSpec);

                width = editText.getMeasuredWidth();
                Log.i("width", String.valueOf(width));

                editText.setText(null);
                editText.setSingleLine(true);
                editText.setBackgroundDrawable(null);
                editText.setPadding(10, 3, 10, 3);
                editText.setWidth(width);
                ++b;
                Log.i("words", strlist.get(i));
            } else {
                parentView.addView(editText);
                //不挖空的文本
                editText.setEnabled(false);//设置不可编辑
                editText.setText(strlist.get(i));
                editText.setBackgroundDrawable(null);
                editText.setPadding(10, 3, 10, 3);
                editText.setTextColor(Color.BLACK);
                editText.setTextSize(20.0f);
                Log.i("words", strlist.get(i));
            }
            editText.setHeight(Tools.dp2px(mContext, 40));

        }
    }

}
