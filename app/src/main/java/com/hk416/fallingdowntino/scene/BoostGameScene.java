package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;

import com.hk416.fallingdowntino.DataLoader;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.UiButtonObject;
import com.hk416.framework.object.UiRectObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.transform.Anchor;

public class BoostGameScene extends GameScene {
    private enum Tags { Background, Dialog, Button, Icon, Text }

    private static final String TAG = BoostGameScene.class.getSimpleName();
    private static final Anchor ENERGY_BTN_ANCHOR = new Anchor(
            0.4f, 0.1f, 0.596875f, 0.45f
    );
    private static final Anchor SPANNER_BTN_ANCHOR = new Anchor(
            0.4f, 0.55f, 0.596875f, 0.9f
    );

    private final DataLoader.DataBlock block;


    public BoostGameScene(DataLoader.DataBlock block) {
        super(Tags.values().length);
        this.block = block;
    }

    private GameObject createBackground() {
        return new UiRectObject(
                Color.argb(128, 128, 128, 128),
                0.0f,
                0.0f,
                new Anchor(0.0f, 0.0f, 1.0f, 1.0f)
        );
    }

    private GameObject createEnergyButton() {
        return new UiButtonObject(
                Color.argb(218, 250, 250, 250),
                Color.argb(218, 218, 218, 218),
                45.0f,
                45.0f,
                ENERGY_BTN_ANCHOR,
                null
        );
    }

    private GameObject createSpannerButton() {
        return new UiButtonObject(
                Color.argb(218, 250, 250, 250),
                Color.argb(218, 218, 218, 218),
                45.0f,
                45.0f,
                SPANNER_BTN_ANCHOR,
                null
        );
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        insertObject(Tags.Background, createBackground());

        insertObject(Tags.Button, createEnergyButton());

        insertObject(Tags.Button, createSpannerButton());

        Sound.resumeAllSounds();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
