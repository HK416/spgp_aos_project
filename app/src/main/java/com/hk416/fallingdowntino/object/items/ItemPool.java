package com.hk416.fallingdowntino.object.items;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;

public final class ItemPool extends GameObject {
    private static final String TAG = ItemPool.class.getSimpleName();

    public static final int CAPACITY = 16;
    public static final float SPAWN_POS = -32.0f;
    public static final float RETAIN_POS = 32.0f;
    public static final float SPAWN_INTERVAL = 24.0f;

    private final float minSpawnX;
    private final float maxSpawnX;

    private final ArrayDeque<ItemObject> activeItems;
    private final ArrayDeque<ItemObject> inactiveItems;
    private final Player player;
    private float prevSpawnDistance;

    public ItemPool(
            float minSpawnX,
            float maxSpawnX,
            float startSpawnDistance,
            @NonNull Player player
    ) {
        if (maxSpawnX <= minSpawnX) {
            throw new InvalidParameterException("주어진 maxSpawnX는 minSpawnX 보다 커야 합니다!");
        }

        if (maxSpawnX - minSpawnX < ItemObject.WIDTH) {
            throw new InvalidParameterException("주어진 범위의 길이는 아이템 오브젝트의 길이보다 커야 합니다!");
        }

        this.minSpawnX = minSpawnX;
        this.maxSpawnX = maxSpawnX;

        this.player = player;
        this.prevSpawnDistance = startSpawnDistance;
        activeItems = new ArrayDeque<>(CAPACITY);
        inactiveItems = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveItems.add(new ItemObject(player));
        }
    }

    @NonNull
    private ItemObject getItemObject() {
        if (inactiveItems.isEmpty()) {
            return new ItemObject(player);
        } else {
            ItemObject itemObject = inactiveItems.pop();
            itemObject.reset();
            return itemObject;
        }
    }

    private ItemObject.Type getRandomItemType() {
        ItemObject.Type[] types = ItemObject.Type.values();
        return types[(int)Math.floor(Math.random() * types.length)];
    }

    private float getRandomPositionX() {
        final float halfItemWidth = 0.5f * ItemObject.WIDTH;
        float begX = minSpawnX + halfItemWidth;
        float endX = maxSpawnX - halfItemWidth;
        return begX + (float)Math.random() * (endX - begX);
    }

    private void onSpawnRandomItem() {
        float currDistance = player.getDistance();
        if (prevSpawnDistance + SPAWN_INTERVAL > currDistance) {
            return;
        }

        float offsetY = currDistance - (prevSpawnDistance + SPAWN_INTERVAL);
        prevSpawnDistance += SPAWN_INTERVAL;

        ItemObject itemObject = getItemObject();
        ItemObject.Type type = getRandomItemType();
        float posX = getRandomPositionX();
        float posY = SPAWN_POS + offsetY;

        itemObject.setItemType(type);
        itemObject.setPosition(posX, posY);
        activeItems.add(itemObject);

        Log.d(TAG, "::onSpawnRandomItem >> 아이템 추가 (type:" + type + ")");
    }

    private void onRetainItems() {
        while (!activeItems.isEmpty()) {
            ItemObject firstItemObject = activeItems.peek();
            if (firstItemObject == null) {
                break;
            }

            if (firstItemObject.getWorldPosition().y >= RETAIN_POS) {
                Log.d(TAG, "::onRetainItems >> 아이템 제거");
                firstItemObject = activeItems.pop();
                firstItemObject.setChild(null);
                inactiveItems.add(firstItemObject);
            } else {
                break;
            }
        }
    }

    private void onUpdateActiveItems(float elapsedTimeSec) {
        for (ItemObject object : activeItems) {
            object.onUpdate(elapsedTimeSec);
        }
    }

    private void onCheckCollision() {
        ArrayDeque<ItemObject> nextActiveItems = new ArrayDeque<>(activeItems.size());
        while (!activeItems.isEmpty()) {
            ItemObject itemObject = activeItems.pop();
            if (itemObject == null) {
                break;
            }

            if (itemObject.intersects(player)) {
                Log.d(TAG, "::onCheckCollision >> 플레이어와 충돌!");
                player.onCollide(itemObject);
                itemObject.reset();
                inactiveItems.add(itemObject);
            } else {
                nextActiveItems.add(itemObject);
            }
        }
        activeItems.addAll(nextActiveItems);
    }

    public int getNumActiveItems() {
        return activeItems.size();
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        onUpdateActiveItems(elapsedTimeSec);
        onCheckCollision();
        onSpawnRandomItem();
        onRetainItems();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        for (ItemObject itemObject : activeItems) {
            itemObject.onDraw(canvas);
        }
    }
}
