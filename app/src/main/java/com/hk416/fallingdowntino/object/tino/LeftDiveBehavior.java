package com.hk416.fallingdowntino.object.tino;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.items.ItemObject;
import com.hk416.fallingdowntino.object.items.SpannerItem;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;

public class LeftDiveBehavior extends SpriteObject {
    private static final String TAG = LeftDiveBehavior.class.getSimpleName();
    private static final int BITMAP_RES_ID = R.mipmap.tino_dive;

    private final Player player;

    public LeftDiveBehavior(@NonNull Player player) {
        super(BITMAP_RES_ID, Tino.WIDTH, Tino.HEIGHT, false, false);
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
                case Spanner:
                    break;
                case Like: player.addLikeCount();
                    break;
                default:
                    throw new RuntimeException("해당 유형의 아이템에 대해 행동이 구현되어 있지 않습니다! (type:" + type + ")");
            }
        }
    }
}
