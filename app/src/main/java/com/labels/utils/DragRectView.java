package com.labels.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DragRectView extends AppCompatImageView implements View.OnTouchListener {

    protected Matrix matrix;
    private Paint mRectPaint;
    private Canvas canvas;
    //
    private int count = 1;
    private int touchX;
    private int touchY;
    private ArrayList<Point> points = new ArrayList<>();
    private HashMap<Integer, ArrayList<Point>> rectMap = new HashMap<>();
    private ArrayList<Rect> rects = new ArrayList();

    public DragRectView(final Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public DragRectView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public DragRectView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = new Point();
            //touchX = (int) event.getX();
            touchX = (int) getPointerCoords(event)[0];
            //touchY = (int) event.getY();
            touchY = (int) getPointerCoords(event)[1];
            point.set(touchX, touchY);
            points.add(point);
        }

        if (points.size() != 0 && points.size() == 2) {
            //rectMap.put(count, points);

            int xs = points.get(0).x;
            int ys = points.get(0).y;
            int xe = points.get(1).x;
            int ye = points.get(1).y;

            rects.add(new Rect(xs, ys, xe, ye));
            count++;
            points.clear();
        }

        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRectangle();
    }

    public void setNewImage(Bitmap alteredBitmap, Bitmap bmp) {
        canvas = new Canvas(alteredBitmap);
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.RED);
        mRectPaint.setStrokeWidth(5);
        mRectPaint.setStyle(Paint.Style.STROKE);
        matrix = new Matrix();
        //canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getHeight(), ), mRectPaint);
        canvas.drawBitmap(bmp, matrix, mRectPaint);

        setImageBitmap(alteredBitmap);
    }

    private void drawRectanglee(int xs, int ys, int xe, int ye, /*Canvas canvas,*/ Paint paint) {
        float left = xs > xe ? xe : xs;
        float top = ys > ye ? ye : ys;
        float right = xs > xe ? xs : xe;
        float bottom = ys > ye ? ys : ye;
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawRectangle() {
        for (int i = 0; i < rects.size(); i++) {
            canvas.drawRect(rects.get(i), mRectPaint);
        }
    }

    final float[] getPointerCoords(MotionEvent e) {
        final int index = e.getActionIndex();
        final float[] coords = new float[]{e.getX(index), e.getY(index)};
        Matrix matrix = new Matrix();
        getImageMatrix().invert(matrix);
        matrix.postTranslate(getScrollX(), getScrollY());
        matrix.mapPoints(coords);

        return coords;
    }
}
