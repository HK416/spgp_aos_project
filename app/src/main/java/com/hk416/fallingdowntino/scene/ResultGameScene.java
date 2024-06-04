package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.GameView;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.object.UiImageObject;
import com.hk416.framework.object.UiRectObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Anchor;

public class ResultGameScene extends GameScene {
    enum Tags { Dialog, Image, Text }

    private static final String TAG = ResultGameScene.class.getSimpleName();

    private final float distance;
    private final int likeCount;

    ResultGameScene(float distance, int likeCount) {
        super(Tags.values().length);
        this.distance = distance;
        this.likeCount = likeCount;
    }

    @Override
    public void onEnter() {
        Log.d(TAG, ":: onEnter >> 게임 장면에 진입함.");

        // 배경 사각형
        insertObject(Tags.Dialog, new UiRectObject(
                Color.argb(204, 255, 255, 255),
                45.0f,
                45.0f,
                new Anchor(0.1f, 0.05f, 0.65f, 0.95f)
        ));

        // 이동 거리 텍스트
        UiTextObject distText = new UiTextObject(
                String.format("%04dm", (int)distance),
                new Anchor(0.15f, 0.2f, 0.6f, 0.8f),
                UiTextObject.Pivot.Horizontal
        );
        distText.setColor(Color.BLACK);
        insertObject(Tags.Text, distText);

        // 좋아요 이미지
        UiImageObject likeSprite = new UiImageObject(
                R.mipmap.item_like,
                new Anchor(0.35f, 0.2f, 0.45f, 0.5f)
        );
        insertObject(Tags.Image, likeSprite);

        // 좋아요 갯수 텍스트
        UiTextObject likeText = new UiTextObject(
                String.format("%03d", likeCount),
                new Anchor(0.35f, 0.5f, 0.45f, 0.8f),
                UiTextObject.Pivot.Horizontal
        );
        likeText.setColor(Color.BLACK);
        insertObject(Tags.Text, likeText);

        // 터치하여 종료하기 텍스트
        UiTextObject exitText = new UiTextObject(
                GameView.getStringFromRes(R.string.in_game_exit_msg),
                new Anchor(0.55f, 0.15f, 0.55f, 0.85f),
                UiTextObject.Pivot.Horizontal
        );
        exitText.setColor(Color.BLACK);
        insertObject(Tags.Text, exitText);
    }

    @Override
    public void onExit() {
        Log.d(TAG, "::onExit >> 장면에 빠져나감");
        BitmapPool.getInstance().clear();
    }

    @Override
    public void handleEvent(@NonNull MotionEvent e) {
        super.handleEvent(e);
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            SceneManager.getInstance().cmdChangeScene(new TitleScene());
        }
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
