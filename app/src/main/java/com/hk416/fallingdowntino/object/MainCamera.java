package com.hk416.fallingdowntino.object;

import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;

public final class MainCamera extends GameCamera {
    public static final float X_POS = 0.0f;
    public static final float Y_POS = 8.0f;

    public static final float PROJ_LEFT = -4.5f;
    public static final float PROJ_RIGHT = 4.5f;
    public static final float PROJ_TOP = 8.0f;
    public static final float PROJ_BOTTOM = -8.0f;
    public static final float PROJ_Z_NEAR = 0.0f;
    public static final float PROJ_Z_FAR = 100.0f;

    public MainCamera() {
        super(X_POS, Y_POS);
        setProjection(new Projection(
                PROJ_TOP,
                PROJ_LEFT,
                PROJ_BOTTOM,
                PROJ_RIGHT,
                PROJ_Z_NEAR,
                PROJ_Z_FAR
        ));
        generateCameraTransform();
    }
}
