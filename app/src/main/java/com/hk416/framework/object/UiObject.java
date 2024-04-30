package com.hk416.framework.object;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;

public class UiObject {
    protected final Anchor anchor = new Anchor();
    protected final Margin margin = new Margin();

    protected UiObject child = null;
    protected UiObject sibling = null;

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
        /* empty */
    }

    public void onUpdate(float elapsedTime) {
        /* empty */
    }

    public void onDraw(@NonNull Canvas canvas) {
        /* empty */
    }
}
