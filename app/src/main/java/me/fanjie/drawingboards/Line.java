package me.fanjie.drawingboards;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Line implements DrawingView {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    //    标注
    private int lengthLabel;

    private Paint paint;
    private boolean selected;
    private HolderPoint point;
    private OnClickCallback callback;

    public Line(Paint paint) {
        this.paint = paint;
    }

    public void setCallback(OnClickCallback callback) {
        this.callback = callback;
    }

    @Override
    public void drawing(Canvas canvas) {
        Log.d("XXX", "startX = " + startX + ",startY = " + startY + ",stopX = " + stopX
                + ",stopY = " + stopY);
        Log.d("XXX", "lengthLabel = " + lengthLabel
                + ",length = " + getLength());
        if (selected) {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(startX, startY, 20, paint);
            canvas.drawCircle(stopX, stopY, 20, paint);
        } else {
            paint.setColor(Color.RED);
        }
        float px = startX + (stopX-startX)/2;
        float py = startY + (stopY-startY)/2;
        paint.setTextSize(200);
        canvas.drawText(lengthLabel+"",px,py,paint);
        canvas.drawLine(startX, startY, stopX, stopY, this.paint);
    }

    /**
     * 输入坐标判断是否在范围内
     */
    public boolean clickLine(float x, float y) {
        double s = getS(startX, startY, stopX, stopY, x, y);
        boolean select = Math.abs(s) < 20;
        if (selected) {
            double a = getL(x, y, startX, startY);
            double b = getL(x, y, stopX, stopY);
            if (a < 40) {
                point = HolderPoint.START;
            } else if (b < 40) {
                point = HolderPoint.STOP;
            } else if (select) {
                point = HolderPoint.LINE;
            } else {
                point = null;
            }
            callback.onLineClick(point);
        }
        Log.d("XXX", s + "");
        return select;
    }

    public void setLengthLabel(int lengthLabel, Float weight) {
        this.lengthLabel = lengthLabel;
        if (weight != null) {
//            fun(weight);
            fun2(weight);
        }
    }

    private void fun2(float weight) {

        double v = Math.atan((stopX - startX) / (stopY - startY));
        v = Math.abs(v);//取消掉斜率代表的方向，在下方手动判断
        Log.d("XXX", "v = " + v);
        float ac = lengthLabel / weight;
        Log.d("XXX", "ac = " + ac);
        float bc = (float) (ac * Math.sin(v));
        Log.d("XXX", "bc = " + bc);
        float ab = (float) (ac * Math.cos(v));
        Log.d("XXX", "ab = " + ab);
//        手动判断方向
        stopX = stopX > startX ? startX + bc : startX - bc;
        stopY = stopY > startY ? startY + ab : startY - ab;

    }

    private void fun(float weight) {
        // 先求线段比，线段AC除以线段BC为线段比
        float ac = lengthLabel / weight;
        float ab = getLength();
        float s = ac / (ac - ab);
//            再求延长线坐标，用定比分点坐标公式
        float dx = (startX + s * stopX) / (1 + s);
        float dy = (startY + s * stopY) / (1 + s);
        stopX = dx;
        stopY = dy;
    }


    public float getLength() {
        return (float) getL(startX, startY, stopX, stopY);
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private double getS(float x1, float y1, float x2, float y2, float x3, float y3) {
        return (((y2 - y1) * x3 + (x1 - x2) * y3 + ((x2 * y1) - (x1 * y2)))) / getL(x1, y1, x2, y2);
//        return 1/2*(x2*y3+x1*y2+x3*y1-x3*y2-x2*y1-x1*y3);
    }

    private double getL(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x1 - x2, 2));
    }

    public void setStartXY(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void movePoint(float x, float y) {
        if (point == HolderPoint.START) {
            setStartXY(x, y);
        } else if (point == HolderPoint.STOP) {
            setStopXY(x, y);
        }
    }

    public void setStopXY(float x, float y) {
        this.stopX = x;
        this.stopY = y;
    }

    public enum HolderPoint {
        START, STOP, LINE
    }

    interface OnClickCallback {
        void onLineClick(HolderPoint point);
    }

}
