package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

    public SpriteObject(
            int bitmapResId,
            float width,
            float height,
            boolean flipX,
            boolean flipY
    ) {
        super();
        size = new Vector(width, height, 0.0f);
        bitmap = getBitmapFromResId(bitmapResId, flipX, flipY);
    }

    public SpriteObject(
            int bitmapResId,
            float x,
            float y,
            float width,
            float height,
            boolean flipX,
            boolean flipY
    ) {
        super(x, y);
        size = new Vector(width, height, 0.0f);
        bitmap = getBitmapFromResId(bitmapResId, flipX, flipY);
    }

    protected final Bitmap getBitmapFromResId(
            int bitmapResId,
            boolean flipX,
            boolean flipY
    ) {
        float scaleX = flipX ? -1.0f : 1.0f;
        float scaleY = flipY ? -1.0f : 1.0f;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        Bitmap bitmap = BitmapPool.getInstance().get(bitmapResId);
        return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                false
        );
    }

    @NonNull
    public Vector getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        if (aabb != null) {
            aabb.setSize(width, height);
        }

        size.x = width;
        size.y = height;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
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
