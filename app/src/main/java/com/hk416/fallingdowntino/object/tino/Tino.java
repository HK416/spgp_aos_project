package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Transform;

import java.util.HashMap;
import java.util.Map;

public class Tino extends GameObject {
    public enum Behavior { Default, }

    protected static final String TAG = Tino.class.getSimpleName();
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 12.5f;

    public static Paint debugColor = null;

    private final Map<Behavior, GameObject> behaviors = new HashMap<>();
    private final Parachute parachute = new Parachute();

    public Tino() {
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

    @Override
    public void updateTransform(@Nullable Transform parent) {
        super.updateTransform(parent);
        if (parachute != null) {
            parachute.updateTransform(transform);
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        parachute.onUpdate(elapsedTimeSec);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        parachute.onDraw(canvas);
    }
}
