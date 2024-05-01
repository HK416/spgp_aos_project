package com.hk416.framework.object;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;

public class UiObject extends GameObject {
    protected final Anchor anchor;
    protected final Margin margin;

    public UiObject() {
        anchor = new Anchor();
        margin = new Margin();
    }

    public UiObject(@NonNull Anchor anchor) {
        this.anchor = anchor;
        margin = new Margin();
    }

    public UiObject(@NonNull Margin margin) {
        anchor = new Anchor();
        this.margin = margin;
    }

    public UiObject(@NonNull Anchor anchor, @NonNull Margin margin) {
        this.anchor = anchor;
        this.margin = margin;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(float top, float left, float bottom, float right) {
        anchor.top = top;
        anchor.left = left;
        anchor.bottom = bottom;
        anchor.right = right;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(int top, int left, int bottom, int right) {
        margin.top = top;
        margin.left = left;
        margin.bottom = bottom;
        margin.right = right;
    }

    public void onTouchEvent(@NonNull MotionEvent e) {
        super.onTouchEvent(e);
    }

    public void onUpdate(float elapsedTime) {
        super.onUpdate(elapsedTime);
    }

    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
    }
}
