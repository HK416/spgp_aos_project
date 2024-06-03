package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.framework.collide.IGameCollider;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;
import com.hk416.framework.transform.Viewport;

import java.util.ArrayList;

public class UiButtonObject extends UiObject {
    enum State { Released, Pressed }

    private static final String TAG = UiButtonObject.class.getSimpleName();

    protected final ArrayList<Bitmap> btnImages = new ArrayList<>();
    protected final RectF drawArea = new RectF();

    protected State state = State.Released;

    public UiButtonObject(
            int releasedResId,
            int pressedResId,
            @NonNull Anchor anchor
    ) {
        super(anchor);
        init(releasedResId, pressedResId);
    }

    public UiButtonObject(
            int releasedResId,
            int pressedResId,
            @NonNull Anchor anchor,
            @NonNull Margin margin
    ) {
        super(anchor, margin);
        init(releasedResId, pressedResId);
    }

    private void init(int releasedResId, int pressedResId) {
        BitmapPool pool = BitmapPool.getInstance();
        btnImages.add(pool.get(releasedResId));
        btnImages.add(pool.get(pressedResId));
    }

    private void buildDrawArea(Bitmap bitmap) {
        Viewport viewport = DrawPipeline.getInstance().getViewport();

        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;

        if (bitmap != null) {
            float desiredBitmapSize = bitmap.getHeight() * drawArea.width() / bitmap.getWidth();
            float centerY = drawArea.centerY();
            drawArea.top = centerY - 0.5f * desiredBitmapSize;
            drawArea.bottom = centerY + 0.5f * desiredBitmapSize;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = btnImages.get(state.ordinal());
        if (bitmap != null) {
            buildDrawArea(bitmap);
            canvas.drawBitmap(bitmap, null, drawArea, null);
        }
    }
}
