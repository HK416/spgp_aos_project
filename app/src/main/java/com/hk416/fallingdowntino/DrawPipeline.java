package com.hk416.fallingdowntino;

import android.util.Log;

public final class DrawPipeline {
    private static final String TAG = DrawPipeline.class.getSimpleName();
    private static final DrawPipeline instance = new DrawPipeline();
    private static final int DEF_SCREEN_WIDTH = 720;
    private static final int DEF_SCREEN_HEIGHT = 1280;
    private static final float DEF_RATIO = 9.0f / 16.0f;

    private float ratio = DEF_RATIO;
    private int screenWidth = DEF_SCREEN_WIDTH;
    private int screenHeight = DEF_SCREEN_HEIGHT;
    private final Viewport viewport = new Viewport();

    public static DrawPipeline getInstance() {
        return instance;
    }

    public void setRatio(float w, float h) {
        ratio = w / h;
    }

    public void setViewport(float top, float left, float bottom, float right) {
        viewport.top = top;
        viewport.left = left;
        viewport.bottom = bottom;
        viewport.right = right;
    }

    public void setAutoViewport() {
        float centerX = 0.5f * screenWidth;
        float centerY = 0.5f * screenHeight;
        float viewWidth = 0.0f;
        float viewHeight = 0.0f;
        if (screenWidth <= screenHeight) {
            viewWidth = screenWidth;
            viewHeight = screenWidth * 1.0f / ratio;

        } else {
            viewWidth = screenHeight * ratio;
            viewHeight = screenHeight;
        }

        float halfViewWidth = 0.5f * viewWidth;
        float halfViewHeight = 0.5f * viewHeight;
        setViewport(
                centerY - halfViewHeight,
                centerX - halfViewWidth,
                centerY + halfViewHeight,
                centerX + halfViewWidth
        );

        Log.d(TAG, "::setAutoViewport >> top:" + viewport.top
                + ", left:" + viewport.left
                + ", bottom:" + viewport.bottom
                + ", right:" + viewport.right
        );
    }

    public void onResize(int w, int h) {
        screenWidth = w;
        screenHeight = h;
        setAutoViewport();
    }
}
