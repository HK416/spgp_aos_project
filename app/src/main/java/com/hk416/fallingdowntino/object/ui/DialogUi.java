package com.hk416.fallingdowntino.object.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.hk416.framework.object.UiObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Viewport;

public final class DialogUi extends UiObject {
    private static final Anchor anchor = new Anchor(
            0.1f, 0.05f, 0.65f, 0.95f
    );

    private final Paint drawPaint = new Paint();
    private final RectF drawArea = new RectF();
    private final float rx;
    private final float ry;

    public DialogUi(int bgColor, float rx, float ry) {
        super(anchor);
        this.rx = rx;
        this.ry = ry;
        drawPaint.setColor(bgColor);
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
