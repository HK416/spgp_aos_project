package com.hk416.framework.object;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.collide.IGameCollider;
import com.hk416.framework.transform.Transform;
import com.hk416.framework.transform.Vector;

public class GameObject implements IGameCollider<GameObject> {
    private static final String TAG = GameObject.class.getSimpleName();

    protected GameObject child = null;
    protected GameObject sibling = null;

    protected Transform transform = new Transform();
    protected Transform worldTransform = new Transform();

    protected BoundingBox aabb = null;

    public GameObject() {
        /* empty */
    }

    public GameObject(float x, float y) {
        setPosition(x, y);
    }

    public void setChild(@Nullable GameObject child) {
        this.child = child;
    }

    @Nullable
    public GameObject getChild() {
        return child;
    }

    public void setSibling(@Nullable GameObject sibling) {
        this.sibling = sibling;
    }

    public GameObject getSibling() {
        return sibling;
    }

    public void setPosition(float x, float y) {
        transform.zAxis.x = x;
        transform.zAxis.y = y;
        updateTransform(null);
    }

    public Vector getPosition() {
        return transform.zAxis;
    }

    public Vector getWorldPosition() {
        return worldTransform.zAxis;
    }

    public Vector getRightVector() {
        return transform.xAxis.normalize();
    }

    public Vector getWorldRightVector() {
        return worldTransform.xAxis.normalize();
    }

    public Vector getUpVector() {
        return transform.yAxis.normalize();
    }

    public Vector getWorldUpVector() {
        return worldTransform.yAxis.normalize();
    }

    public float getRotationAngle() {
        Vector right = getRightVector();
        return new Vector(1.0f, 0.0f, 0.0f).angleBetweenVector(right);
    }

    public float getWorldRotationAngle() {
        Vector right = getWorldRightVector();
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

    public final void updateTransform(@Nullable Transform parent) {
        worldTransform = transform.postMul(parent != null ? parent : new Transform());
        if (aabb != null) {
            aabb.updateTransform(worldTransform);
        }

        if (sibling != null) {
            sibling.updateTransform(parent);
        }
        if (child != null) {
            child.updateTransform(worldTransform);
        }
    }

    public void onTouchEvent(@NonNull MotionEvent e) {
        if (sibling != null) {
            sibling.onTouchEvent(e);
        }

        if (child != null) {
            child.onTouchEvent(e);
        }
    }

    public void onUpdate(float elapsedTimeSec) {
        if (sibling != null) {
            sibling.onUpdate(elapsedTimeSec);
        }

        if (child != null) {
            child.onUpdate(elapsedTimeSec);
        }
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (sibling != null) {
            sibling.onDraw(canvas);
        }

        if (child != null) {
            child.onDraw(canvas);
        }

        if (BuildConfig.DEBUG && aabb != null) {
            aabb.onDraw(canvas);
        }
    }

    @Override
    public boolean intersects(@NonNull GameObject other) {
        if (aabb != null && other.aabb != null && aabb.intersects(other.aabb)) {
            return true;
        }

        if (sibling != null && sibling.intersects(other)) {
            return true;
        }

        return child != null && child.intersects(other);
    }
}
