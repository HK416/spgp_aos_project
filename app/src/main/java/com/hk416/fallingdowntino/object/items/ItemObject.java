package com.hk416.fallingdowntino.object.items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.collide.BoundingBox;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteObject;
import com.hk416.framework.transform.Vector;

import java.util.HashMap;

public final class ItemObject extends GameObject {
    public enum Type { Energy, Like, Spanner }

    private final HashMap<Type, SpriteObject> SPRITES = new HashMap<Type, SpriteObject>() {{
        put(Type.Energy, new EnergyItem());
        put(Type.Like, new LikeItem());
        put(Type.Spanner, new SpannerItem());
    }};

    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.0f;

    private final Player player;
    private float oldDistance;

    private Type type = null;

    public ItemObject(@NonNull Player player) {
        super();
        this.oldDistance = player.getDistance();
        this.player = player;
        this.aabb = new BoundingBox(0.0f, 0.0f, WIDTH, HEIGHT);
    }

    public void reset() {
        type = null;
        child = null;
        sibling = null;
        oldDistance = player.getDistance();
    }

    public void setItemType(@NonNull Type type) {
        this.type = type;
        SpriteObject spriteObject = SPRITES.get(type);
        if (spriteObject == null) {
            throw new RuntimeException("해당 유형의 스프라이트 오브젝트가 존재하지 않습니다! (type" + type + ")");
        }
        setChild(spriteObject);
    }

    @Nullable
    public Type getItemType() {
        return type;
    }

    private float moveUp(float oldY) {
        float currDistance = player.getDistance();
        float delta = currDistance - oldDistance;
        oldDistance = currDistance;
        return oldY + delta;
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        super.onUpdate(elapsedTimeSec);
        Vector newPosition = getWorldPosition();
        newPosition.y = moveUp(newPosition.y);
        setPosition(newPosition.x, newPosition.y);
    }
}
