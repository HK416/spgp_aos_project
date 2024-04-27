package com.hk416.framework.object;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.transform.Transform;
import com.hk416.framework.transform.Vector;

public class GameObject {
    protected GameObject child = null;
    protected GameObject sibling = null;

    protected Transform transform = new Transform();

    public GameObject() {
        /* empty */
    }

    public GameObject(float x, float y) {
        setPosition(x, y);
    }

    public void setChild(@NonNull GameObject child) {
        this.child = child;
    }

    public void setSibling(@NonNull GameObject sibling) {
        this.sibling = sibling;
    }

    public void setPosition(float x, float y) {
        transform.zAxis.x = x;
        transform.zAxis.y = y;
        updateTransform(null);
    }

    public Vector getPosition() {
        return transform.zAxis;
    }

    public Vector getRightVector() {
        return transform.xAxis.normalize();
    }

    public Vector getUpVector() {
        return transform.yAxis.normalize();
    }

    public float getRotationAngle() {
        Vector right = getRightVector();
        return new Vector(1.0f, 0.0f, 0.0f).angleBetweenVector(right);
    }

    public void translateLocal(float dx, float dy) {
        Vector right = getRightVector().postMul(dx);
        Vector up = getUpVector().postMul(dy);
        translateWorld(right.x + up.x, right.y + up.y);
    }

    public void translateWorld(float dx, float dy) {
        transform.zAxis.x += dx;
        transform.zAxis.y += dy;
        updateTransform(null);
    }

    public void rotate(float angleRadian) {
        transform.postMulAssign(new Transform(angleRadian));
        updateTransform(null);
    }

    private void updateTransform(@Nullable Transform parent) {
        if (parent != null) {
            transform.postMulAssign(parent);
        }

        if (sibling != null) {
            sibling.updateTransform(parent);
        }
        if (child != null) {
            child.updateTransform(transform);
        }
    }

    public void onTouchEvent(@NonNull MotionEvent e) {
        /* empty */
    }

    public void onUpdate(float elapsedTimeSec) {
        /* empty */
    }

    public void onDraw(@NonNull Canvas canvas) {
        /* empty */
    }
}
