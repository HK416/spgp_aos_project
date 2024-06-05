package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.fallingdowntino.object.ui.DurabilityUi;
import com.hk416.fallingdowntino.object.ui.LikeUi;
import com.hk416.fallingdowntino.object.MainCamera;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.ui.ScoreUi;
import com.hk416.fallingdowntino.object.items.ItemPool;
import com.hk416.fallingdowntino.object.land.BlockPool;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Viewport;

public final class InGameScene extends GameScene {
    private static final String TAG = InGameScene.class.getSimpleName();

    public enum Tags { Object, Item, Player, Ui }

    private final PauseScene pauseScene = new PauseScene();

    private Player player;


    public InGameScene() {
        super(Tags.values().length);
    }

    private void setupDrawPipeline() {
        DrawPipeline pipeline = DrawPipeline.getInstance();
        pipeline.setRatio(9.0f, 16.0f);
        pipeline.setMainCamera(new MainCamera());
    }

    private void setupObjects() {
        player = new Player();
        insertObject(Tags.Player, player);
        insertObject(Tags.Object, new BlockPool(
                MainCamera.PROJ_LEFT,
                MainCamera.PROJ_RIGHT,
                -24.0f,
                player
        ));
        insertObject(Tags.Item, new ItemPool(
                MainCamera.PROJ_LEFT,
                MainCamera.PROJ_RIGHT,
                -12.0f,
                player
        ));
    }

    private void setupUserInterface() {
        insertObject(Tags.Ui, new ScoreUi(player));
        insertObject(Tags.Ui, new LikeUi(player));
        insertObject(Tags.Ui, new DurabilityUi(player));
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함.");
        setupDrawPipeline();
        setupObjects();
        setupUserInterface();
    }

    @Override
    public void onExit() {
        Log.d(TAG, "::onExit >> 장면을 종료함.");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "::onPause >> 장면을 정지함.");
        if (player.getTino().getBehavior() == Tino.Behavior.LeftInvincible
        || player.getTino().getBehavior() == Tino.Behavior.RightInvincible) {
            Sound.pauseEffects();
        } else {
            Sound.pauseMusic();
        }

        SceneManager.getInstance().cmdPushScene(pauseScene);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "::onResume >> 장면을 재개함.");
        if (player.getTino().getBehavior() == Tino.Behavior.LeftInvincible
                || player.getTino().getBehavior() == Tino.Behavior.RightInvincible) {
            Sound.resumeEffects();
        } else {
            Sound.resumeMusic();
        }
    }

    @Override
    public void handleEvent(@NonNull MotionEvent e) {
        super.handleEvent(e);
    }

    @Override
    public void onUpdate(float elapsedTimeSec, long frameRate) {
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
