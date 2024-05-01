package com.hk416.fallingdowntino.object;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.framework.object.GameObject;

public final class Player extends GameObject {
    private static final String TAG = GameObject.class.getSimpleName();
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 12.5f;
    public static final float SPEED = 3.25f;
    public static final float MIN_DOWN_SPEED = 9.0f;
    public static final float MAX_DOWN_SPEED = 18.0f;

    private float downSpeed = MIN_DOWN_SPEED;
    private float distance = 0.0f;

    private final Parachute parachute;
    private final Tino tino;

    public Player() {
        super(X_POS, Y_POS);

        parachute = new Parachute();
        tino = new Tino(this);
        tino.setSibling(parachute);

        setChild(tino);
        updateTransform(null);
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

    public float getDistance() {
        return distance;
    }

    @Override
    public void onTouchEvent(@NonNull MotionEvent e) {
        super.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            turnBehavior();
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        float percent = parachute.getDurabilityPercent();
        downSpeed = MIN_DOWN_SPEED + (MAX_DOWN_SPEED - MIN_DOWN_SPEED) * percent;
        distance += downSpeed * elapsedTimeSec;

        float durability = parachute.getCurrDurability();
        if (durability <= 30.0) {
            downcastBehavior();
        } else {
            upcastBehavior();
        }
    }
}
