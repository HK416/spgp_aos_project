package com.hk416.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.texture.BitmapPool;
import com.hk416.framework.transform.Anchor;
import com.hk416.framework.transform.Margin;
import com.hk416.framework.transform.Viewport;

import java.util.ArrayList;

public class UiButtonObject extends UiObject {
    public interface IButtonFunc {
        void onClick();
    }

    enum State { Released, Pressed }

    private static final String TAG = UiButtonObject.class.getSimpleName();

    protected final ArrayList<Bitmap> btnImages = new ArrayList<>();
    protected final RectF drawArea = new RectF();
    protected final IButtonFunc buttonFunc;

    private State state = State.Released;
    private Integer pointerId = null;

    public UiButtonObject(
            int releasedResId,
            int pressedResId,
            @NonNull Anchor anchor,
            @Nullable IButtonFunc buttonFunc
    ) {
        super(anchor);
        this.buttonFunc = buttonFunc;
        init(releasedResId, pressedResId);
    }

    public UiButtonObject(
            int releasedResId,
            int pressedResId,
            @NonNull Anchor anchor,
            @NonNull Margin margin,
            @Nullable IButtonFunc buttonFunc
    ) {
        super(anchor, margin);
        this.buttonFunc = buttonFunc;
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

    private boolean contains(float x, float y) {
        buildDrawArea(btnImages.get(state.ordinal()));
        return drawArea.contains(x, y);
    }

    private void onClick() {
        Log.d(TAG, "::onClick >> 버튼이 선택됨!");
        if (buttonFunc != null) {
            buttonFunc.onClick();
        }
    }

    @Override
    public void onTouchEvent(@NonNull MotionEvent e) {
        super.onTouchEvent(e);
        int index = e.getActionIndex();
        int id = e.getPointerId(index);
        if (state == State.Released && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (contains(e.getX(), e.getY())) {
                state = State.Pressed;
                pointerId = id;
            }
        } else if (state == State.Pressed && pointerId == id && e.getAction() == MotionEvent.ACTION_UP) {
            if (contains(e.getX(), e.getY())) {
                onClick();
            }
            state = State.Released;
            pointerId = null;
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
