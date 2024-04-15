package com.hk416.fallingdowntino;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public interface IGameScene {
    void onEnter();
    void onExit();
    void onPause();
    void onResume();
    void handleEvent(@NonNull MotionEvent e);
    void onUpdate(float elapsedTimeSec, long frameRate);
    void onDraw(@NonNull Canvas canvas);
}
