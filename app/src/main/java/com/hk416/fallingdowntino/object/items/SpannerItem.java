package com.hk416.fallingdowntino.object.items;

import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.SpriteObject;

public final class SpannerItem extends SpriteObject {
    private static final int BITMAP_RES_ID = R.mipmap.item_spanner;

    public SpannerItem() {
        super(BITMAP_RES_ID, ItemObject.WIDTH, ItemObject.HEIGHT, false, false);
    }
}
