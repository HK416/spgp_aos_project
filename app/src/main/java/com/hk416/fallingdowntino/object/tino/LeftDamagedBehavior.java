package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.items.ItemObject;
import com.hk416.fallingdowntino.object.items.SpannerItem;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;

public class LeftDamagedBehavior extends SpriteAnimeObject {
    private static final String TAG = LeftDamagedBehavior.class.getSimpleName();
    private static final float ANIMATION_SPEED = 0.8f;
    private static final int[] BITMAP_RES_IDS = new int[] {
            R.mipmap.tino_damaged_0, R.mipmap.tino_damaged_1,
            R.mipmap.tino_damaged_2, R.mipmap.tino_damaged_1,
    };

    private final Paint darknessPaint;
    private final Player player;

    public LeftDamagedBehavior(@NonNull Player player) {
        super(
                BITMAP_RES_IDS,
                Tino.WIDTH,
                Tino.HEIGHT,
                ANIMATION_SPEED,
                false,
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
            player.setBehaviors(
                    Tino.Behavior.LeftDive,
                    null
            );
        }

        float percent = 1.0f - currDurability / maxDurability;
        float speed = Player.MIN_DOWN_SPEED + (Player.MAX_DOWN_SPEED - Player.MIN_DOWN_SPEED) * percent;
        Log.d(TAG, "::falldown >> 현재 낙하 속도:" + speed);
        return oldDistance + speed * elapsedTimeSec;
    }

    private void checkInvincibleTimer() {
        float currDurability = player.getCurrParachuteDurability();
        if (player.getInvincibleTimer() <= 0.0f && currDurability > 0.0f) {
            if (currDurability <= Tino.SCARED_POINT) {
                player.setBehaviors(
                        Tino.Behavior.LeftScared,
                        Parachute.Behavior.LeftScratched
                );
            } else {
                player.setBehaviors(
                        Tino.Behavior.LeftDefault,
                        Parachute.Behavior.LeftDefault
                );
            }
        }
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
        checkInvincibleTimer();
        player.updatePlayer(newX, newDistance);
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

    @Override
    public void onCollide(@NonNull GameObject object) {
        if (object instanceof ItemObject) {
            ItemObject itemObject = (ItemObject)object;
            ItemObject.Type type = itemObject.getItemType();
            if (type == null) {
                throw new NullPointerException("충돌이 발생한 아이템의 유형은 null이 될 수 없습니다!");
            }

            switch (type) {
                case Energy:
                    player.setInvincibleTimer(Tino.INVINCIBLE_DURATION);
                    player.setCurrDownSpeed(Player.MAX_DOWN_SPEED);
                    player.setBehaviors(
                            Tino.Behavior.LeftInvincible,
                            null
                    );
                    break;
                case Spanner:
                    player.addParachuteDurability(SpannerItem.DURABILITY);
                    break;
                case Like:
                    player.addLikeCount();
                    break;
                default:
                    throw new RuntimeException("해당 유형의 아이템에 대해 행동이 구현되어 있지 않습니다! (type:" + type + ")");
            }
        }
    }
}
