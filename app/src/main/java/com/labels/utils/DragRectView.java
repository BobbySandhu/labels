package com.labels.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.labels.R;
import com.labels.interfaces.MarkingListener;
import com.labels.interfaces.UndoListener;
import com.labels.model.MarkingRect;

import java.util.ArrayList;

public class DragRectView extends AppCompatImageView implements View.OnTouchListener, UndoListener {

    private int touchX;
    private int touchY;
    private Canvas canvas;
    private Paint mRectPaint;
    private Matrix matrix;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<MarkingRect> rects = new ArrayList<>();

    private Path mPath;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> undonePaths = new ArrayList<>();
    private Bitmap bitmap;
    private int[] rainbow;
    private int colorArraySize = 0;
    private int colorIndex = 0;
    private MarkingListener markingListener;

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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point point = new Point();

                touchX = (int) getPointerCoords(event)[0];
                touchY = (int) getPointerCoords(event)[1];

                point.set(touchX, touchY);
                points.add(point);

                undonePaths.clear(); // Can be used to implement Redo functionality
                mPath.reset();

                if (points.size() != 0 && points.size() == 2) {

                    int xs = points.get(0).x;
                    int ys = points.get(0).y;
                    int xe = points.get(1).x;
                    int ye = points.get(1).y;

                    /* Adding path (different rectangles) */
                    Rect rect = new Rect(xs, ys, xe, ye);
                    RectF rectF = new RectF(rect);
                    mPath.addRect(rectF, Path.Direction.CW);
                    paths.add(mPath);

                    /* Saving rectangle with its color value */
                    if (colorIndex > colorArraySize - 1) colorIndex = 0;
                    rects.add(0, new MarkingRect(rect, rainbow[colorIndex]));
                    markingListener.onMarking(rects, 0);
                    colorIndex++;

                    mPath = new Path();

                    points.clear();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                /*int x = (int) event.getX();
                int y = (int) event.getY();

                int diffX = x - touchX;
                int diffY = y - touchY;

                if (diffX > 5 && diffY > 5 && rects.size() > 0) {
                    rects.get(rectPosition).left = rects.get(rectPosition).left + diffX;
                    rects.get(rectPosition).top = rects.get(rectPosition).top + diffY;
                    rects.get(rectPosition).right = rects.get(rectPosition).right + diffX;
                    rects.get(rectPosition).bottom = rects.get(rectPosition).bottom + diffY;
                }

                invalidate();*/

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRectangle();
    }

    private void drawRectangle() {
        for (int i = 0; i < rects.size(); i++) {
            mRectPaint.setColor(rects.get(i).getRectColor());
            canvas.drawRect(rects.get(i).getRect(), mRectPaint);
        }
    }

    @Override
    public void onUndo(int position) {
        if (bitmap == null) return;

        canvas.drawBitmap(bitmap, matrix, mRectPaint);

        if (rects.size() > 0) {
            rects.remove(position);
            //rects.remove(rects.size() - 1);
            markingListener.onMarking(rects, position);
            if (colorIndex != 0) colorIndex--;
            invalidate();
        }
        invalidate();
    }

    public void setNewImage(Context context, Bitmap alteredBitmap, Bitmap bmp) {
        markingListener = (MarkingListener) context;
        canvas = new Canvas(alteredBitmap);
        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(5);
        mRectPaint.setStyle(Paint.Style.STROKE);
        matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, mRectPaint);
        //canvas.drawBitmap(bmp, 0, 0, mRectPaint);
        mPath = new Path();

        bitmap = bmp;
        setImageBitmap(alteredBitmap);

        rainbow = context.getResources().getIntArray(R.array.rainbow);
        colorArraySize = rainbow.length;
    }

    public DragRectView getInstance() {
        return this;
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
