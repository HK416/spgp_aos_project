package com.hk416.fallingdowntino.object.land;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Vector;

public final class Block extends GameObject {
    public enum Type { Static, Dynamic }

    public static final float STATIC_INTERVAL = 3.2f;
    public static final float HALF_STATIC_INTERVAL = 0.5f * STATIC_INTERVAL;
    public static final float MIN_DYNAMIC_WIDTH = 2.4f;
    public static final float MAX_DYNAMIC_WIDTH = 6.8f;
    public static final float MIN_DYNAMIC_SPEED = 1.0f;
    public static final float MAX_DYNAMIC_SPEED = 5.0f;
    public static final float MIN_WIDTH = Math.min(MIN_DYNAMIC_WIDTH, HALF_STATIC_INTERVAL);

    public static float getRandomDynamicBlockWidth() {
        return MIN_DYNAMIC_WIDTH + (float)Math.random() * (MAX_DYNAMIC_WIDTH - MIN_DYNAMIC_WIDTH);
    }

    public static float getRandomDynamicBlockVelocityX() {
        if ((int)Math.round(Math.random()) == 0) {
            return -1.0f * (MIN_DYNAMIC_SPEED + (float)Math.random() * (MAX_DYNAMIC_SPEED - MIN_DYNAMIC_SPEED));
        } else {
            return MIN_DYNAMIC_SPEED + (float) Math.random() * (MAX_DYNAMIC_SPEED - MIN_DYNAMIC_SPEED);
        }
    }

    private final float minPosX;
    private final float maxPosX;

    private final Player player;
    private float oldDistance;

    private Type type = null;
    private float velocityX = 0.0f;

    public Block(
            float minPosX,
            float maxPosX,
            @NonNull Player player
    ) {
        super();
        this.minPosX = minPosX;
        this.maxPosX = maxPosX;
        this.player = player;
        this.oldDistance = player.getDistance();
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setBlockType(@NonNull Type type) {
        this.type = type;
    }

    @Nullable
    public Type getBlockType() {
        return type;
    }

    public void reset() {
        oldDistance = player.getDistance();
        type = null;
        child = null;
        sibling = null;
        velocityX = 0.0f;
        setPosition(0.0f, 0.0f);
    }

    private float moveHorizon(float elapsedTimeSec, float oldX) {
        float newX = oldX + velocityX * elapsedTimeSec;
        if (velocityX < 0.0f && newX <= minPosX) {
            float diff = minPosX - newX;
            newX = minPosX + diff;
            velocityX *= -1.0f;
        } else if (velocityX >= 0.0f && newX >= maxPosX) {
            float diff = newX - maxPosX;
            newX = maxPosX - diff;
            velocityX *= -1.0f;
        }
        return newX;
    }

    private float moveUp(float oldY) {
        float currDistance = player.getDistance();
        float delta = currDistance - oldDistance;
        oldDistance = currDistance;
        return oldY + delta;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        Vector newPosition = getWorldPosition();
        newPosition.x = moveHorizon(elapsedTimeSec, newPosition.x);
        newPosition.y = moveUp(newPosition.y);
        setPosition(newPosition.x, newPosition.y);
    }
}
