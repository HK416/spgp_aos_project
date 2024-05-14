package com.hk416.fallingdowntino.object.items;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.land.Block;
import com.hk416.framework.object.GameObject;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Random;

public final class ItemPool extends GameObject {
    private static final String TAG = ItemPool.class.getSimpleName();

    public static final int CAPACITY = 16;
    public static final float SPAWN_POS = -32.0f;
    public static final float RETAIN_POS = 32.0f;
    public static final float SPAWN_INTERVAL = 24.0f;

    private final Random random;
    private final float minPosX;
    private final float maxPosX;

    private final ArrayDeque<ItemObject> activeItems;
    private final ArrayDeque<ItemObject> inactiveItems;
    private final Player player;
    private float prevSpawnDistance;

    public ItemPool(
            float minPosX,
            float maxPosX,
            float initSpawnDistance,
            @NonNull Player player
    ) {
        checkParameters(minPosX, maxPosX, initSpawnDistance, player);

        this.random = new Random();
        this.minPosX = minPosX;
        this.maxPosX = maxPosX;

        this.player = player;
        this.prevSpawnDistance = initSpawnDistance;

        activeItems = new ArrayDeque<>(CAPACITY);
        inactiveItems = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveItems.add(new ItemObject(player));
        }
    }

    private void checkParameters(
            float minPosX,
            float maxPosX,
            float _initSpawnDistance,
            Player player
    ) {
        if (maxPosX <= minPosX) {
            throw new InvalidParameterException("주어진 maxSpawnX는 minSpawnX 보다 커야 합니다!");
        }

        if (maxPosX - minPosX < Block.MIN_WIDTH) {
            throw new InvalidParameterException("주어진 범위의 길이는 `Block.MIN_WIDTH`보다 커야 합니다!");
        }

        if (player == null) {
            throw new InvalidParameterException("주어진 Player 객체는 null이 될 수 없습니다!");
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
        int index = (int)Math.round(random.nextGaussian() * 0.75 + 1.25);
        return types[Math.max(Math.min(index, types.length - 1), 0)];
    }

    private float getRandomPositionX() {
        final float halfItemWidth = 0.5f * ItemObject.WIDTH;
        float begX = minPosX + halfItemWidth;
        float endX = maxPosX - halfItemWidth;
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
