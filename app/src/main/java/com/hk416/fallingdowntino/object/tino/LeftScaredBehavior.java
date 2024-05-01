package com.hk416.fallingdowntino.object.tino;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.SpriteAnimeObject;

public class LeftScaredBehavior extends SpriteAnimeObject {
    private static final float ANIMATION_SPEED = 0.6f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_scared_0, R.mipmap.tino_scared_1,
            R.mipmap.tino_scared_2, R.mipmap.tino_scared_1,
    };

    private final Player player;

    public LeftScaredBehavior(@NonNull Player player) {
        super(
                BITMAP_RES_IDS,
                Tino.WIDTH,
                Tino.HEIGHT,
                ANIMATION_SPEED,
                false,
                false
        );
        this.player = player;
    }
}
