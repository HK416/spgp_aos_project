package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Vector;

public class TileObject extends SpriteObject {
    public enum Align {
        TopLeft, TopMid, TopRight,
        MidLeft, Center, MidRight,
        BottomLeft, BottomMid, BottomRight,
    }

    protected final Vector tileSize;
    protected final Align align;

    public TileObject(
            int bitmapResId,
            float width,
            float height,
            float tileWidth,
            float tileHeight,
            boolean flipX,
            boolean flipY,
            Align align
    ) {
        super(bitmapResId, width, height, flipX, flipY);
        this.tileSize = new Vector(tileWidth, tileHeight, 0.0f);
        this.align = (align == null) ? Align.TopLeft : align;
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
            boolean flipY,
            Align align
    ) {
        super(bitmapResId, x, y, width, height, flipX, flipY);
        this.tileSize = new Vector(tileWidth, tileHeight, 0.0f);
        this.align = (align == null) ? Align.TopLeft : align;
    }

    private Pair<Float, Float> getStartX(@NonNull PointF minPos, @NonNull PointF maxPos) {
        float ratioWidth = size.x / tileSize.x;
        float tileWidth = (maxPos.x - minPos.x) / ratioWidth;
        switch (align) {
            case TopLeft:
            case MidLeft:
            case BottomLeft:
                return Pair.create(minPos.x, tileWidth);
            case TopMid:
            case Center:
            case BottomMid:
                return Pair.create(
                        0.5f * (minPos.x + maxPos.x)
                        - tileWidth * (int)Math.ceil(0.5f * ratioWidth),
                        tileWidth
                );
            case TopRight:
            case MidRight:
            case BottomRight:
                return Pair.create(maxPos.x, -tileWidth);
            default:
                return Pair.create(0.0f, 0.0f);
        }
    }

    private Pair<Float, Float> getStartY(@NonNull PointF minPos, @NonNull PointF maxPos) {
        float ratioHeight = size.y / tileSize.y;
        float tileHeight = (maxPos.y - minPos.y) / ratioHeight;
        switch (align) {
            case TopLeft:
            case TopMid:
            case TopRight:
                return Pair.create(maxPos.y, -tileHeight);
            case MidLeft:
            case Center:
            case MidRight:
                return Pair.create(
                        0.5f * (minPos.y + maxPos.y)
                        - tileHeight * (int)Math.ceil(0.5f * ratioHeight),
                        tileHeight
                );
            case BottomLeft:
            case BottomMid:
            case BottomRight:
                return Pair.create(minPos.y, tileHeight);
            default:
                return Pair.create(0.0f, 0.0f);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Vector minimum = getWorldPosition().postSub(getSize().postMul(0.5f));
        Vector maximum = getWorldPosition().postAdd(getSize().postMul(0.5f));

        PointF minPos = DrawPipeline.getInstance().toScreenCoord(minimum);
        PointF maxPos = DrawPipeline.getInstance().toScreenCoord(maximum);

        if (minPos != null && maxPos != null) {
            Pair<Float, Float> v = getStartX(minPos, maxPos);
            Pair<Float, Float> h = getStartY(minPos, maxPos);
            float x = v.first;
            float sx = v.second;
            float y = h.first;
            float sy = h.second;

            canvas.save();
            canvas.clipRect(minPos.x, maxPos.y, maxPos.x, minPos.y);
            for (int i = 0 ; i < (int)Math.ceil(size.y / tileSize.y); i++) {
                for (int j = 0; j < (int)Math.ceil(size.x / tileSize.x); j++) {
                    drawScreenArea.top = Math.min(y + sy * i, y + sy * (i + 1));
                    drawScreenArea.left = Math.min(x + sx * j, x + sx * (j + 1));
                    drawScreenArea.bottom = Math.max(y + sy * i, y + sy * (i + 1));
                    drawScreenArea.right = Math.max(x + sx * j, x + sx * (j + 1));
                    canvas.drawBitmap(bitmap, null, drawScreenArea, null);
                }
            }
            canvas.restore();

            if (BuildConfig.DEBUG) {
                drawScreenArea.top = maxPos.y;
                drawScreenArea.left = minPos.x;
                drawScreenArea.bottom = minPos.y;
                drawScreenArea.right = maxPos.x;
            }
        }
    }
}
