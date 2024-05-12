package com.hk416.fallingdowntino.object.items;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;

public final class ItemObject extends GameObject {
    public static enum Type { Energy, Like, Spanner }

    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.0f;

    private Type type = null;
    private float oldDistance;
    private final Player player;

    public ItemObject(@NonNull Player player) {
        super();
        this.oldDistance = player.getDistance();
        this.player = player;
    }

    public void reset() {
        oldDistance = player.getDistance();
    }
}
