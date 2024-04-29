package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Vector;

public class SpriteObject extends GameObject {
    protected final Vector size;
    protected final Bitmap bitmap;
    protected final RectF drawScreenArea = new RectF();

    public SpriteObject(int bitmapResId, float width, float height) {
        super();
        size = new Vector(width, height, 0.0f);
        bitmap = BitmapPool.getInstance().get(bitmapResId);
    }

    public SpriteObject(int bitmapResId, float x, float y, float width, float height) {
        super(x, y);
        size = new Vector(width, height, 0.0f);
        bitmap = BitmapPool.getInstance().get(bitmapResId);
    }

    @NonNull
    public Vector getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size.x = width;
        size.y = height;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Vector minimum = getWorldPosition().postSub(getSize().postMul(0.5f));
        Vector maximum = getWorldPosition().postAdd(getSize().postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null) {
            drawScreenArea.top = maxPos.y;
            drawScreenArea.left = minPos.x;
            drawScreenArea.bottom = minPos.y;
            drawScreenArea.right = maxPos.x;
            canvas.drawBitmap(bitmap, null, drawScreenArea, null);
        }
    }
}
