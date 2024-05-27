package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public final class SpriteClipAnimeObject extends GameObject {
    private static final class AnimationClip {
        private final float timePoint;
        private final Vector size;
        private final int bitmapResId;

        public AnimationClip(
                float timePoint,
                float width,
                float height,
                int bitmapResId
        ) {
            this.timePoint = timePoint;
            this.size = new Vector(width, height, 0.0f);
            this.bitmapResId = bitmapResId;
        }
    }

    public static final class Builder {
        private float x = 0.0f;
        private float y = 0.0f;
        private boolean repeat = false;
        private float currTime = 0.0f;
        private final ArrayList<AnimationClip> clips = new ArrayList<>();
        private final HashMap<Integer, Bitmap> bitmaps = new HashMap<>();

        public Builder() { /* empty */ }

        public Builder setX(float x) {
            this.x = x;
            return this;
        }

        public Builder setY(float y) {
            this.y = y;
            return this;
        }

        public Builder setRepeat(boolean repeat) {
            this.repeat = repeat;
            return this;
        }

        public Builder addClips(
                float duration,
                float width,
                float height,
                int bitmapResId,
                boolean flipX,
                boolean flipY
        ) {
            currTime += duration;
            clips.add(new AnimationClip(currTime, width, height, bitmapResId));
            addBitmap(bitmapResId, flipX, flipY);
            return this;
        }

        private void addBitmap(int bitmapResId, boolean flipX, boolean flipY) {
            float scaleX = flipX ? -1.0f : 1.0f;
            float scaleY = flipY ? -1.0f : 1.0f;
            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY);
            Bitmap bitmap = BitmapPool.getInstance().get(bitmapResId);
            bitmaps.put(bitmapResId, Bitmap.createBitmap(
                    bitmap,
                    0, 0,
                    bitmap.getWidth(), bitmap.getHeight(),
                    matrix, false
            ));
        }

        public SpriteClipAnimeObject build() {
            return new SpriteClipAnimeObject(this);
        }
    }

    private final boolean repeat;
    private final float duration;

    private final ArrayList<AnimationClip> clips;
    private final HashMap<Integer, Bitmap> bitmaps;

    private int currAnimationClip = 0;
    private float animationTimer = 0.0f;
    private final RectF dstRect = new RectF();

    private SpriteClipAnimeObject(@NonNull Builder builder) {
        super(builder.x, builder.y);
        this.repeat = builder.repeat;
        this.duration = builder.currTime;

        this.clips = builder.clips;
        this.bitmaps = builder.bitmaps;
    }

    public float getDuration() {
        return duration;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        if (clips.isEmpty() || duration <= animationTimer) {
            return;
        }

        animationTimer += elapsedTimeSec;
        Log.d(SpriteClipAnimeObject.class.getSimpleName(), "timer:" + animationTimer
                + ", clip:" + currAnimationClip
                + ", duration:" + duration
        );

        if (duration <= animationTimer) {
            if (repeat) {
                animationTimer -= duration;
                currAnimationClip = 0;
            } else {
                currAnimationClip = clips.size() - 1;
                return;
            }
        }

        while (currAnimationClip < clips.size()) {
            if (clips.get(currAnimationClip).timePoint < animationTimer) {
                currAnimationClip += 1;
            } else {
                break;
            }
        }

        Log.d(SpriteClipAnimeObject.class.getSimpleName(), "timer:" + animationTimer
                + "clip:" + currAnimationClip);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (clips.isEmpty() || clips.size() <= currAnimationClip) {
            return;
        }

        AnimationClip animationClip = clips.get(currAnimationClip);
        Bitmap bitmap = bitmaps.get(animationClip.bitmapResId);
        Vector minimum = getWorldPosition().postSub(animationClip.size.postMul(0.5f));
        Vector maximum = getWorldPosition().postAdd(animationClip.size.postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null && bitmap != null) {
            dstRect.top = maxPos.y;
            dstRect.left = minPos.x;
            dstRect.bottom = minPos.y;
            dstRect.right = maxPos.x;
            canvas.drawBitmap(bitmap, null, dstRect, null);
        }
    }
}
