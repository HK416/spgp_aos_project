package com.hk416.fallingdowntino.object.tino;

import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;

import java.util.HashMap;
import java.util.Map;

public class Tino extends GameObject {
    public enum Behavior {
        LeftDefault, RightDefault,
        LeftScared, RightScared,
        LeftDive, RightDive
    }

    protected static final String TAG = Tino.class.getSimpleName();
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;

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

    public void upcastBehavior() {
        switch (currBehavior) {
            case LeftScared:
                child = behaviors.get(Behavior.LeftDefault);
                currBehavior = Behavior.LeftDefault;
                break;
            case RightScared:
                child = behaviors.get(Behavior.RightDefault);
                currBehavior = Behavior.RightDefault;
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
                child = behaviors.get(Behavior.LeftScared);
                currBehavior = Behavior.LeftScared;
                break;
            case RightDefault:
                child = behaviors.get(Behavior.RightScared);
                currBehavior = Behavior.RightScared;
                break;
            case LeftScared:
                child = behaviors.get(Behavior.LeftDive);
                currBehavior = Behavior.LeftDive;
                break;
            case RightScared:
                child = behaviors.get(Behavior.RightDive);
                currBehavior = Behavior.RightDive;
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
                child = behaviors.get(Behavior.RightDefault);
                currBehavior = Behavior.RightDefault;
                break;
            case RightDefault:
                child = behaviors.get(Behavior.LeftDefault);
                currBehavior = Behavior.LeftDefault;
                break;
            case LeftScared:
                child = behaviors.get(Behavior.RightScared);
                currBehavior = Behavior.RightScared;
                break;
            case RightScared:
                child = behaviors.get(Behavior.LeftScared);
                currBehavior = Behavior.LeftScared;
                break;
            default:
                /* empty */
                return;
        }
        updateTransform(null);
    }
}
