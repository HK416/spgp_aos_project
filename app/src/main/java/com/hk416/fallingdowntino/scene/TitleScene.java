package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.land.Tile;
import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Viewport;

public class TitleScene extends GameScene {
    enum Tags { Background, Tino, Button, Text };

    private static final String TAG = TitleScene.class.getSimpleName();

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
        return new SpriteObject(
                R.mipmap.tino_crash_3,
                TINO_POS_X,
                TINO_POS_Y,
                Tino.WIDTH,
                Tino.HEIGHT,
                true,
                false
        );
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        setupMainCamera();
        insertObject(Tags.Background, createGround());
        insertObject(Tags.Tino, createTino());
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
