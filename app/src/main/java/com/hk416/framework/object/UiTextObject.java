package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;
import com.hk416.framework.transform.Viewport;

public class UiTextObject extends UiObject {
    protected String text;
    protected final Paint paint = new Paint();
    protected final RectF drawArea = new RectF();

    public UiTextObject(@Nullable String text) {
        super();
        this.text = text;
    }

    public UiTextObject(
            @Nullable String text,
            @NonNull Anchor anchor
    ) {
        super(anchor);
        this.text = text;
    }

    public UiTextObject(
            @Nullable String text,
            @NonNull Margin margin
    ) {
        super(margin);
        this.text = text;
    }

    public UiTextObject(
            @Nullable String text,
            @NonNull Anchor anchor,
            @NonNull Margin margin
    ) {
        super(anchor, margin);
        this.text = text;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    private void setTextSizeForWidth(float desiredWidth) {
        if (text != null) {
            Rect bounds = new Rect();
            final float testTextSize = 48.0f;

            paint.setTextSize(testTextSize);
            paint.getTextBounds(text.trim(), 0, text.trim().length(), bounds);
            float desiredTextSize = testTextSize * desiredWidth / bounds.width();

            paint.setTextSize(desiredTextSize);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;

        if (text != null) {
            setTextSizeForWidth(drawArea.width());
            canvas.drawText(text, drawArea.left, drawArea.top + paint.getTextSize(), paint);
        }
    }
}
