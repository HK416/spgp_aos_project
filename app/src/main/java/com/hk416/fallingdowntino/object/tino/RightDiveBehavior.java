package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.items.ItemObject;
import com.hk416.fallingdowntino.object.land.Tile;
import com.hk416.fallingdowntino.scene.FinishGameScene;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteClipAnimeObject;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Vector;

public class RightDiveBehavior extends SpriteObject {
    private static final String TAG = RightDiveBehavior.class.getSimpleName();
    private static final int BITMAP_RES_ID = R.mipmap.tino_dive;

    private final Player player;

    public RightDiveBehavior(@NonNull Player player) {
        super(BITMAP_RES_ID, Tino.WIDTH, Tino.HEIGHT, true, false);
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

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        float newDistance = falldown(elapsedTimeSec);
        player.updatePlayer(player.getPositionX(), newDistance);
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
            case Spanner:
                break;
            case Like: player.addLikeCount();
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
            Vector position = player.getWorldPosition();
            SpriteClipAnimeObject player = new SpriteClipAnimeObject.Builder()
                    .setX(position.x)
                    .setY(position.y)
                    .setRepeat(false)
                    .addClips(0.1f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_crash_0, true, false)
                    .addClips(0.1f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_crash_1, true, false)
                    .addClips(1.0f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_crash_2, true, false)
                    .addClips(1.0f, Tino.WIDTH, Tino.HEIGHT, R.mipmap.tino_crash_3, true, false)
                    .build();
            SceneManager.getInstance().cmdChangeScene(
                    new FinishGameScene(
                            player.getDuration(),
                            this.player.getDistance(),
                            this.player.getLikeCount(),
                            player,
                            tile
                    )
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
