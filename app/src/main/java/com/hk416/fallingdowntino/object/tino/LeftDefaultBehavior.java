package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;

public class LeftDefaultBehavior extends SpriteAnimeObject {
    private static final float ANIMATION_SPEED = 0.8f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_default_0, R.mipmap.tino_default_1,
            R.mipmap.tino_default_2, R.mipmap.tino_default_1,
    };

    private final Player player;

    public LeftDefaultBehavior(@NonNull Player player) {
        super(
                BITMAP_RES_IDS,
                Tino.WIDTH,
                Tino.HEIGHT,
                ANIMATION_SPEED,
                false,
                false
        );
        this.player = player;
    }

    private void updatePlayerPosition(float elapsedTimeSec) {
        GameCamera mainCamera = DrawPipeline.getInstance().getMainCamera();
        Projection projection = mainCamera.getProjection();
        if (projection == null) {
            return;
        }

        Vector newPosition = player.getPosition();
        newPosition.x -= Player.SPEED * elapsedTimeSec;

        float left = newPosition.x - 0.5f * Tino.WIDTH;
        if (left <= projection.left) {
            float diff = projection.left - left;
            newPosition.x = projection.left + Tino.WIDTH + diff;
            player.turnBehavior();
        }
        player.setPosition(newPosition.x, newPosition.y);
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        updatePlayerPosition(elapsedTimeSec);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }
}
