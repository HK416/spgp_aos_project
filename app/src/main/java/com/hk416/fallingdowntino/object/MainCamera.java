package com.hk416.fallingdowntino.object;

import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;

public final class MainCamera extends GameCamera {
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 8.0f;

    public MainCamera() {
        super(X_POS, Y_POS);
        setProjection(new Projection(
                8.0f,
                -4.5f,
                -8.0f,
                4.5f,
                0.0f,
                100.0f
        ));
        generateCameraTransform();
    }
}
