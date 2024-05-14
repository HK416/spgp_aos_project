package com.hk416.fallingdowntino.object.tino;

import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteAnimeObject;

import java.util.HashMap;
import java.util.Map;

public class Tino extends GameObject {
    public enum Behavior {
        LeftDefault, RightDefault,
        LeftScared, RightScared,
        LeftHappy, RightHappy,
        LeftDive, RightDive,
    }

    protected static final String TAG = Tino.class.getSimpleName();
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;
    public static final float SCARED_POINT = 30.0f;
    public static final float HAPPY_DUARTION = 3.0f;

    public static final float BOX_X = 0.0f;
    public static final float BOX_Y = -0.2f;
    public static final float BOX_WIDTH = 1.2f;
    public static final float BOX_HEIGHT = 1.6f;

    public static Paint debugColor = null;

    private final Map<Behavior, GameObject> behaviors = new HashMap<>();
    private Behavior currBehavior = Behavior.RightDefault;

    public Tino(@NonNull Player player) {
        super();
        createBehaviors(player);
        createDebugPaint();
    }

    private void createBehaviors(@NonNull Player player) {
        behaviors.put(Behavior.LeftDefault, new LeftDefaultBehavior(player));
        behaviors.put(Behavior.RightDefault, new RightDefaultBehavior(player));
        behaviors.put(Behavior.LeftScared, new LeftScaredBehavior(player));
        behaviors.put(Behavior.RightScared, new RightScaredBehavior(player));
        behaviors.put(Behavior.LeftHappy, new LeftHappyBehavior(player));
        behaviors.put(Behavior.RightHappy, new RightHappyBehavior(player));
        behaviors.put(Behavior.LeftDive, new LeftDiveBehavior(player));
        behaviors.put(Behavior.RightDive, new RightDiveBehavior(player));
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

    public void upcastBehavior() {
        switch (currBehavior) {
            case LeftScared:
                setBehavior(Behavior.LeftDefault);
                break;
            case RightScared:
                setBehavior(Behavior.RightDefault);
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }

    public void downcastBehavior() {
        switch (currBehavior) {
            case LeftDefault:
            case LeftHappy:
                setBehavior(Behavior.LeftScared);
                break;
            case RightDefault:
            case RightHappy:
                setBehavior(Behavior.RightScared);
                break;
            case LeftScared:
                setBehavior(Behavior.LeftDive);
                break;
            case RightScared:
                setBehavior(Behavior.RightDive);
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
            case LeftHappy:
                setBehavior(Behavior.RightHappy);
                break;
            case RightHappy:
                setBehavior(Behavior.LeftHappy);
                break;
            case LeftScared:
                setBehavior(Behavior.RightScared);
                break;
            case RightScared:
                setBehavior(Behavior.LeftScared);
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }
}
