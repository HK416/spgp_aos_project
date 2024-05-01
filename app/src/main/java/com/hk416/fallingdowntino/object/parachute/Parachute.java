package com.hk416.fallingdowntino.object.parachute;

import android.graphics.Paint;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.object.GameObject;

import java.util.HashMap;
import java.util.Map;

public class Parachute extends GameObject {
    public enum Behavior { LeftDefault, RightDefault, }

    private static final String TAG = Parachute.class.getSimpleName();
    public static final float WIDTH = 2.5f;
    public static final float HEIGHT = 2.5f;
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 1.0f;
    public static final float DEF_DURABILITY = 100.0f;
    public static final float DEF_DURABILITY_PER_SECONDS = 20.0f;

    public static Paint debugColor = null;

    private float maxDurability = DEF_DURABILITY;
    private float currDurability = maxDurability;
    private float durabilityPerSeconds = DEF_DURABILITY_PER_SECONDS;
    private final Map<Behavior, GameObject> behaviors = new HashMap<>();
    private Behavior currBehavior = Behavior.RightDefault;

    public Parachute() {
        super(X_POS, Y_POS);
        createBehaviors();
        createDebugPaint();
    }

    private void createBehaviors() {
        behaviors.put(Behavior.LeftDefault, new LeftDefaultBehavior());
        behaviors.put(Behavior.RightDefault, new RightDefaultBehavior());
        child = behaviors.get(currBehavior);
        updateTransform(null);
    }

    private void createDebugPaint() {
        if (BuildConfig.DEBUG && debugColor == null) {
            debugColor = new Paint();
            debugColor.setStrokeWidth(5.0f);
            debugColor.setStyle(Paint.Style.STROKE);
            debugColor.setARGB(255, 204, 51, 51);
        }
    }

    public void turnBehavior() {
        switch (currBehavior) {
            case LeftDefault:
                child = behaviors.get(Behavior.RightDefault);
                currBehavior = Behavior.RightDefault;
                break;
            case RightDefault:
                child = behaviors.get(Behavior.LeftDefault);
                currBehavior = Behavior.LeftDefault;
                break;
        }
        updateTransform(null);
    }

    public float getDurabilityPercent() {
        return currDurability / maxDurability;
    }

    public float getCurrDurability() {
        return currDurability;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        float nextDurability = currDurability - durabilityPerSeconds * elapsedTimeSec;
        currDurability = Math.max(0.0f, nextDurability);
    }
}
