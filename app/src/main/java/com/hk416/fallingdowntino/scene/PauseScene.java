package com.hk416.fallingdowntino.scene;

import android.util.Log;

import com.hk416.framework.scene.GameScene;

public class PauseScene extends GameScene {
    private enum Tags { Background, Text }

    private static final String TAG = PauseScene.class.getSimpleName();

    public PauseScene() {
        super(Tags.values().length);
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");
    }
}
