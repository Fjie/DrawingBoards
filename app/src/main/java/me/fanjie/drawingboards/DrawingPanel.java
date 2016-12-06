package me.fanjie.drawingboards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fanji on 2016/12/4.
 */

public class DrawingPanel extends View {
    private Paint paint;
    private Set<Line> lines;
    private Line line;
    private Line selectedLine;
    private Canvas cacheCanvas;
    private Bitmap bitmap;

    public DrawingPanel(Context context) {
        this(context, null);
    }

    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setDither(true);
        lines = new HashSet<>();
    }

    public void setBitmap(@NonNull Bitmap photo) {
        setVisibility(VISIBLE);
        int width = photo.getWidth();
        int height = photo.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化画布绘制的图像到icon上
        cacheCanvas = new Canvas(bitmap);
        // 建立画笔
        Paint photoPaint = new Paint();
        // 获取更清晰的图像采样，防抖动
        photoPaint.setDither(true);
        // 过滤一下，抗剧齿
        photoPaint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        cacheCanvas.drawBitmap(photo, src, dst, photoPaint);
        this.bitmap = bitmap;
    }

    private void drawing() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("XXX", "size = " + lines.size());
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        for (Line l : lines) {
            l.drawing(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                selectedLine = selectedLine(x, y);
                if (selectedLine == null) {
                    line = new Line(paint);
                    line.setStartXY(x, y);
                } else {
                    invalidate();
                }
                Log.d("XXX", selectedLine + "");
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (selectedLine == null) {
                    line.setStopXY(x, y);
                    lines.add(line);

                } else {
                    selectedLine.movePoint(x, y);
                }
                drawing();
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }

        }
        return true;
    }

    private Line selectedLine(float x, float y) {
        for (Line l : lines) {
            if (l.clickLine(x, y)) {
                l.setSelected(true);
                return l;
            } else {
                l.setSelected(false);
            }
        }
        return null;
    }
}
