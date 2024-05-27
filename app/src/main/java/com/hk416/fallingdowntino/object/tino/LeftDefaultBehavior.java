package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.items.ItemObject;
import com.hk416.fallingdowntino.object.items.SpannerItem;
import com.hk416.fallingdowntino.object.land.Tile;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.fallingdowntino.scene.FinishGameScene;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.object.SpriteClipAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;

public class LeftDefaultBehavior extends SpriteAnimeObject {
    private static final String TAG = LeftDefaultBehavior.class.getSimpleName();
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
        if (currDurability <= Tino.SCARED_POINT) {
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
        float newDistance =falldown(elapsedTimeSec);
        player.updatePlayer(newX, newDistance);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (BuildConfig.DEBUG) {
            canvas.drawRect(drawScreenArea, Tino.debugColor);
        }
    }

    private void handleItemCollision(@NonNull ItemObject itemObject) {
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
                player.setBehaviorTimer(Tino.HAPPY_DUARTION);
                player.setBehaviors(
                        Tino.Behavior.LeftHappy,
                        Parachute.Behavior.LeftDefault
                );
                break;
            case Like:
                player.addLikeCount();
                player.setBehaviorTimer(Tino.HAPPY_DUARTION);
                player.setBehaviors(
                        Tino.Behavior.LeftHappy,
                        Parachute.Behavior.LeftDefault
                );
                break;
            default:
                throw new RuntimeException("해당 유형의 아이템에 대해 행동이 구현되어 있지 않습니다! (type:" + type + ")");
        }
    }

    private void handleBlockCollision(@NonNull Tile tile) {
        BoundingBox tileBox = tile.getBoundingBox();
        Vector tileCenter = tileBox.getWorldPosition();
        Vector tileSize = tileBox.getSize();
        float tileLeftSide = tileCenter.x - 0.5f * tileSize.x;
        float tileRightSide = tileCenter.x + 0.5f * tileSize.x;

        BoundingBox thisBox = player.getBoundingBox();
        Vector thisCenter = thisBox.getWorldPosition();
        Vector thisSize = thisBox.getSize();
        float thisLeftSide = thisCenter.x - 0.5f * thisSize.x;
        float thisRightSide = thisCenter.x + 0.5f * thisSize.x;
        float thisLowerSide = thisCenter.y - 0.5f * thisSize.y;

        if (tileCenter.y < thisLowerSide && (tileLeftSide <= thisLeftSide && thisRightSide <= tileRightSide)) {
            Log.d(TAG, "::handleBlockCollision >> 게임 종료!");
            Vector position = player.getWorldPosition();
            SpriteClipAnimeObject player = new SpriteClipAnimeObject.Builder()
                    .setX(position.x)
                    .setY(position.y)
                    .setRepeat(false)
                    .addClips(1.0f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_landing_0, false, false)
                    .addClips(1.0f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_landing_1, false, false)
                    .build();
            SceneManager.getInstance().cmdChangeScene(
                    new FinishGameScene(player.getDuration(), player, tile)
            );
        } else {
            player.addParachuteDurability(Player.PARACHUTE_DAMAGE);
            player.setInvincibleTimer(Player.DAMAGE_DURATION);
            player.setBehaviors(
                    Tino.Behavior.LeftDamaged,
                    Parachute.Behavior.LeftScratched
            );
        }
    }

    @Override
    public void onCollide(@NonNull GameObject object) {
        if (object instanceof ItemObject) {
            handleItemCollision((ItemObject)object);
        } else if (object instanceof Tile) {
            handleBlockCollision((Tile)object);
        }
    }
}
