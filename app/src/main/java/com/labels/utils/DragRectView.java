package com.labels.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.labels.ui.activity.EditImageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DragRectView extends AppCompatImageView implements View.OnTouchListener, EditImageActivity.UndoListener {

    private int touchX;
    private int touchY;
    private Canvas canvas;
    private Paint mRectPaint;
    private Matrix matrix;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Rect> rects = new ArrayList();

    private Path mPath;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> undonePaths = new ArrayList<>();

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
            touchX = (int) getPointerCoords(event)[0];
            touchY = (int) getPointerCoords(event)[1];
            point.set(touchX, touchY);
            points.add(point);
        }

        if (points.size() != 0 && points.size() == 2) {

            int xs = points.get(0).x;
            int ys = points.get(0).y;
            int xe = points.get(1).x;
            int ye = points.get(1).y;

            rects.add(new Rect(xs, ys, xe, ye));
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

    @Override
    public void onUndo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void setNewImage(Bitmap alteredBitmap, Bitmap bmp) {
        canvas = new Canvas(alteredBitmap);
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.RED);
        mRectPaint.setStrokeWidth(5);
        mRectPaint.setStyle(Paint.Style.STROKE);
        matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, mRectPaint);
        mPath = new Path();

        setImageBitmap(alteredBitmap);
    }

    private void drawRectangle() {
//        for (int i = 0; i < rects.size(); i++) {
//            canvas.drawRect(rects.get(i), mRectPaint);
//        }

        /* Drawing only recently added rectangle */
        if (!rects.isEmpty())
            canvas.drawRect(rects.get(rects.size() - 1), mRectPaint);
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

    public DragRectView getInstance() {
        return this;
    }
}
