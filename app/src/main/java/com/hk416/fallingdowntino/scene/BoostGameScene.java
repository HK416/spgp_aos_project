package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.UiRectObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.transform.Anchor;

public class BoostGameScene extends GameScene {
    private enum Tags { Background, Dialog, Icon, Text }

    private static final String TAG = BoostGameScene.class.getSimpleName();

    public BoostGameScene() {
        super(Tags.values().length);
    }

    private GameObject createBackground() {
        return new UiRectObject(
                Color.argb(128, 255, 255, 255),
                0.0f,
                0.0f,
                new Anchor(0.0f, 0.0f, 1.0f, 1.0f)
        );
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");
        insertObject(Tags.Background, createBackground());
        Sound.resumeAllSounds();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
