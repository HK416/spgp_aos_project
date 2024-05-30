package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;

import com.hk416.fallingdowntino.object.ui.DialogUi;
import com.hk416.framework.scene.GameScene;

public class ResultGameScene extends GameScene {
    enum Tags { Dialog, Image, Button, Text }

    private static final String TAG = ResultGameScene.class.getSimpleName();

    ResultGameScene() {
        super(Tags.values().length);
    }

    @Override
    public void onEnter() {
        Log.d(TAG, ":: onEnter >> 게임 장면에 진입함.");
        insertObject(Tags.Dialog, new DialogUi(
                Color.argb(204, 255, 255, 255),
                45.0f,
                45.0f
        ));
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
