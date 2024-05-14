package com.hk416.fallingdowntino.object;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.GameObject;

public final class Player extends GameObject {
    private static final String TAG = GameObject.class.getSimpleName();
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 12.5f;
    public static final float SPEED = 3.25f;
    public static final float MIN_DOWN_SPEED = 9.0f;
    public static final float MAX_DOWN_SPEED = 12.0f;
    public static final float MAX_DIVE_DOWN_SPEED = 36.0f;

    private float currDownSpeed = 0.0f;
    private float distance = 0.0f;
    private float behaviorTimer = 0.0f;

    private final Parachute parachute;
    private final Tino tino;

    public Player() {
        super(X_POS, Y_POS);

        parachute = new Parachute();
        tino = new Tino(this);
        tino.setSibling(parachute);
        setChild(tino);

        aabb = new BoundingBox(Tino.BOX_X, Tino.BOX_Y, Tino.BOX_WIDTH, Tino.BOX_HEIGHT);
        updateTransform(null);
    }

    public void setBehaviors(
            @Nullable Tino.Behavior tinoBehavior,
            @Nullable Parachute.Behavior parachuteBehavior
    ) {
        if (tinoBehavior != null) {
            tino.setBehavior(tinoBehavior);
        }

        if (parachuteBehavior != null) {
            parachute.setBehavior(parachuteBehavior);
        }
    }

    public void upcastBehavior() {
        tino.upcastBehavior();
        parachute.upcastBehavior();
        updateTransform(null);
    }

    public void downcastBehavior() {
        tino.downcastBehavior();
        parachute.downcastBehavior();
        updateTransform(null);
    }

    public void turnBehavior() {
        tino.turnBehavior();
        parachute.turnBehavior();
        updateTransform(null);
    }

    public float addParachuteDurability(float durability) {
        return parachute.addDurability(durability);
    }

    public float getCurrParachuteDurability() {
        return parachute.getCurrDurability();
    }

    public float getMaxParachuteDurability() {
        return parachute.getMaxDurability();
    }

    public float getCurrDownSpeed() {
        return currDownSpeed;
    }

    public void setCurrDownSpeed(float downSpeed) {
        currDownSpeed = downSpeed;
    }

    public void setBehaviorTimer(float behaviorTimer) {
        this.behaviorTimer = behaviorTimer;
    }

    public float getPositionX() {
        return getPosition().x;
    }

    public float getDistance() {
        return distance;
    }

    public float getBehaviorTimer() {
        return behaviorTimer;
    }

    public void updatePlayer(float newX, float newDistance) {
        setPosition(newX, transform.zAxis.y);
        distance = newDistance;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        behaviorTimer = Math.max(behaviorTimer - elapsedTimeSec, 0.0f);
        super.onUpdate(elapsedTimeSec);
    }

    @Override
    public void onCollide(@NonNull GameObject object) {
        Log.d(TAG, "::onCollide >> object(" + object + ")와 플레이어가 충돌함.");
        super.onCollide(object);
    }
}
