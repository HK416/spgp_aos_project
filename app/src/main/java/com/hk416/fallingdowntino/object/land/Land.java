package com.hk416.fallingdowntino.object.land;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.TileObject;
import com.hk416.framework.transform.Vector;

public class Land extends TileObject {
    public static final int BITMAP_RES_ID = R.mipmap.land;
    public static final float TILE_WIDTH = 2.0f;
    public static final float TILE_HEIGHT = 2.0f;

    protected final Player player;
    protected float oldDistance;

    public Land(
            float x,
            float y,
            float width,
            float height,
            @NonNull Player player
            ) {
        super(
                BITMAP_RES_ID,
                x,
                y,
                width,
                height,
                TILE_WIDTH,
                TILE_HEIGHT,
                false,
                false,
                Align.TopMid
        );
        this.player = player;
        this.oldDistance = player.getDistance();
    }

    private void moveUp(float elapsedTimeSec) {
        Vector newPosition = getWorldPosition();
        float currDistance = player.getDistance();
        float delta = currDistance - oldDistance;
        oldDistance = currDistance;
        newPosition.y = newPosition.y + delta;
        setPosition(newPosition.x, newPosition.y);
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        moveUp(elapsedTimeSec);
    }
}
