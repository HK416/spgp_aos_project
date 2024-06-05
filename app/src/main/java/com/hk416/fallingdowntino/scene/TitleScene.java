package com.hk416.fallingdowntino.scene;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.DataLoader;
import com.hk416.fallingdowntino.GameView;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.land.Tile;
import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.object.UiButtonObject;
import com.hk416.framework.object.UiImageObject;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Viewport;

public class TitleScene extends GameScene {
    enum Tags { Background, Icon, Button, Text };

    private static final String TAG = TitleScene.class.getSimpleName();
    private static final Anchor TITLE_TEXT_ANCHOR = new Anchor(
            0.1f, 0.1f, 0.1f, 0.9f
    );
    private static final Anchor BEST_TEXT_ANCHOR = new Anchor(
            0.25f, 0.3f, 0.28f, 0.7f
    );
    private static final Anchor LIKE_ICO_ANCHOR = new Anchor(
            0.9f, 0.7f, 0.9f, 0.8f
    );
    private static final Anchor LIKE_SCORE_ANCHOR = new Anchor(
            0.878f, 0.82f, 0.9f, 0.92f
    );
    private static final Anchor START_BTN_ANCHOR = new Anchor(
            0.4f, 0.1f, 0.58f, 0.9f
    );
    private static final Anchor START_BTN_TEXT_ANCHOR = new Anchor(
            0.445f, 0.1f, 0.515f, 0.9f
    );
    private static final Anchor EXIT_BTN_ANCHOR = new Anchor(
            0.6f, 0.1f, 0.78f, 0.9f
    );
    private static final Anchor EXIT_BTN_TEXT_ANCHOR = new Anchor(
            0.645f, 0.1f, 0.715f, 0.9f
    );

    public static final float CAMERA_POS_X = 0.0f;
    public static final float CAMERA_POS_Y = 8.0f;

    public static final float TINO_POS_X = -3.0f;
    public static final float TINO_POS_Y = 0.5f * Tino.HEIGHT + 0.5f * Tile.TILE_HEIGHT;


    public static final float PROJ_LEFT = -4.5f;
    public static final float PROJ_RIGHT = 4.5f;
    public static final float PROJ_TOP = 8.0f;
    public static final float PROJ_BOTTOM = -8.0f;
    public static final float PROJ_WIDTH = PROJ_RIGHT - PROJ_LEFT;

    public TitleScene() {
        super(Tags.values().length);
    }

    void setupMainCamera() {
        GameCamera mainCamera = new GameCamera(CAMERA_POS_X, CAMERA_POS_Y);
        mainCamera.setProjection(new Projection(
                PROJ_TOP, PROJ_LEFT, PROJ_BOTTOM, PROJ_RIGHT, 0.0f, 100.0f
        ));
        mainCamera.generateCameraTransform();
        DrawPipeline.getInstance().setMainCamera(mainCamera);
    }

    @NonNull
    GameObject createGround() {
        return new Tile(
                0.0f,
                0.0f,
                PROJ_WIDTH,
                Tile.TILE_HEIGHT
        );
    }

    GameObject createTino() {
        final int[] MIPMAP_RES_IDS = new int[] {
                R.mipmap.tino_crash_3,
                R.mipmap.tino_landing_1
        };
        int resId = (int)Math.round(Math.random());
        return new SpriteObject(
                MIPMAP_RES_IDS[resId],
                TINO_POS_X,
                TINO_POS_Y,
                Tino.WIDTH,
                Tino.HEIGHT,
                true,
                false
        );
    }

    GameObject createTitleText() {
        String titleText = GameView.getStringFromRes(R.string.app_name);
        return new UiTextObject(
                titleText,
                TITLE_TEXT_ANCHOR,
                UiTextObject.Pivot.Horizontal
        );
    }

    @SuppressLint("DefaultLocale")
    GameObject createBestDistText(long bestDistance) {
        String locale = GameView.getStringFromRes(R.string.title_best_text);
        String bestText = String.format("%s:%04dm", locale, bestDistance);
        return new UiTextObject(
                bestText,
                BEST_TEXT_ANCHOR,
                UiTextObject.Pivot.Vertical
        );
    }

    GameObject createExitButton() {
        return new UiButtonObject(
                R.mipmap.button_released,
                R.mipmap.button_pressed,
                EXIT_BTN_ANCHOR,
                () -> {
                    SceneManager.getInstance().cmdPopScene();
                }
        );
    }

    GameObject createExitButtonText() {
        return new UiTextObject(
                R.string.title_btn_exit,
                EXIT_BTN_TEXT_ANCHOR,
                UiTextObject.Pivot.Vertical
        );
    }

    GameObject createLikeIcon() {
        return new UiImageObject(R.mipmap.item_like, LIKE_ICO_ANCHOR);
    }

    @SuppressLint("DefaultLocale")
    GameObject createLikeScore(long numLikes) {
        String text = String.format("%03d", numLikes);
        return new UiTextObject(text, LIKE_SCORE_ANCHOR, UiTextObject.Pivot.Horizontal);
    }

    GameObject createStartButton() {
        return new UiButtonObject(
                R.mipmap.button_released,
                R.mipmap.button_pressed,
                START_BTN_ANCHOR,
                () -> SceneManager.getInstance().cmdChangeScene(new PrepareGameScene())
        );
    }

    GameObject createStartButtonText() {
        return new UiTextObject(
                R.string.title_btn_start,
                START_BTN_TEXT_ANCHOR,
                UiTextObject.Pivot.Vertical
        );
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        DataLoader.DataBlock block = GameView.getDataBlock();

        setupMainCamera();
        insertObject(Tags.Background, createGround());
        insertObject(Tags.Icon, createTino());

        insertObject(Tags.Text, createTitleText());
        insertObject(Tags.Text, createBestDistText(block.bestDistance));

        insertObject(Tags.Icon, createLikeIcon());
        insertObject(Tags.Text, createLikeScore(block.numLikes));

        insertObject(Tags.Button, createStartButton());
        insertObject(Tags.Text, createStartButtonText());

        insertObject(Tags.Button, createExitButton());
        insertObject(Tags.Text, createExitButtonText());

        Sound.playMusic(R.raw.title);
    }

    @Override
    public void onExit() {
        Log.d(TAG, "::onExit >> 장면을 빠져나감");
        Sound.stopMusic();
        BitmapPool.getInstance().clear();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        canvas.save();
        canvas.clipRect(viewport.left, viewport.top, viewport.right, viewport.bottom);
        canvas.drawColor(Color.parseColor("#FBEAFF"));
        super.onDraw(canvas);
        canvas.restore();
    }
}
