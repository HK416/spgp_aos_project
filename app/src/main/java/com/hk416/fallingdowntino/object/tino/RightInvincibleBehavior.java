package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Vector;

public class RightInvincibleBehavior extends SpriteAnimeObject {
    private static final String TAG = RightInvincibleBehavior.class.getSimpleName();
    private static final float ANIMATION_SPEED = 0.4f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_invincible_0, R.mipmap.tino_invincible_1,
            R.mipmap.tino_invincible_2, R.mipmap.tino_invincible_1,
    };

    private final Player player;
    private final Paint darknessPaint;


    public RightInvincibleBehavior(@NonNull Player player) {
        super(
                BITMAP_RES_IDS,
                Tino.WIDTH,
                Tino.HEIGHT,
                ANIMATION_SPEED,
                true,
                false
        );
        this.player = player;
        this.darknessPaint = new Paint();
        float[] array = {
                1,0,0,0,-127,
                0,1,0,0,-127,
                0,0,1,0,-127,
                0,0,0,1,0
        };
        ColorMatrix cm = new ColorMatrix(array);
        darknessPaint.setColorFilter(new ColorMatrixColorFilter(cm));
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
                    Tino.Behavior.RightDefault,
                    Parachute.Behavior.RightDefault
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
        Vector minimum = getWorldPosition().postSub(getSize().postMul(0.5f));
        Vector maximum = getWorldPosition().postAdd(getSize().postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null) {
            drawScreenArea.top = maxPos.y;
            drawScreenArea.left = minPos.x;
            drawScreenArea.bottom = minPos.y;
            drawScreenArea.right = maxPos.x;

            float invincibleTimer = player.getInvincibleTimer();
            boolean drakness = (int)Math.floor(invincibleTimer * 6.0f) % 2 == 0;
            if (invincibleTimer <= 3.0f && drakness) {
                canvas.drawBitmap(bitmaps[currBitmapIndex], null, drawScreenArea, darknessPaint);
            } else {
                canvas.drawBitmap(bitmaps[currBitmapIndex], null, drawScreenArea, null);
            }
        }

        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }
}
