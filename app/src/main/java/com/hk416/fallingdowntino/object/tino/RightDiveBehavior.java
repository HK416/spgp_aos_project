package com.hk416.fallingdowntino.object.tino;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.SpriteObject;

public class RightDiveBehavior extends SpriteObject {
    private static final int BITMAP_RES_ID = R.mipmap.tino_dive;

    private final Player player;

    public RightDiveBehavior(@NonNull Player player) {
        super(BITMAP_RES_ID, Tino.WIDTH, Tino.HEIGHT, false, false);
        this.player = player;
    }
}
