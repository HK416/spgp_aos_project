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

public class SpriteAnimeObject extends GameObject {
    private static final String TAG = SpriteAnimeObject.class.getSimpleName();

    protected final Vector size;
    protected float animationSpeed;
    protected float animationTimer = 0.0f;

    protected final Bitmap[] bitmaps;
    protected int currBitmapIndex = 0;

    protected final RectF drawScreenArea = new RectF();

    public SpriteAnimeObject(
            int[] bitmapResIds,
            float width,
            float height,
            float animationSpeed,
            boolean flipX,
            boolean flipY
    ) {
        super();
        if (bitmapResIds == null || bitmapResIds.length == 0) {
            throw new RuntimeException("::Constructor >> bitmapResIds는 적어도 1개 이상 있어야 합니다.");
        }

        this.size = new Vector(width, height, 0.0f);
        this.animationSpeed = animationSpeed;
        this.bitmaps = new Bitmap[bitmapResIds.length];
        setBitmaps(bitmapResIds, flipX, flipY);
    }

    public SpriteAnimeObject(
            int[] bitmapResIds,
            float x,
            float y,
            float width,
            float height,
            float animationSpeed,
            boolean flipX,
            boolean flipY
    ) {
        super(x, y);
        if (bitmapResIds == null || bitmapResIds.length == 0) {
            throw new RuntimeException("::Constructor >> bitmapResIds는 적어도 1개 이상 있어야 합니다.");
        }

        this.size = new Vector(width, height, 0.0f);
        this.animationSpeed = animationSpeed;
        this.bitmaps = new Bitmap[bitmapResIds.length];
        setBitmaps(bitmapResIds, flipX, flipY);
    }

    private void setBitmaps(int[] bitmapResIds, boolean flipX, boolean flipY) {
        float scaleX = flipX ? -1.0f : 1.0f;
        float scaleY = flipY ? -1.0f : 1.0f;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        for (int i = 0; i < bitmapResIds.length; i++) {
            Bitmap bitmap = BitmapPool.getInstance().get(bitmapResIds[i]);
            this.bitmaps[i] = Bitmap.createBitmap(
                    bitmap,
                    0, 0,
                    bitmap.getWidth(), bitmap.getHeight(),
                    matrix,
                    false
            );
        }
    }

    @NonNull
    public Vector getSize() {
        return size;
    }

    public void setSize(float width, float height) {
        size.x = width;
        size.y = height;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        final float EPSILON = 1.401298E-45f;
        if (Math.abs(animationSpeed) <= EPSILON) {
            return; // animationSpeed가 0인 경우 onUpdate함수를 실행하지 않는다. (0 나누기 오류 방지)
        }

        boolean reverse = animationSpeed <= 0.0f;
        float animationSpeedAbs = Math.abs(animationSpeed);
        float framePerSeconds = (float)bitmaps.length / animationSpeedAbs;
        animationTimer = (animationTimer + elapsedTimeSec) % animationSpeedAbs;
        int currFrame = (int)(animationTimer * framePerSeconds);
        if (!reverse) {
            currBitmapIndex = currFrame;
        } else {
            currBitmapIndex = (bitmaps.length - 1) - currFrame;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Vector minimum = getPosition().postSub(getSize().postMul(0.5f));
        Vector maximum = getPosition().postAdd(getSize().postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null) {
            drawScreenArea.top = maxPos.y;
            drawScreenArea.left = minPos.x;
            drawScreenArea.bottom = minPos.y;
            drawScreenArea.right = maxPos.x;
            canvas.drawBitmap(bitmaps[currBitmapIndex], null, drawScreenArea, null);
        }
    }
}
