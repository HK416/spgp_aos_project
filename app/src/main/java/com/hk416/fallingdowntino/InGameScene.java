package com.hk416.fallingdowntino;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public final class InGameScene implements IGameScene {
    private static final String TAG = InGameScene.class.getSimpleName();

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함.");

        // MainCamera를 생성한다.
        GameCamera mainCamera = new MainCamera(0.0f, 8.0f);
        mainCamera.setProjection(new Projection(
                16.0f,
                -4.5f,
                0.0f,
                4.5f,
                0.0f,
                100.0f
        ));
        mainCamera.generateCameraTransform();

        // DrawPipeline을 설정한다.
        DrawPipeline pipeline = DrawPipeline.getInstance();
        pipeline.setRatio(9.0f, 16.0f);
        pipeline.setMainCamera(mainCamera);
    }

    @Override
    public void onExit() {
        Log.d(TAG, "::onExit >> 장면을 종료함.");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "::onPause >> 장면을 정지함.");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "::onResume >> 장면을 재개함.");
    }

    @Override
    public void handleEvent(@NonNull MotionEvent e) {
        Vector worldPoint = DrawPipeline.getInstance().toWorldCoord(new PointF(e.getX(), e.getY()));
        Log.d(TAG, "::handleEvent >> 입력받은 마우스의 월드 좌표계 (x:" + worldPoint.x
                + ", y:" + worldPoint.y
                + ", z:" + worldPoint.z
                + ")"
        );
    }

    @Override
    public void onUpdate(float elapsedTimeSec, long frameRate) {

    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
