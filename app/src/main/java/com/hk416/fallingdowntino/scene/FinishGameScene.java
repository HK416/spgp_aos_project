package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.land.Tile;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.transform.Viewport;

public final class FinishGameScene extends GameScene {
    public enum Tags { Object, Player };

    private static final String TAG = FinishGameScene.class.getSimpleName();

    private final Player player;
    private final Tile tile;

    public FinishGameScene(
            @NonNull Player player,
            @NonNull Tile tile
    ) {
        super(Tags.values().length);
        this.player = player;
        this.tile = tile;
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");
        insertObject(Tags.Player, player);
        insertObject(Tags.Object, tile);
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
