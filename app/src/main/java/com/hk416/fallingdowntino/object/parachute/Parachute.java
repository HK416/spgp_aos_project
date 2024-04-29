package com.hk416.fallingdowntino.object.parachute;

import android.graphics.Paint;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.object.GameObject;

import java.util.HashMap;
import java.util.Map;

public class Parachute extends GameObject {
    public enum Behavior { Default, Scratched }

    private static final String TAG = Parachute.class.getSimpleName();
    public static final float WIDTH = 2.5f;
    public static final float HEIGHT = 2.5f;
    public static final float X_POS = -0.05f;
    public static final float Y_POS = 0.5f;

    public static Paint debugColor = null;

    private final Map<Behavior, GameObject> behaviors = new HashMap<>();

    public Parachute() {
        super(X_POS, Y_POS);
        createBehaviors();
        createDebugPaint();
    }

    private void createBehaviors() {
        behaviors.put(Behavior.Default, new DefaultBehavior());
        child = behaviors.get(Behavior.Default);
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
}
