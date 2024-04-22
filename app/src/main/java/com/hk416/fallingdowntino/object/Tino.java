package com.hk416.fallingdowntino.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.framework.object.GameObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Vector;

public class Tino extends GameObject {
    private static final String TAG = Tino.class.getSimpleName();
    private static final float WIDTH = 1.6f;
    private static final float HEIGHT = 2.0f;
    private static final float X_POS = 0.0f;
    private static final float Y_POS = 11.2f;

    private final Paint debugColor;

    public Tino() {
        super(X_POS, Y_POS, WIDTH, HEIGHT);
        debugColor = new Paint();
        debugColor.setStrokeWidth(5.0f);
        debugColor.setStyle(Paint.Style.STROKE);
        debugColor.setARGB(255, 204, 51, 51);
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

        Log.d(TAG, "pos:" + topRight + ", size:" + bottomLeft);
        if (topRight != null && bottomLeft != null) {
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
