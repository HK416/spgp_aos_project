package com.hk416.fallingdowntino.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.SpriteAnimeObject;

public class Tino extends SpriteAnimeObject {
    private static final String TAG = Tino.class.getSimpleName();
    private static final float WIDTH = 2.0f;
    private static final float HEIGHT = 2.0f;
    private static final float X_POS = 0.0f;
    private static final float Y_POS = 12.5f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_default_0, R.mipmap.tino_default_1, R.mipmap.tino_default_2
    };

    private static Paint debugColor = null;

    public Tino() {
        super(BITMAP_RES_IDS, X_POS, Y_POS, WIDTH, HEIGHT, 0.5f);
        createDebugPaint();
    }

    private void createDebugPaint() {
        if (BuildConfig.DEBUG && debugColor == null) {
            debugColor = new Paint();
            debugColor.setStrokeWidth(5.0f);
            debugColor.setStyle(Paint.Style.STROKE);
            debugColor.setARGB(255, 204, 51, 51);
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, debugColor);
        }
    }
}
