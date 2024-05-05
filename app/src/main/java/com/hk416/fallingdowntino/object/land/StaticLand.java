package com.hk416.fallingdowntino.object.land;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Vector;

public class StaticLand extends GameObject {
    public static final float INTERVAL = 4.2f;

    protected final Player player;
    protected float oldDistance;

    public StaticLand(
            float x,
            float y,
            float leftWidth,
            float rightWidth,
            float allHeight,
            @NonNull Player player
    ) {
        super(x, y);
        this.player = player;
        this.oldDistance = player.getDistance();
        createChild(leftWidth, rightWidth, allHeight);
    }

    private void createChild(float leftWidth, float rightWidth, float allHeight) {
        final float HALF_INTERVAL = INTERVAL * 0.5f;
        GameObject left = new Land(-HALF_INTERVAL - 0.5f * leftWidth, 0.0f, leftWidth, allHeight);
        GameObject right = new Land(+HALF_INTERVAL + 0.5f * rightWidth, 0.0f, rightWidth, allHeight);
        left.setSibling(right);
        setChild(left);
        updateTransform(null);
    }

    private void moveUp() {
        Vector newPosition = getWorldPosition();
        float currDistance = player.getDistance();
        float delta = currDistance - oldDistance;
        oldDistance = currDistance;
        newPosition.y += delta;
        setPosition(newPosition.x, newPosition.y);
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        moveUp();
    }
}
