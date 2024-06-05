package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.UiRectObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Anchor;

public class PauseScene extends GameScene {
    private enum Tags { Background, Text }

    private static final String TAG = PauseScene.class.getSimpleName();

    public PauseScene() {
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

    private GameObject createMainText() {
        return new UiTextObject(
                R.string.pause_main_text,
                new Anchor(0.1f, 0.1f, 0.4f, 0.9f),
                UiTextObject.Pivot.Horizontal
        );
    }

    private GameObject createSubText() {
        return new UiTextObject(
                R.string.pause_sub_text,
                new Anchor(0.8f, 0.1f, 0.9f, 0.9f),
                UiTextObject.Pivot.Horizontal
        );
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");
        insertObject(Tags.Background, createBackground());
        insertObject(Tags.Text, createMainText());
        insertObject(Tags.Text, createSubText());
    }

    @Override
    public void handleEvent(@NonNull MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            SceneManager.getInstance().cmdPopScene();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "::onPuase >> 일시정지");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "::onResume >> 재개");
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
