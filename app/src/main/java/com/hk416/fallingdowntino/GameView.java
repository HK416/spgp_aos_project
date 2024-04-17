package com.hk416.fallingdowntino;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends View {
    public static final String TAG = GameView.class.getSimpleName();

    private FrameCallback currentCallback = null;

    private final FrameCallback startCallback = frameTimeNanos -> {
        onStartScheduling(frameTimeNanos);
        Choreographer.getInstance().postFrameCallback(currentCallback);
    };

    private final FrameCallback doFrameCallback = frameTimeNanos -> {
        onUpdate(frameTimeNanos);
        Choreographer.getInstance().postFrameCallback(currentCallback);
    };

    private final FrameCallback stopCallback = frameTimeNanos -> {
        onStopScheduling(frameTimeNanos);
        currentCallback = null;
    };

    private final GameTimer timer = new GameTimer();
    private final Activity activity;

    public GameView(Context context) {
        super(context);
        setFullScreen();

        // Context는 반드시 Activity라 가정한다.
        activity = (Activity)context;

        // 게임의 첫 번째 장면을 추가.
        SceneManager.getInstance().cmdPushScene(new InGameScene());
    }

    private void setFullScreen() {
        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        setSystemUiVisibility(flags);
    }

    public void startScheduling() {
        currentCallback = doFrameCallback;
        Choreographer.getInstance().postFrameCallback(startCallback);
    }

    public void stopScheduling() {
        currentCallback = stopCallback;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        SceneManager.getInstance().onDraw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                SceneManager.getInstance().addMotionEvent(event);
                return true;
            case MotionEvent.ACTION_UP:
                SceneManager.getInstance().addMotionEvent(event);
                return false;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        DrawPipeline.getInstance().onResize(w, h);
    }

    private void onStartScheduling(long frameTimeNanos) {
        Log.d(TAG, "::onStartScheduling >> 게임 루프 시작.");
        timer.reset(frameTimeNanos);
        SceneManager.getInstance().onResume();
    }

    private void onUpdate(long frameTimeNanos) {
        timer.tick(frameTimeNanos);
        boolean isRunning = SceneManager.getInstance().onUpdate(
                timer.getElapsedTimeSec(),
                timer.getFrameRate()
        );

        if (!isRunning) {
            activity.finish();
        }
    }

    private void onStopScheduling(long frameTimeNanos) {
        Log.d(TAG, "::onStopScheduling >> 게임 루프 정지.");
        SceneManager.getInstance().onPause();
    }
}