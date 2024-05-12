package com.hk416.fallingdowntino.object.items;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;

import java.util.ArrayDeque;

public final class ItemPool extends GameObject {
    private static final String TAG = ItemPool.class.getSimpleName();

    public static final int CAPACITY = 16;
    public static final float SPAWN_POS = -32.0f;
    public static final float RETAIN_POS = 32.0f;
    public static final float SPAWN_INTERVAL = 24.0f;

    private final ArrayDeque<ItemObject> activeItems;
    private final ArrayDeque<ItemObject> inactiveItems;
    private final Player player;
    private float prevSpawnDistance;

    public ItemPool(@NonNull Player player) {
        this.player = player;
        this.prevSpawnDistance = -SPAWN_INTERVAL;
        activeItems = new ArrayDeque<>(CAPACITY);
        inactiveItems = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveItems.add(new ItemObject(player));
        }
    }
}
