package com.hk416.fallingdowntino.object.land;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.GameObject;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;

public final class BlockPool extends GameObject {
    private static final String TAG = BlockPool.class.getSimpleName();

    public static final int CAPACITY = 16;
    public static final float SPAWN_POS = -32.0f;
    public static final float RETAIN_POS = 32.0f;
    public static final float SPAWN_INTERVAL = 24.0f;

    private final float minPosX;
    private final float maxPosX;

    private final ArrayDeque<Block> activeBlocks;
    private final ArrayDeque<Block> inactiveBlocks;

    private final ArrayDeque<Tile> activeTiles;
    private final ArrayDeque<Tile> inactiveTiles;

    private final Player player;
    private float prevSpawnDistance;

    public BlockPool(
            float minPosX,
            float maxPosX,
            float initSpawnDistance,
            @NonNull Player player
    ) {
        checkParameters(minPosX, maxPosX, initSpawnDistance, player);

        this.minPosX = minPosX;
        this.maxPosX = maxPosX;

        this.activeBlocks = new ArrayDeque<>(CAPACITY);
        this.inactiveBlocks = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveBlocks.add(new Block(minPosX, maxPosX, player));
        }

        this.activeTiles = new ArrayDeque<>(CAPACITY);
        this.inactiveTiles = new ArrayDeque<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            inactiveTiles.add(new Tile(0.0f, 0.0f, 0.0f, 0.0f));
        }

        this.player = player;
        this.prevSpawnDistance = initSpawnDistance;
    }

    private void checkParameters(
            float minPosX,
            float maxPosX,
            float _initSpawnDistance,
            Player player
    ) {
        if (maxPosX <= minPosX) {
            throw new InvalidParameterException("주어진 maxSpawnX는 minSpawnX 보다 커야 합니다!");
        }

        if (maxPosX - minPosX < Block.MIN_WIDTH) {
            throw new InvalidParameterException("주어진 범위의 길이는 `Block.MIN_WIDTH`보다 커야 합니다!");
        }

        if (player == null) {
            throw new InvalidParameterException("주어진 Player 객체는 null이 될 수 없습니다!");
        }
    }

    @NonNull
    private Block getBlockObject() {
        if (inactiveBlocks.isEmpty()) {
            return new Block(minPosX, maxPosX, player);
        } else {
            Block block = inactiveBlocks.pop();
            block.reset();
            return block;
        }
    }

    private Tile getTileObject(float x, float width) {
        if (inactiveTiles.isEmpty()) {
            return new Tile(x, 0.0f, width, Tile.TILE_HEIGHT);
        } else {
            Tile tile = inactiveTiles.pop();
            activeTiles.add(tile);
            tile.setPosition(x, 0.0f);
            tile.setSize(width, Tile.TILE_HEIGHT);
            return tile;
        }
    }

    private Block.Type getRandomBlockType() {
        Block.Type[] types = Block.Type.values();
        return types[(int)Math.floor(Math.random() * types.length)];
    }

    private float getRandomPositionX(float minBlockWidth) {
        final float halfBlockWidth = 0.5f * minBlockWidth;
        float begX = minPosX + halfBlockWidth;
        float endX = maxPosX - halfBlockWidth;
        return begX + (float)Math.random() * (endX - begX);
    }

    private void onSpawnRandomBlock() {
        float currDistance = player.getDistance();
        if (prevSpawnDistance + SPAWN_INTERVAL > currDistance) {
            return;
        }

        float offsetY = currDistance - (prevSpawnDistance + SPAWN_INTERVAL);
        prevSpawnDistance += SPAWN_INTERVAL;

        Block block = getBlockObject();
        Block.Type type = getRandomBlockType();
        switch (type) {
            case Static: onInitStaticBlock(block, offsetY);
                break;
            case Dynamic: onInitDynamicBlock(block, offsetY);
                break;
            default:
                throw new RuntimeException("해당 유형의 Block 오브젝트가 존재하지 않습니다! (type:" + type + ")");
        }

        activeBlocks.add(block);
        Log.d(TAG, "::onSpawnRandomBlock >> 장애물 블록 추가 (type:" + type + ")");
    }

    private void onInitStaticBlock(@NonNull Block block, float offsetY) {
        float posX = getRandomPositionX(Block.STATIC_INTERVAL);
        float posY = SPAWN_POS + offsetY;

        float leftBlockWidth = (posX - Block.HALF_STATIC_INTERVAL) - minPosX;
        float leftBlockCenterX = (posX - Block.HALF_STATIC_INTERVAL) - 0.5f * leftBlockWidth;
        Tile leftBlockTile = getTileObject(leftBlockCenterX, leftBlockWidth);

        float rightBlockWidth = maxPosX - (posX + Block.HALF_STATIC_INTERVAL);
        float rightBlockCenterX = (posX + Block.HALF_STATIC_INTERVAL) + 0.5f * rightBlockWidth;
        Tile rightBlockTile = getTileObject(rightBlockCenterX, rightBlockWidth);

        leftBlockTile.setSibling(rightBlockTile);
        block.setChild(leftBlockTile);
        block.setPosition(0.0f, posY);
        block.setVelocityX(0.0f);
        block.setBlockType(Block.Type.Static);
    }

    private void onInitDynamicBlock(@NonNull Block block, float offsetY) {
        float posX = getRandomPositionX(Block.MIN_DYNAMIC_WIDTH);
        float posY = SPAWN_POS + offsetY;

        float blockWidth = Block.getRandomDynamicBlockWidth();
        float blockVelocityX = Block.getRandomDynamicBlockVelocityX();
        Tile tile = getTileObject(0.0f, blockWidth);

        block.setChild(tile);
        block.setPosition(posX, posY);
        block.setVelocityX(blockVelocityX);
        block.setBlockType(Block.Type.Dynamic);
    }

    private void onRetainBlocks() {
        while (!activeBlocks.isEmpty()) {
            Block firstBlock = activeBlocks.peek();
            if (firstBlock == null) {
                break;
            }

            if (firstBlock.getWorldPosition().y >= RETAIN_POS) {
                Log.d(TAG, "::onRetainBlocks >> 블록 제거");
                firstBlock = activeBlocks.pop();
                Block.Type type = firstBlock.getBlockType();
                if (type == null) {
                    throw new NullPointerException("활성화 상태 블럭의 유형은 null이 될 수 없습니다!");
                }

                switch (type) {
                    case Static:
                        Tile rightTile = (Tile)firstBlock.getChild();
                        if (rightTile == null) {
                            throw new NullPointerException("활성화 상태 Static 블럭의 오른쪽은 null이 될 수 없습니다!");
                        }

                        Tile leftTile = (Tile)rightTile.getSibling();
                        if (leftTile == null) {
                            throw new NullPointerException("활성화 상태 Static 블럭의 왼쪽은 null이 될 수 없습니다!");
                        }

                        onRetainTile(rightTile);
                        onRetainTile(leftTile);
                        break;
                    case Dynamic:
                        Tile tile = (Tile)firstBlock.getChild();
                        if (tile == null) {
                            throw new NullPointerException("활성화 상태 Dynamic 블럭의 타일은 null이 될 수 없습니다!");
                        }

                        onRetainTile(tile);
                        break;
                    default:
                        throw new RuntimeException("해당 유형의 Block 오브젝트가 존재하지 않습니다! (type:" + type + ")");
                }

                firstBlock.setChild(null);
                inactiveBlocks.add(firstBlock);
            } else {
                break;
            }
        }
    }

    private void onRetainTile(@NonNull Tile tile) {
        ArrayDeque<Tile> nextActiveTiles = new ArrayDeque<>(activeTiles.size());
        while (!activeTiles.isEmpty()) {
            Tile activeTile = activeTiles.pop();
            if (activeTile == null) {
                break;
            }

            if (activeTile == tile) {
                Log.d(TAG, "::onRetainTile >> 타일 제거");
                activeTile.setChild(null);
                activeTile.setSibling(null);
                inactiveTiles.add(activeTile);
            } else {
                nextActiveTiles.add(activeTile);
            }
        }
        activeTiles.addAll(nextActiveTiles);
    }

    private void onUpdateAcitveBlocks(float elapsedTimeSec) {
        for (Block block: activeBlocks) {
            block.onUpdate(elapsedTimeSec);
        }
    }

    private void onCheckCollision() {
        for (Block block : activeBlocks) {
            if (block.intersects(player)) {
                player.onCollide(block);
                break;
            }
        }
    }

    public int getNumActiveBlocks() {
        return activeBlocks.size();
    }

    @Override
    public void onUpdate(float elapsedTimeSec) {
        onUpdateAcitveBlocks(elapsedTimeSec);
        onCheckCollision();
        onSpawnRandomBlock();
        onRetainBlocks();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        for (Block block : activeBlocks) {
            block.onDraw(canvas);
        }
    }
}
