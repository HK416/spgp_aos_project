package com.hk416.fallingdowntino.scene;

import android.graphics.Color;
import android.util.Log;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.ui.DialogUi;
import com.hk416.fallingdowntino.object.ui.LikeUi;
import com.hk416.fallingdowntino.object.ui.ScoreUi;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.object.UiImageObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.transform.Anchor;

public class ResultGameScene extends GameScene {
    enum Tags { Dialog, Image, Button, Text }

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
        insertObject(Tags.Dialog, new DialogUi(
                Color.argb(204, 255, 255, 255),
                45.0f,
                45.0f
        ));

        // 이동 거리 텍스트
        UiTextObject distText = new UiTextObject(
                String.format("%04dm", (int)distance),
                new Anchor(0.15f, 0.2f, 0.6f, 0.8f)
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
                new Anchor(0.35f, 0.5f, 0.45f, 0.8f)
        );
        likeText.setColor(Color.BLACK);
        insertObject(Tags.Text, likeText);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
