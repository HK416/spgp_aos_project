package com.hk416.fallingdowntino.object.land;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.TileObject;

public class Tile extends TileObject {
    public static final int BITMAP_RES_ID = R.mipmap.land;
    public static final float TILE_WIDTH = 1.5f;
    public static final float TILE_HEIGHT = 1.5f;

    private static Paint debugColor = null;

    public Tile(float x, float y, float width, float height) {
        super(
                BITMAP_RES_ID,
                x,
                y,
                width,
                height,
                TILE_WIDTH,
                TILE_HEIGHT,
                false,
                false
        );
        createDebugColor();

        aabb = new BoundingBox(0.0f, 0.0f, width, height);
        updateTransform(null);
    }

    private void createDebugColor() {
        if (BuildConfig.DEBUG && debugColor == null) {
            debugColor = new Paint();
            debugColor.setColor(Color.rgb(51, 51, 204));
            debugColor.setStrokeWidth(5.0f);
            debugColor.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, debugColor);
        }
    }
}
