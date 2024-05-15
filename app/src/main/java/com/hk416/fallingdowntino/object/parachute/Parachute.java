package com.hk416.fallingdowntino.object.parachute;

import android.graphics.Paint;
import android.util.Log;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteAnimeObject;

import java.util.HashMap;
import java.util.Map;

public class Parachute extends GameObject {
    public enum Behavior { LeftDefault, RightDefault, LeftScratched, RightScratched }

    private static final String TAG = Parachute.class.getSimpleName();
    public static final float WIDTH = 2.5f;
    public static final float HEIGHT = 2.5f;
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 1.0f;
    public static final float DEF_DURABILITY = 100.0f;
    public static final float DEF_DURABILITY_PER_SECONDS = 6.0f;

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
        behaviors.put(Behavior.LeftScratched, new LeftScratchedBehavior());
        behaviors.put(Behavior.RightScratched, new RightScratchedBehavior());
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

    public void setBehavior(Behavior nextBehavior) {
        currBehavior = nextBehavior;
        GameObject object = behaviors.get(currBehavior);
        if (object instanceof SpriteAnimeObject) {
            SpriteAnimeObject animeObject = (SpriteAnimeObject)object;
            animeObject.resetAnimationTimer();
        }
        child = object;
    }

    public void downcastBehavior() {
        switch (currBehavior) {
            case LeftDefault:
                setBehavior(Behavior.LeftScratched);
                break;
            case RightDefault:
                setBehavior(Behavior.RightScratched);
                break;
            case LeftScratched:
            case RightScratched:
                setBehavior(null);
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }

    public void upcastBehavior() {
        switch (currBehavior) {
            case LeftScratched:
                setBehavior(Behavior.LeftDefault);
                break;
            case RightScratched:
                setBehavior(Behavior.RightDefault);
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }

    public void turnBehavior() {
        switch (currBehavior) {
            case LeftDefault:
                setBehavior(Behavior.RightDefault);
                break;
            case RightDefault:
                setBehavior(Behavior.LeftDefault);
                break;
            case LeftScratched:
                setBehavior(Behavior.RightScratched);
                break;
            case RightScratched:
                setBehavior(Behavior.LeftScratched);
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }

    public final float addDurability(float durability) {
        currDurability += durability;
        return currDurability;
    }

    public final float getCurrDurability() {
        return currDurability;
    }

    public final float getMaxDurability() {
        return maxDurability;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        if (currBehavior != null) {
            float nextDurability = currDurability - durabilityPerSeconds * elapsedTimeSec;
            currDurability = Math.max(0.0f, nextDurability);
        }
        Log.d(TAG, "::onUpdate >> 현재 낙하산의 내구도:" + currDurability + ", currBehavior:" + currBehavior);
    }
}
