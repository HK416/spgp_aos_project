package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.SpriteAnimeObject;

public class DefaultBehavior extends SpriteAnimeObject {
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_default_0, R.mipmap.tino_default_1,
            R.mipmap.tino_default_2, R.mipmap.tino_default_1,
    };

    public DefaultBehavior() {
        super(BITMAP_RES_IDS, Tino.WIDTH, Tino.HEIGHT, 0.8f);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }
}
