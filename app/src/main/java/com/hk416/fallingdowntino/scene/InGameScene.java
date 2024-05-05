package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.MainCamera;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.ScoreUi;
import com.hk416.fallingdowntino.object.land.Land;
import com.hk416.framework.object.UiObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.GameScene;

public final class InGameScene extends GameScene {
    private static final String TAG = InGameScene.class.getSimpleName();

    public enum Tags { Object, Player, Ui }

    private Player player;

    public InGameScene() {
        super(Tags.values().length);
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함.");

        // MainCamera를 생성한다.
        GameCamera mainCamera = new MainCamera();

        // DrawPipeline을 설정한다.
        DrawPipeline pipeline = DrawPipeline.getInstance();
        pipeline.setRatio(9.0f, 16.0f);
        pipeline.setMainCamera(mainCamera);

        // 게임 오브젝트들을 추가한다.
        player = new Player();
        super.insertObject(Tags.Player, player);
        super.insertObject(Tags.Object, new Land(0.0f, 0.0f, 12.0f, 2.0f, player));

        // Ui 오브젝트들을 추가한다.
        super.insertObject(Tags.Ui, new ScoreUi(player));
    }

    @Override
    public void onExit() {
        Log.d(TAG, "::onExit >> 장면을 종료함.");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "::onPause >> 장면을 정지함.");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "::onResume >> 장면을 재개함.");
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
        super.onDraw(canvas);
    }
}
