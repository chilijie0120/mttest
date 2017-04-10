package chilijie.baway.com.chilijie20170410;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by MacBook- on 2017/4/10.
 */
public class CustomCircle extends View {
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;
    public CustomCircle(Context context) {
        super(context);
    }

    public CustomCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        gatAttrs(context,attrs);
    }

    public CustomCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gatAttrs(context,attrs);
    }
    private void gatAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomCircle);
        roundColor = typedArray.getInt(R.styleable.CustomCircle_roundColor, Color.RED);
        roundProgressColor = typedArray.getColor(R.styleable.CustomCircle_roundProgressColor, Color.GREEN);
        textColor = typedArray.getColor(R.styleable.CustomCircle_textColor, Color.GREEN);
        textSize = typedArray.getDimension(R.styleable.CustomCircle_textSize, 15);
        roundWidth = typedArray.getDimension(R.styleable.CustomCircle_roundWidth, 5);
        max = typedArray.getInteger(R.styleable.CustomCircle_max, 100);
        textIsDisplayable = typedArray.getBoolean(R.styleable.CustomCircle_textIsDisplayable, true);
        style = typedArray.getInt(R.styleable.CustomCircle_style, 0);
        typedArray.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint=new Paint();
        /**
         * 画最外层的大圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth/2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if(textIsDisplayable && percent != 0 && style == STROKE){

            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
        }


        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }


    }
    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }
}
