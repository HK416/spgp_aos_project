package com.hk416.fallingdowntino.scene;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.hk416.fallingdowntino.DataLoader;
import com.hk416.fallingdowntino.GameView;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.UiButtonObject;
import com.hk416.framework.object.UiImageObject;
import com.hk416.framework.object.UiRectObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Anchor;

public class BoostGameScene extends GameScene {
    private enum Tags { Background, Button, Icon, Text }

    private static final String TAG = BoostGameScene.class.getSimpleName();
    private static final Anchor ENERGY_BTN_ANCHOR = new Anchor(
            0.4f, 0.325f, 0.596875f, 0.675f
    );
    private static final Anchor ENERGY_ICO_ANCHOR = new Anchor(
            0.45f, 0.375f, 0.546875f, 0.625f
    );
    private static final Anchor SKIP_BTN_ANCHOR = new Anchor(
            0.8f, 0.1f, 0.9f, 0.9f
    );
    private static final Anchor SKIP_TEXT_ANCHOR = new Anchor(
            0.82f, 0.1f, 0.87f, 0.9f
    );
    private static final Anchor INFO_TEXT_ANCHOR = new Anchor(
            0.62f, 0.1f, 0.68f, 0.9f
    );
    private static final Anchor LIKE_ICO_ANCHOR = new Anchor(
            0.72f, 0.35f, 0.78f, 0.48f
    );
    private static final Anchor LIKE_TEXT_ANCHOR = new Anchor(
            0.72f, 0.5f, 0.77f, 0.68f
    );

    public static final long COST = 30;

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
                () -> {
                    block.numLikes -= COST;
                    GameView.setDataBlock(block);
                    SceneManager.getInstance().cmdChangeScene(new InGameScene(true));
                }
        );
    }

    private GameObject createEnergyButtonIcon() {
        return new UiImageObject(R.mipmap.item_energy, ENERGY_ICO_ANCHOR);
    }

    private GameObject createSkipButton() {
        return new UiButtonObject(
                Color.argb(218, 250, 250, 250),
                Color.argb(218, 218, 218, 218),
                45.0f,
                45.0f,
                SKIP_BTN_ANCHOR,
                () -> {
                    SceneManager.getInstance().cmdChangeScene(new InGameScene());
                }
        );
    }

    private GameObject createSkipButtonText() {
        return new UiTextObject(
                R.string.boost_btn_skip_text,
                SKIP_TEXT_ANCHOR,
                UiTextObject.Pivot.Vertical
        );
    }

    @SuppressLint("DefaultLocale")
    private GameObject createInfoText() {
        String costText = GameView.getStringFromRes(R.string.boost_cost_text);
        String text = String.format("%s:%d", costText, COST);
        return new UiTextObject(
                text,
                INFO_TEXT_ANCHOR,
                UiTextObject.Pivot.Vertical
        );
    }

    private GameObject createLikeIcon() {
        return new UiImageObject(R.mipmap.item_like, LIKE_ICO_ANCHOR);
    }

    @SuppressLint("DefaultLocale")
    private GameObject createLikeScoreText(long numLikes) {
        String text = String.format("%03d", numLikes);
        return new UiTextObject(text, LIKE_TEXT_ANCHOR, UiTextObject.Pivot.Vertical);
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        insertObject(Tags.Background, createBackground());

        insertObject(Tags.Button, createEnergyButton());
        insertObject(Tags.Text, createEnergyButtonIcon());

        insertObject(Tags.Button, createSkipButton());
        insertObject(Tags.Text, createSkipButtonText());

        insertObject(Tags.Text, createInfoText());

        insertObject(Tags.Icon, createLikeIcon());
        insertObject(Tags.Text, createLikeScoreText(block.numLikes));

        Sound.resumeAllSounds();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
