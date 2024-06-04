package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.fallingdowntino.GameView;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;
import com.hk416.framework.transform.Viewport;

public class UiTextObject extends UiObject {
    public enum Pivot { Vertical, Horizontal };

    protected final Paint drawPaint = new Paint();
    protected final RectF drawArea = new RectF();
    protected final Pivot pivot;

    protected String text;

    public UiTextObject(
            @Nullable String text,
            @NonNull Anchor anchor,
            @NonNull Pivot pivot
    ) {
        super(anchor);
        this.text = text;
        this.pivot = pivot;
    }

    public UiTextObject(
            int resId,
            @NonNull Anchor anchor,
            @NonNull Pivot pivot
    ) {
        super(anchor);
        this.text = GameView.getStringFromRes(resId);
        this.pivot = pivot;
    }

    public void setColor(int color) {
        drawPaint.setColor(color);
    }

    public void setStyle(Paint.Style style) {
        drawPaint.setStyle(style);
    }

    private void buildDrawArea() {
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;
    }

    private void drawTextBasedWidth(@NonNull Canvas canvas) {
        if (text == null) {
            return;
        }

        buildDrawArea();

        final Rect bounds = new Rect();
        float baseTextSize = 48.0f;

        drawPaint.setTextSize(baseTextSize);
        drawPaint.getTextBounds(text.trim(), 0, text.trim().length(), bounds);
        float desiredTextSize = baseTextSize * drawArea.width() / bounds.width();

        drawPaint.setTextSize(desiredTextSize);
        canvas.drawText(text.trim(), drawArea.left, drawArea.top + drawPaint.getTextSize(), drawPaint);
    }

    private void drawTextBasedHeight(@NonNull Canvas canvas) {
        if (text == null) {
            return;
        }

        buildDrawArea();

        final Rect bounds = new Rect();
        float baseTextSize = drawArea.height();

        drawPaint.setTextSize(baseTextSize);
        drawPaint.getTextBounds(text.trim(), 0, text.trim().length(), bounds);
        float left = drawArea.centerX() - 0.5f * bounds.width();

        canvas.drawText(text.trim(), left, drawArea.top + drawPaint.getTextSize(), drawPaint);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (pivot == Pivot.Horizontal) {
            drawTextBasedWidth(canvas);
        } else {
            drawTextBasedHeight(canvas);
        }
    }
}
