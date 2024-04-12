package com.hk416.fallingdowntino;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.NonNull;


public class GameView extends View implements Choreographer.FrameCallback {
    public static final String TAG = GameView.class.getSimpleName();

    public GameView(Context context) {
        super(context);

        run();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
    }

    private void run() {
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        Log.d(TAG, "doFrame!");
        run();
    }
}