package com.hk416.fallingdowntino.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Vector;

public class Tino extends GameObject {
    private static final String TAG = Tino.class.getSimpleName();
    private static final float WIDTH = 2.0f;
    private static final float HEIGHT = 2.0f;
    private static final float X_POS = 0.0f;
    private static final float Y_POS = 12.5f;

    private static Paint debugColor = null;

    private Bitmap bitmap = null;
    private RectF dstRect = new RectF();

    public Tino() {
        super(X_POS, Y_POS, WIDTH, HEIGHT);
        createDebugPaint();
        createBitmap();
    }

    private void createDebugPaint() {
        if (BuildConfig.DEBUG && debugColor == null) {
            debugColor = new Paint();
            debugColor.setStrokeWidth(5.0f);
            debugColor.setStyle(Paint.Style.STROKE);
            debugColor.setARGB(255, 204, 51, 51);
        }
    }

    private void createBitmap() {
        if (bitmap == null) {
            bitmap = BitmapPool.getInstance().get(R.mipmap.tino_default_0);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Vector position = getPosition();
        Vector size = getSize();
        Vector max = position.postAdd(size.postMul(0.5f));
        Vector min = position.postSub(size.postMul(0.5f));

        DrawPipeline pipeline = DrawPipeline.getInstance();

        PointF topRight = pipeline.toScreenCoord(max);
        PointF bottomLeft = pipeline.toScreenCoord(min);

        if (topRight != null && bottomLeft != null) {
            dstRect.top = topRight.y;
            dstRect.left = bottomLeft.x;
            dstRect.bottom = bottomLeft.y;
            dstRect.right = topRight.x;
            canvas.drawBitmap(bitmap, null, dstRect, null);

            if (BuildConfig.DEBUG) {
                canvas.drawRect(
                        bottomLeft.x,
                        topRight.y,
                        topRight.x,
                        bottomLeft.y,
                        debugColor
                );
            }
        }
    }
}
