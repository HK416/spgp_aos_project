package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;
import com.hk416.framework.transform.Viewport;

public class UiImageObject extends UiObject {
    protected final RectF drawArea = new RectF();
    protected Bitmap bitmap;

    public UiImageObject(int bitmapResId) {
        super();
        init(bitmapResId);
    }

    public UiImageObject(
            int bitmapResId,
            @NonNull Anchor anchor
    ) {
        super(anchor);
        init(bitmapResId);
    }

    public UiImageObject(
            int bitmapResId,
            @NonNull Margin margin
    ) {
        super(margin);
        init(bitmapResId);
    }

    public UiImageObject(
            int bitmapResId,
            @NonNull Anchor anchor,
            @NonNull Margin margin
    ) {
        super(anchor, margin);
        init(bitmapResId);
    }

    public void init(int bitmapResId) {
        this.bitmap = BitmapPool.getInstance().get(bitmapResId);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, drawArea,null);
        }
    }
}
