package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Viewport;

public class UiRectObject extends UiObject {
    protected final Paint drawPaint = new Paint();
    protected final RectF drawArea = new RectF();
    protected final float rx;
    protected final float ry;

    public UiRectObject(int color, float rx, float ry) {
        super();
        this.rx = rx;
        this.ry = ry;
        drawPaint.setColor(color);
    }

    public UiRectObject(
            int color,
            float rx,
            float ry,
            @NonNull Anchor anchor
    ) {
        super(anchor);
        this.rx = rx;
        this.ry = ry;
        drawPaint.setColor(color);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;
        canvas.drawRoundRect(drawArea, rx, ry, drawPaint);
    }
}
