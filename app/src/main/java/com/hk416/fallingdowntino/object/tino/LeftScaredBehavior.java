package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;

public class LeftScaredBehavior extends SpriteAnimeObject {
    private static final String TAG = LeftScaredBehavior.class.getSimpleName();
    private static final float ANIMATION_SPEED = 0.6f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_scared_0, R.mipmap.tino_scared_1,
            R.mipmap.tino_scared_2, R.mipmap.tino_scared_1,
    };

    private final Player player;

    public LeftScaredBehavior(@NonNull Player player) {
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

    private float moveLeft(float elapsedTimeSec) {
        float oldX = player.getPositionX();
        GameCamera mainCamera = DrawPipeline.getInstance().getMainCamera();
        if (mainCamera == null) {
            Log.w(TAG, "::moveLeft >> DrawPipeline에 카메라가 설정되어 있지 않습니다!");
            return oldX;
        }

        Projection projection = mainCamera.getProjection();
        if (projection == null) {
            Log.w(TAG, "::moveLeft >> 카메라에 Projection이 설정되어 있지 않습니다!");
            return oldX;
        }

        float newX = oldX;
        newX = newX - Player.SPEED * elapsedTimeSec;
        float halfTinoWidth = 0.5f * Tino.WIDTH;
        float leftSide = newX - halfTinoWidth;
        if (leftSide <= projection.left) {
            float diff = projection.left - leftSide;
            newX = projection.left + halfTinoWidth + diff;
            player.turnBehavior();
        }

        return newX;
    }

    private float falldown(float elapsedTimeSec) {
        float oldDistance = player.getDistance();
        float maxDurability = player.getMaxParachuteDurability();
        float currDurability = player.getCurrParachuteDurability();
        if (currDurability <= 0.0f) {
            player.setCurrDownSpeed(Player.MAX_DOWN_SPEED);
            player.downcastBehavior();
        }

        float percent = 1.0f - currDurability / maxDurability;
        float speed = Player.MIN_DOWN_SPEED + (Player.MAX_DOWN_SPEED - Player.MIN_DOWN_SPEED) * percent;
        Log.d(TAG, "::falldown >> 현재 낙하 속도:" + speed);
        return oldDistance + speed * elapsedTimeSec;
    }

    @Override
    public void onTouchEvent(@NonNull MotionEvent e) {
        super.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            player.turnBehavior();
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        float newX = moveLeft(elapsedTimeSec);
        float newDistance = falldown(elapsedTimeSec);
        player.updatePlayer(newX, newDistance);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }
}
