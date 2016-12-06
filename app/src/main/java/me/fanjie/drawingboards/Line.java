package me.fanjie.drawingboards;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * @Description: TODO 添加类的描述
 * @author: fanjie
 * @date: 2016/12/5 15:52
 */

public class Line implements DrawingView {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private Paint paint;
    private boolean selected;
    public HolderPoint point;

    public Line(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void drawing(Canvas canvas) {
        if(selected){
            paint.setColor(Color.BLACK);
            canvas.drawCircle(startX,startY,20,paint);
            canvas.drawCircle(stopX,stopY,20,paint);
        }else {
            paint.setColor(Color.RED);
        }
        canvas.drawLine(startX, startY, stopX, stopY, this.paint);
    }

    /**
     * 输入坐标判断是否在范围内
     */
    public boolean clickLine(float x, float y) {
        if(selected){
            double a = getL(x, y, startX, startY);
            double b = getL(x, y, stopX, stopY);
            if(a<40){
                point = HolderPoint.START;
            }else if(b<40){
                point = HolderPoint.STOP;
            }else {
                point = null;
            }
        }
        double s = getS(startX, startY, stopX, stopY, x, y);
        Log.d("XXX",s+"");
        return Math.abs(s)<20;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private double getS(float x1, float y1, float x2, float y2, float x3, float y3){
        return (((y2 - y1) * x3 + (x1 - x2) * y3 + ((x2 * y1) - (x1 * y2)))) / getL(x1,y1,x2,y2);
//        return 1/2*(x2*y3+x1*y2+x3*y1-x3*y2-x2*y1-x1*y3);
    }

    private double getL(float x1, float y1, float x2, float y2){
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x1 - x2, 2));
    }

    public void setStartXY(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void movePoint(float x, float y){
        if(point == HolderPoint.START){
            setStartXY(x,y);
        }else if(point == HolderPoint.STOP){
            setStopXY(x,y);
        }
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setStopXY(float x, float y) {
        this.stopX = x;
        this.stopY = y;
    }

    public enum HolderPoint{
        START,STOP
    }

}
