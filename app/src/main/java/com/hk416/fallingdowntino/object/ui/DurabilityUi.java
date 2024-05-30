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

public final class DurabilityUi extends UiObject {
    public final class ImageUi extends UiImageObject {
        public ImageUi(
                int bitmapResId,
                @NonNull Anchor anchor,
                @NonNull Margin margin
        ) {
            super(bitmapResId, anchor, margin);
        }
    }

    public final class TextUi extends UiTextObject {
        private int maxDurability = 0;
        private int durability = 0;

        public TextUi(@NonNull Anchor anchor, @NonNull Margin margin) {
            super(null, anchor, margin);
            setColor(Color.BLACK);
        }

        public void setMaxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
        }

        public void setDurability(int durability) {
            this.durability = durability;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onUpdate(float elapsedTime) {
            super.onUpdate(elapsedTime);
            this.text = String.format("%03d/%03d", maxDurability, durability);
        }
    }

    private static final int BITMAP_RES_ID = R.mipmap.item_spanner;
    private final Player player;
    private final TextUi text;

    public DurabilityUi(@NonNull Player player) {
        super();
        this.player = player;
        GameObject img = new ImageUi(
                BITMAP_RES_ID,
                new Anchor(0.02f, 0.02f, 0.06f, 0.107f),
                new Margin(0, 0, 0, 0)
        );
        text = new TextUi(
                new Anchor(0.02f, 0.117f, 0.06f, 0.24f),
                new Margin(0, 0, 0, 0)
        );
        text.setMaxDurability((int)player.getMaxParachuteDurability());
        text.setDurability((int)player.getCurrParachuteDurability());
        img.setSibling(text);
        setChild(img);
    }

    @Override
    public void onUpdate(float elapsedTime) {
        text.setDurability((int)player.getCurrParachuteDurability());
        super.onUpdate(elapsedTime);
    }
}
