package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Vector;

public class TileObject extends SpriteObject {

    protected final Vector tileSize;

    public TileObject(
            int bitmapResId,
            float width,
            float height,
            float tileWidth,
            float tileHeight,
            boolean flipX,
            boolean flipY
    ) {
        super(bitmapResId, width, height, flipX, flipY);
        this.tileSize = new Vector(tileWidth, tileHeight, 0.0f);
    }

    public TileObject (
            int bitmapResId,
            float x,
            float y,
            float width,
            float height,
            float tileWidth,
            float tileHeight,
            boolean flipX,
            boolean flipY
    ) {
        super(bitmapResId, x, y, width, height, flipX, flipY);
        this.tileSize = new Vector(tileWidth, tileHeight, 0.0f);
    }

    private Pair<Pair<Float, Float>, Integer> getStartX(float minPosX, float maxPosX) {
        float width = maxPosX - minPosX;
        float centerX = minPosX + 0.5f * width;
        int numTiles = (int)Math.ceil(size.x / tileSize.x);
        float tileWidth = (width / size.x) * tileSize.x;
        return Pair.create(Pair.create(centerX - 0.5f * tileWidth * numTiles, tileWidth), numTiles);
    }

    private Pair<Pair<Float, Float>, Integer> getStartY(float minPosY, float maxPosY) {
        float height = maxPosY - minPosY;
        float centerY = minPosY + 0.5f * height;
        int numTiles = (int)Math.ceil(size.y / tileSize.y);
        float tileHeight = (height / size.y) * tileSize.y;
        return Pair.create(Pair.create(centerY - 0.5f * tileHeight * numTiles, tileHeight), numTiles);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (sibling != null) {
            sibling.onDraw(canvas);
        }

        if (child != null) {
            child.onDraw(canvas);
        }

        if (BuildConfig.DEBUG && aabb != null) {
            aabb.onDraw(canvas);
        }

        Vector minimum = getWorldPosition().postSub(getSize().postMul(0.5f));
        Vector maximum = getWorldPosition().postAdd(getSize().postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null) {
            float top = Math.min(minPos.y, maxPos.y);
            float left = Math.min(minPos.x, maxPos.x);
            float bottom = Math.max(minPos.y, maxPos.y);
            float right = Math.max(minPos.x, maxPos.x);

            Pair<Pair<Float, Float>, Integer> horizontal = getStartX(left, right);
            Pair<Pair<Float, Float>, Integer> vertical = getStartY(top, bottom);

            float x = horizontal.first.first;
            float sx = horizontal.first.second;
            int cntX = horizontal.second;
            float y = vertical.first.first;
            float sy = vertical.first.second;
            int cntY = vertical.second;

            canvas.save();
            canvas.clipRect(left, top, right, bottom);
            for (int i = 0 ; i < cntY; i++) {
                for (int j = 0; j < cntX; j++) {
                    drawScreenArea.top = Math.min(y + sy * i, y + sy * (i + 1));
                    drawScreenArea.left = Math.min(x + sx * j, x + sx * (j + 1));
                    drawScreenArea.bottom = Math.max(y + sy * i, y + sy * (i + 1));
                    drawScreenArea.right = Math.max(x + sx * j, x + sx * (j + 1));
                    canvas.drawBitmap(bitmap, null, drawScreenArea, null);
                }
            }
            canvas.restore();

            if (BuildConfig.DEBUG) {
                drawScreenArea.top = top;
                drawScreenArea.left = left;
                drawScreenArea.bottom = bottom;
                drawScreenArea.right = right;
            }
        }
    }
}
