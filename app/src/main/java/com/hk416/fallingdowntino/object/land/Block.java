package com.hk416.fallingdowntino.object.land;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.object.TileObject;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;

public final class Block extends GameObject {
    public static final float MIN_SPEED = 1.0f;
    public static final float MAX_SPEED = 9.0f;

    private float minRange;
    private float maxRange;
    private float velocityX = 0.0f;
    private float oldDistance = 0.0f;
    private final Player player;

    public Block(
            @NonNull Player player,
            @NonNull Projection projection
    ) {
        super();
        this.player = player;
        this.oldDistance = player.getDistance();
        this.minRange = projection.left;
        this.maxRange = projection.right;
    }

    public void setRange(@NonNull Projection projection) {
        this.minRange = projection.left;
        this.maxRange = projection.right;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void reset() {
        oldDistance = player.getDistance();
        child = null;
        sibling = null;
    }

    private float moveHorizon(float elapsedTimeSec, float oldX) {
        if (child != null && child instanceof SpriteObject) {
            SpriteObject childObject = (TileObject)child;
            float halfWidth = 0.5f * childObject.getSize().x;
            float newX = oldX + velocityX * elapsedTimeSec;
            float leftSide = newX - halfWidth;
            float rightSide = newX + halfWidth;
            if (velocityX < 0.0f && leftSide <= minRange) {
                float diff = minRange - leftSide;
                newX = minRange + halfWidth + diff;
                velocityX *= -1.0f;
            } else if (velocityX >= 0.0f && rightSide >= maxRange) {
                float diff = rightSide - maxRange;
                newX = maxRange - halfWidth - diff;
                velocityX *= -1.0f;
            }
            return newX;
        }
        return oldX;
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
