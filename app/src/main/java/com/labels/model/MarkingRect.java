package com.labels.model;

import android.graphics.Rect;

public class MarkingRect {

    private Rect rect;
    private int rectColor;

    public MarkingRect(Rect rect, int rectColor) {
        this.rect = rect;
        this.rectColor = rectColor;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getRectColor() {
        return rectColor;
    }

    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
    }
}
