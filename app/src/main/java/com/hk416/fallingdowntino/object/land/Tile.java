package com.hk416.fallingdowntino.object.land;

import com.hk416.fallingdowntino.R;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.TileObject;

public class Tile extends TileObject {
    public static final int BITMAP_RES_ID = R.mipmap.land;
    public static final float TILE_WIDTH = 1.5f;
    public static final float TILE_HEIGHT = 1.5f;

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

        aabb = new BoundingBox(0.0f, 0.0f, width, height);
        updateTransform(null);
    }
}
