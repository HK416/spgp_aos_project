package com.hk416.fallingdowntino.object.parachute;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.SpriteAnimeObject;

public class LeftScratchedBehavior extends SpriteAnimeObject {
    private static final float ANIMATION_SPEED = 0.6f;
    private static final float OFFSET_X = -0.05f;
    private static final float OFFSET_Y = 0.0f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.parachute_scratched_0, R.mipmap.parachute_scratched_1,
            R.mipmap.parachute_scratched_2, R.mipmap.parachute_scratched_1,
    };

    public LeftScratchedBehavior() {
        super(
                BITMAP_RES_IDS,
                OFFSET_X,
                OFFSET_Y,
                Parachute.WIDTH,
                Parachute.HEIGHT,
                ANIMATION_SPEED,
                false,
                false
        );
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Parachute.debugColor);
        }
    }
}
