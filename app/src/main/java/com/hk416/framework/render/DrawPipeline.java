package com.hk416.framework.render;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.framework.transform.Vector;
import com.hk416.framework.transform.Viewport;

public final class DrawPipeline {
    private static final String TAG = DrawPipeline.class.getSimpleName();

    private static final DrawPipeline instance = new DrawPipeline();
    private static final int DEF_SCREEN_WIDTH = 720;
    private static final int DEF_SCREEN_HEIGHT = 1280;
    private static final float DEF_RATIO = 9.0f / 16.0f;

    private float ratio = DEF_RATIO;
    private int screenWidth = DEF_SCREEN_WIDTH;
    private int screenHeight = DEF_SCREEN_HEIGHT;
    private GameCamera mainCamera = null;
    private final Viewport viewport = new Viewport();

    public static DrawPipeline getInstance() {
        return instance;
    }

    public void setRatio(float w, float h) {
        ratio = w / h;
    }

    public void setMainCamera(@NonNull GameCamera camera) {
        this.mainCamera = camera;
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
    }

    public void drawDebugArea(@NonNull Canvas canvas) {
        viewport.drawDebugArea(canvas);
    }

    public void onResize(int w, int h) {
        screenWidth = w;
        screenHeight = h;
        setAutoViewport();
    }

    /**
     * 게임 월드 좌표계상의 한 점의 위치를 스크린 좌표계상의 한 점의 위치로 변환합니다.
     * 만약 mainCamera를 DrawPipeline에 설정하지 않았을 경우 null을 반환합니다.
     *
     * @param worldPoint 게임 월드 좌표계상의 한 점
     * @return 스크린 좌표계상의 한 점의 위치 또는 null
     */
    public PointF toScreenCoord(@NonNull Vector worldPoint) {
        if (mainCamera == null) {
            Log.w(TAG, "::toScreenCoord >> mainCamera가 설정되지 않았습니다.");
            return null;
        }

        Vector projctionPoint = mainCamera.toProjectionCoord(worldPoint);
        return viewport.toScreenCoord(projctionPoint);
    }

    /**
     * 스크린 좌표계상의 한 점의 위치를 월드 좌표계상의 한 점의 위치로 변환합니다.
     * 만약 mainCamera를 DrawPipeline에 설정하지 않았을 경우 null을 반환합니다.
     *
     * @param screenPoint 스크린 좌표계상의 한 점
     * @return 월드 좌표계상의 한 점의 위치 또는 null
     */
    public Vector toWorldCoord(@NonNull PointF screenPoint) {
        if (mainCamera == null) {
            Log.w(TAG, "::toWorldCoord >> mainCamera가 설정되지 않았습니다.");
            return null;
        }

        Vector projectionPoint = viewport.toProjectionCoord(screenPoint);
        return mainCamera.toWorldCoord(projectionPoint);
    }
}
