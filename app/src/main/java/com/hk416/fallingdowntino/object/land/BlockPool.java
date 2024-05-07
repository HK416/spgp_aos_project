package com.hk416.fallingdowntino.object.land;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.TileObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Viewport;

import java.util.ArrayDeque;

public final class BlockPool extends GameObject {
    public enum Types { Static, Dynamic };

    private static final String TAG = BlockPool.class.getSimpleName();
    public static final int CAPACITY = 16;
    public static final float MIN_RANGE = -32.0f;
    public static final float MAX_RANGE = 32.0f;
    public static final float SPAWN_INTERVAL = 24.0f;
    public static final float STATIC_INTERVAL = 4.2f;
    public static final float HALF_STATIC_INTERVAL = 0.5f * STATIC_INTERVAL;
    public static final float MIN_DYNAMIC_WIDTH = Tile.TILE_WIDTH;
    public static final float MAX_DYNAMIC_WIDTH = Tile.TILE_WIDTH * 3;

    private static Paint debugPaint = null;

    private final Player player;
    private float prevSpawnDistance;
    private final ArrayDeque<Block> activeBlocks;
    private final ArrayDeque<Block> inactiveBlocks;

    public BlockPool(@NonNull Player player) {
        createDebugPaint();
        this.player = player;
        this.prevSpawnDistance = -SPAWN_INTERVAL;
        activeBlocks = new ArrayDeque<>(CAPACITY);
        inactiveBlocks = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveBlocks.add(new Block(player, getCurrProjection()));
        }
    }

    private void createDebugPaint() {
        if (BuildConfig.DEBUG && debugPaint == null) {
            debugPaint = new Paint();
            debugPaint.setColor(Color.rgb(51, 204, 51));
            debugPaint.setTextSize(48.0f);
        }
    }

    @NonNull
    private Projection getCurrProjection() {
        GameCamera mainCamera = DrawPipeline.getInstance().getMainCamera();
        if (mainCamera == null) {
            throw new NullPointerException("mainCamere가 설정되어 있지 않습니다!");
        }

        Projection projection = mainCamera.getProjection();
        if (projection == null) {
            throw new NullPointerException("mainCamera에 Projection이 설정되어 있지 않습니다!");
        }

        return projection;
    }

    private void spawnBlock() {
        float currDistance = player.getDistance();
        if (prevSpawnDistance + SPAWN_INTERVAL <= currDistance) {
            float diff = currDistance - (prevSpawnDistance + SPAWN_INTERVAL);
            prevSpawnDistance += SPAWN_INTERVAL;
            Projection projection = getCurrProjection();
            Block block = null;
            if (inactiveBlocks.isEmpty()) {
                block = new Block(player, projection);
            } else {
                block = inactiveBlocks.pop();
                block.setRange(projection);
                block.reset();
            }

            float posX = 0.0f;
            float posY = MIN_RANGE + diff;
            int type = (int)Math.round(Math.random());
            if (type == Types.Static.ordinal()) {
                posX = projection.left + HALF_STATIC_INTERVAL
                        + (projection.getWidth() - STATIC_INTERVAL) * (float)Math.random();
                block.setPosition(posX, posY);
                block.setVelocityX(0.0f);

                float leftWidth = (posX - HALF_STATIC_INTERVAL) - projection.left;
                float rightWidth = projection.right - (posX + HALF_STATIC_INTERVAL);

                Tile left = new Tile(
                        -HALF_STATIC_INTERVAL - 0.5f * leftWidth,
                        0.0f,
                        leftWidth,
                        Tile.TILE_HEIGHT
                );
                Tile right = new Tile(
                        HALF_STATIC_INTERVAL + 0.5f * rightWidth,
                        0.0f,
                        rightWidth,
                        Tile.TILE_HEIGHT
                );

                left.setSibling(right);
                block.setChild(left);
                block.updateTransform(null);
                Log.d(TAG, "::spawnBlock >> 고정된 블록 추가 left:" + leftWidth + ", right:" + rightWidth);
            } else {
                float width = MIN_DYNAMIC_WIDTH
                        + (MAX_DYNAMIC_WIDTH - MIN_DYNAMIC_WIDTH)
                        * (float)Math.random();
                posX = projection.left + 0.5f * width
                        + (projection.getWidth() - width) * (float)Math.random();
                block.setPosition(posX, posY);
                block.setVelocityX(Block.MIN_SPEED
                        + (Block.MAX_SPEED - Block.MIN_SPEED)
                        * (float)Math.random());
                Tile child = new Tile(0.0f, 0.0f, width, Tile.TILE_HEIGHT);
                block.setChild(child);
                block.updateTransform(null);
                Log.d(TAG, "::spawnBlock >> 움직이는 블록 추가 width:" + width);
            }
            activeBlocks.add(block);
        }
    }

    private void retainBlocks() {
        while (!activeBlocks.isEmpty()) {
            Block firstBlock = activeBlocks.peek();
            if (firstBlock == null) {
                break;
            }

            if (firstBlock.getWorldPosition().y >= MAX_RANGE) {
                Log.d(TAG, "::retainBlocks >> 블록 제거 (Block:" + firstBlock + ")");
                firstBlock = activeBlocks.pop();
                inactiveBlocks.add(firstBlock);
            } else {
                break;
            }
        }
    }

    private void updateAcitveBlocks(float elapsedTimeSec) {
        for (Block block: activeBlocks) {
            block.onUpdate(elapsedTimeSec);
        }
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        updateAcitveBlocks(elapsedTimeSec);
        spawnBlock();
        retainBlocks();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        for (Block block : activeBlocks) {
            block.onDraw(canvas);
        }

        if (BuildConfig.DEBUG) {
            Viewport viewport = DrawPipeline.getInstance().getViewport();
            canvas.drawText(
                    String.format("Active Blocks:%d", activeBlocks.size()),
                    viewport.left,
                    viewport.bottom,
                    debugPaint
            );
        }
    }
}
