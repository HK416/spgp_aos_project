package com.hk416.fallingdowntino;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public final class InGameScene implements IGameScene {
    private static final String TAG = InGameScene.class.getSimpleName();

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함.");
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
        Log.d(TAG, "::handleEvent >> 전달 받은 이벤트:" + e);
    }

    @Override
    public void onUpdate(float elapsedTimeSec, long frameRate) {

    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
