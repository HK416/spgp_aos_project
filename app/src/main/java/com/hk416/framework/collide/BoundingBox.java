package com.hk416.framework.collide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Transform;
import com.hk416.framework.transform.Vector;

public final class BoundingBox implements IGameCollider<BoundingBox> {
    private static Paint debugPaint = null;

    private RectF drawScreenArea = null;
    private Transform worldTransform = new Transform();
    private Transform transform = new Transform();
    private Vector size = new Vector();

    public BoundingBox() {
        setPosition(0.0f, 0.0f);
        setSize(0.0f, 0.0f);
        createDebugPaint();
    }

    public BoundingBox(float x, float y, float sx, float sy) {
        setPosition(x, y);
        setSize(sx, sy);
        createDebugPaint();
    }

    private void createDebugPaint() {
        if (BuildConfig.DEBUG) {
            drawScreenArea = new RectF();
            if (debugPaint == null) {
                debugPaint = new Paint();
                debugPaint.setStyle(Paint.Style.STROKE);
                debugPaint.setColor(Color.rgb(51, 204, 204));
                debugPaint.setStrokeWidth(10.0f);
            }
        }
    }

    public void setPosition(float x, float y) {
        transform.zAxis.x = x;
        transform.zAxis.y = y;
        transform.zAxis.z = 1.0f;
        updateTransform(null);
    }

    public void setSize(float sx, float sy) {
        size.x = sx;
        size.y = sy;
    }

    public Vector getSize() {
        return size;
    }

    public void updateTransform(@Nullable Transform parent) {
        worldTransform = transform.postMul((parent != null) ? parent : new Transform());
    }

    @NonNull
    public Vector getWorldPosition() {
        return worldTransform.zAxis;
    }

    @Override
    public boolean intersects(@NonNull BoundingBox other) {
        Vector center = getWorldPosition();
        Vector otherCenter = other.getWorldPosition();

        float leftSideA = center.x - 0.5f * size.x;
        float rightSideA = center.x + 0.5f * size.x;
        float bottomSideA = center.y - 0.5f * size.y;
        float topSideA = center.y + 0.5f * size.y;

        float leftSideB = otherCenter.x - 0.5f * other.size.x;
        float rightSideB = otherCenter.x + 0.5f * other.size.x;
        float bottomSideB = otherCenter.y - 0.5f * other.size.y;
        float topSideB = otherCenter.y + 0.5f * other.size.y;

        return leftSideA <= rightSideB && rightSideA >= leftSideB
        && bottomSideA <= topSideB && topSideA >= bottomSideB;
    }

    @Override
    public IGameCollider<BoundingBox> getIntersectsCollider(@NonNull BoundingBox other) {
        if (intersects(other)) {
            return this;
        } else {
            return null;
        }
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (BuildConfig.DEBUG) {
            Vector center = getWorldPosition();
            Vector minimum = center.postSub(size.postMul(0.5f));
            Vector maximum = center.postAdd(size.postMul(0.5f));

            PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
            PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

            if (minPos != null && maxPos != null) {
                drawScreenArea.top = maxPos.y;
                drawScreenArea.left = minPos.x;
                drawScreenArea.bottom = minPos.y;
                drawScreenArea.right = maxPos.x;
                canvas.drawRect(drawScreenArea, debugPaint);
            }
        }
    }
}
