package com.hk416.fallingdowntino.object.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.UiImageObject;
import com.hk416.framework.object.UiObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;

public final class LikeUi extends UiObject {
    public class LikeImageUi extends UiImageObject {
        public LikeImageUi(
                int bitmapResId,
                @NonNull Anchor anchor
        ) {
            super(bitmapResId, anchor);
        }
    }

    public class LikeCountUi extends UiTextObject {
        public static final int MAX_COUNT = 999;
        protected int count = 0;

        public LikeCountUi(
                @NonNull Anchor anchor,
                @NonNull UiTextObject.Pivot pivot
        ) {
            super(null, anchor, pivot);
            setColor(Color.BLACK);
        }

        public void setCount(int count) {
            this.count = count;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onUpdate(float elapsedTime) {
            super.onUpdate(elapsedTime);
            int count = Math.min(this.count, MAX_COUNT);
            this.text = String.format("%03d", count);
        }
    }

    private static final int BITMAP_RES_ID = R.mipmap.item_like;
    private static final Anchor imgAnchor = new Anchor(0.02f, 0.76f, 0.06f, 0.865f);
    private static final Anchor textAnchor = new Anchor(0.02f, 0.875f, 0.06f, 0.98f);
    private final LikeCountUi textUi;
    private final Player player;

    public LikeUi(@NonNull Player player) {
        super();
        this.player = player;
        GameObject imgUi = new LikeImageUi(BITMAP_RES_ID, imgAnchor);
        textUi = new LikeCountUi(textAnchor, UiTextObject.Pivot.Horizontal);
        imgUi.setSibling(textUi);
        setChild(imgUi);
    }

    @Override
    public void onUpdate(float elapsedTime) {
        textUi.setCount(player.getLikeCount());
        super.onUpdate(elapsedTime);
    }
}
