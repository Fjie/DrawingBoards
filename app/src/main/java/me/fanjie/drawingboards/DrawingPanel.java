package me.fanjie.drawingboards;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by fanji on 2016/12/4.
 */

public class DrawingPanel extends FrameLayout {


    public DrawingPanel(Context context) {
        super(context);
    }

    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawLine(start.x,start.y,stop.x,stop.y,paint);
    }

    private void drawingLine(){

    }

    private Point start;
    private Point stop;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                start = new Point(x,y);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                stop = new Point(x,y);
                drawingLine();
                break;
            }

        }


        return true;
    }
}
