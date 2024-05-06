package com.hk416.fallingdowntino.object.land;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Vector;

public class DynamicLand extends GameObject {
    public static final float MIN_WIDTH = 1.0f;
    public static final float MAX_WIDTH = 7.0f;
    public static final float MIN_SPEED = 1.0f;
    public static final float MAX_SPEED = 9.0f;

    protected final float width;
    protected final float height;
    protected final float minRange;
    protected final float maxRange;
    protected float velocityX;

    protected final Player player;
    protected float oldDistance;

    public DynamicLand(
            float x,
            float y,
            float width,
            float height,
            float velocityX,
            float minRange,
            float maxRange,
            @NonNull Player player
    ) {
        super(x, y);
        if (maxRange <= minRange)
            throw new RuntimeException("주어진 최대 범위는 최소 범위보다 커야 합니다.");

        if (maxRange - minRange <= width)
            throw new RuntimeException("주어진 가로 크기는 범위보다 작아야 합니다.");

        this.width = width;
        this.height = height;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.velocityX = velocityX;
        this.player = player;
        this.oldDistance = player.getDistance();
        createChild(width, height);
    }

    private void createChild(float width, float height) {
        GameObject child = new Land(0.0f, 0.0f, width, height);
        setChild(child);
        updateTransform(null);
    }

    private float moveUp(float oldY) {
        float currDistance = player.getDistance();
        float delta = currDistance - oldDistance;
        oldDistance = currDistance;
        return oldY + delta;
    }

    private float moveHorizon(float elapsedTimeSec, float oldX) {
        final float halfWidth = 0.5f * width;
        float newX = oldX;
        newX += velocityX * elapsedTimeSec;
        if (velocityX < 0 && newX - halfWidth <= minRange) {
            float diff = minRange - (newX - halfWidth);
            velocityX *= -1.0f;
            newX = minRange + halfWidth + diff;
        } else if (velocityX >= 0 && newX + halfWidth >= maxRange) {
            float diff = (newX + halfWidth) - maxRange;
            velocityX *= -1.0f;
            newX = maxRange - halfWidth - diff;
        }
        return newX;
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
