package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.Paint;
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

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;

        canvas.save();
        canvas.clipRect(drawArea);
        canvas.drawText(text, 0.0f, 0.0f, paint);
        canvas.restore();
    }
}
