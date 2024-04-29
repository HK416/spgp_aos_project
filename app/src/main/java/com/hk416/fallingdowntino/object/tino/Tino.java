package com.hk416.fallingdowntino.object.tino;

import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;

import java.util.HashMap;
import java.util.Map;

public class Tino extends GameObject {
    public enum Behavior { LeftDefault, RightDefault, }

    protected static final String TAG = Tino.class.getSimpleName();
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;

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
}
