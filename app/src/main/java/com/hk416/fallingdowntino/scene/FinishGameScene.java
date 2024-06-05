package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.DataLoader;
import com.hk416.fallingdowntino.GameView;
import com.hk416.fallingdowntino.R;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;
import com.hk416.framework.transform.Viewport;

public final class FinishGameScene extends GameScene {
    public enum Tags { Object, Player };

    private static final String TAG = FinishGameScene.class.getSimpleName();

    public static final float MIN_DURATION = 4.0f;

    private final float duration;
    private final GameObject player;
    private final GameObject tile;
    private final Projection srcProjection;
    private final Projection dstProjection;
    private final Projection currProjection;

    private final Vector srcPosition;
    private final Vector dstPosition;

    private final float distance;
    private final int likeCount;

    private float timer = 0.0f;

    public FinishGameScene(
            float duration,
            float distance,
            int likeCount,
            @NonNull GameObject player,
            @NonNull GameObject tile
    ) {
        super(Tags.values().length);
        this.duration = Math.max(duration, MIN_DURATION);
        this.distance = distance;
        this.likeCount = likeCount;
        this.player = player;
        this.tile = tile;

        GameCamera currCamera = DrawPipeline.getInstance().getMainCamera();
        Projection srcProjection = currCamera.getProjection();
        float leftProj = 0.5f * Projection.DEF_LEFT;
        float rightProj = 0.5f * Projection.DEF_RIGHT;
        float topSide = 0.75f * Projection.DEF_TOP;
        float bottomSide = 0.25f * Projection.DEF_BOTTOM;

        this.srcProjection = srcProjection;
        this.dstProjection = new Projection(topSide, leftProj, bottomSide, rightProj, 0.0f, 100.0f);
        this.currProjection = new Projection(srcProjection.top, srcProjection.left, srcProjection.bottom, srcProjection.right, 0.0f, 100.0f);

        this.srcPosition = currCamera.getWorldPosition();
        Vector playerPos = player.getWorldPosition();
        this.dstPosition = new Vector(playerPos.x, playerPos.y, playerPos.z);
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        GameCamera currCamera = DrawPipeline.getInstance().getMainCamera();
        currCamera.setProjection(currProjection);

        insertObject(Tags.Player, player);
        insertObject(Tags.Object, tile);

        Sound.pauseMusic();
        Sound.playEffect(R.raw.effect2);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "::onPause >> 일시정지");
        Sound.pauseEffects();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "::onResume >> 재개");
        Sound.resumeEffects();
    }

    @Override
    public void onUpdate(float elapsedTimeSec, long frameRate) {
        timer = Math.min(timer + elapsedTimeSec, duration);

        float delta = timer / duration;
        currProjection.top = srcProjection.top + (dstProjection.top - srcProjection.top) * delta;
        currProjection.left = srcProjection.left + (dstProjection.left - srcProjection.left) * delta;
        currProjection.bottom = srcProjection.bottom + (dstProjection.bottom - srcProjection.bottom) * delta;
        currProjection.right = srcProjection.right + (dstProjection.right - srcProjection.right) * delta;

        float x = srcPosition.x + (dstPosition.x - srcPosition.x) * delta;
        float y = srcPosition.y + (dstPosition.y - srcPosition.y) * delta;
        GameCamera currCamera =  DrawPipeline.getInstance().getMainCamera();
        currCamera.setPosition(x, y);
        currCamera.generateCameraTransform();

        // 결과 표시 장면으로 넘어간다.
        if (timer >= duration) {
            DataLoader.DataBlock block = GameView.getDataBlock();
            boolean newRecord = block.bestDistance < (long)distance;
            block.bestDistance = Math.max(block.bestDistance, (long)distance);
            block.numLikes += likeCount;
            GameView.setDataBlock(block);

            SceneManager.getInstance().cmdPushScene(new ResultGameScene(
                    newRecord,
                    distance,
                    likeCount
            ));
            return;
        }
        super.onUpdate(elapsedTimeSec, frameRate);
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
