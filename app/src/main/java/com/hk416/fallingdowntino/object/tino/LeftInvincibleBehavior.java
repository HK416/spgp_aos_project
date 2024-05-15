package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.framework.object.SpriteAnimeObject;

public class LeftInvincibleBehavior extends SpriteAnimeObject {
    private static final String TAG = LeftInvincibleBehavior.class.getSimpleName();
    private static final float ANIMATION_SPEED = 0.6f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_invincible_0, R.mipmap.tino_invincible_1,
            R.mipmap.tino_invincible_2, R.mipmap.tino_invincible_1,
    };

    private final Player player;

    public LeftInvincibleBehavior(@NonNull Player player) {
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

    private float falldown(float elapsedTimeSec) {
        float oldDistance = player.getDistance();
        float currDownSpeed = player.getCurrDownSpeed();

        float newDownSpeed = currDownSpeed + 9.8f * elapsedTimeSec;
        newDownSpeed = Math.min(Player.MAX_DIVE_DOWN_SPEED, newDownSpeed);
        Log.d(TAG, "::falldown >> 현재 낙하 속도:" + newDownSpeed);
        player.setCurrDownSpeed(newDownSpeed);

        return oldDistance + currDownSpeed * elapsedTimeSec;
    }

    private void checkInvincibleTimer() {
        if (player.getInvincibleTimer() <= 0.0f) {
            player.setCurrDownSpeed(0.0f);
            player.setBehaviors(
                    Tino.Behavior.LeftDefault,
                    Parachute.Behavior.LeftDefault
            );
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        float newDistance = falldown(elapsedTimeSec);
        checkInvincibleTimer();
        player.updatePlayer(player.getPositionX(), newDistance);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }
}
