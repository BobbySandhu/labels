package com.labels.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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

    private int[] mColors;
    private int touchX;
    private int touchY;
    private int mColorArraySize = 0;
    private int mCurrentColorIndex = 0;
    private int mSelectedRectPosition = -1;

    private Canvas mCanvas;
    private Paint mRectPaint;
    private Matrix mMatrix;
    private ArrayList<Point> mTouchPoints = new ArrayList<>();
    private ArrayList<MarkingRect> mRects = new ArrayList<>();

    private Bitmap mBitmap;
    private MarkingListener mMarkingListener;

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

                // Check if click is inside any existing rectangle
                for (int i = 0; i < mRects.size(); i++) {
                    Rect rect = mRects.get(i).getRect();
                    if (touchX > rect.left && touchX < rect.right && touchY < rect.bottom && touchY > rect.top) {
                        mSelectedRectPosition = i;
                    }
                }

                if (mSelectedRectPosition == -1)
                    mTouchPoints.add(point);

                if (mSelectedRectPosition == -1) {

                    if (mTouchPoints.size() != 0 && mTouchPoints.size() == 2) {

                        int xs = mTouchPoints.get(0).x;
                        int ys = mTouchPoints.get(0).y;
                        int xe = mTouchPoints.get(1).x;
                        int ye = mTouchPoints.get(1).y;

                        /* Creating - Saving rectangle with its color value */
                        Rect rect = new Rect(xs, ys, xe, ye);

                        if (mCurrentColorIndex > mColorArraySize - 1) mCurrentColorIndex = 0;

                        mRects.add(new MarkingRect(rect, mColors[mCurrentColorIndex]));
                        mMarkingListener.onMarking(mRects);
                        mCurrentColorIndex++;

                        mTouchPoints.clear();
                        invalidate();
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:

                int x = (int) getPointerCoords(event)[0];
                int y = (int) getPointerCoords(event)[1];

                final int distY = Math.abs(y - touchY);
                final int distX = Math.abs(x - touchX);

                int deltaX = x - touchX;
                int deltaY = y - touchY;

                if (mSelectedRectPosition != -1) {

                    mCanvas.drawBitmap(mBitmap, mMatrix, mRectPaint);

                    mRects.get(mSelectedRectPosition).getRect().left = mRects.get(mSelectedRectPosition).getRect().left + deltaX;
                    mRects.get(mSelectedRectPosition).getRect().top = mRects.get(mSelectedRectPosition).getRect().top + deltaY;
                    mRects.get(mSelectedRectPosition).getRect().right = mRects.get(mSelectedRectPosition).getRect().right + deltaX;
                    mRects.get(mSelectedRectPosition).getRect().bottom = mRects.get(mSelectedRectPosition).getRect().bottom + deltaY;

                    invalidate();

                    touchX = x;
                    touchY = y;
                }

                break;
            case MotionEvent.ACTION_UP:
                mSelectedRectPosition = -1;
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRectangle();
    }

    private void drawRectangle() {
        for (int i = 0; i < mRects.size(); i++) {
            mRectPaint.setColor(mRects.get(i).getRectColor());
            mCanvas.drawRect(mRects.get(i).getRect(), mRectPaint);
        }

        if (mRects.size() == 0) mSelectedRectPosition = -1;
    }

    @Override
    public void onUndo(int position) {
        if (mBitmap == null) return;

        mCanvas.drawBitmap(mBitmap, mMatrix, mRectPaint);

        if (mRects.size() > 0) {
            mRects.remove(position);
            mMarkingListener.onMarking(mRects);

            if (mCurrentColorIndex != 0) mCurrentColorIndex--;

            invalidate();
        }
        invalidate();
    }

    public void setNewImage(Context context, Bitmap alteredBitmap, Bitmap bmp) {
        mMarkingListener = (MarkingListener) context;
        mCanvas = new Canvas(alteredBitmap);
        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(5);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
        mCanvas.drawBitmap(bmp, mMatrix, mRectPaint);

        mBitmap = bmp;
        setImageBitmap(alteredBitmap);

        mColors = context.getResources().getIntArray(R.array.rainbow);
        mColorArraySize = mColors.length;
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
