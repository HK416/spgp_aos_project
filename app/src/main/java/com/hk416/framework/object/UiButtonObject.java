package com.hk416.framework.object;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.render.DrawPipeline;
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

    protected final ArrayList<GameObject> state = new ArrayList<>();
    protected final RectF drawArea = new RectF();
    protected final IButtonFunc buttonFunc;

    private State currState = State.Released;
    private Integer pointerId = null;

    public UiButtonObject(
            int releasedResId,
            int pressedResId,
            @NonNull Anchor anchor,
            @Nullable IButtonFunc buttonFunc
    ) {
        super(anchor);
        this.buttonFunc = buttonFunc;
        initFromBitmaps(releasedResId, pressedResId);
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
        initFromBitmaps(releasedResId, pressedResId);
    }

    public UiButtonObject(
            int releasedColor,
            int pressedColor,
            float rx,
            float ry,
            @NonNull Anchor anchor,
            @Nullable IButtonFunc buttonFunc
    ) {
        super(anchor);
        this.buttonFunc = buttonFunc;
        initFromColors(releasedColor, pressedColor, rx, ry);
    }

    private void initFromColors(int releasedColor, int pressedColor, float rx, float ry) {
        state.add(new UiRectObject(releasedColor, rx, ry, super.anchor));
        state.add(new UiRectObject(pressedColor, rx, ry, super.anchor));
    }

    private void initFromBitmaps(int releasedResId, int pressedResId) {
        state.add(new UiImageObject(releasedResId, super.anchor, super.margin));
        state.add(new UiImageObject(pressedResId, super.anchor, super.margin));
    }

    private void buildArea() {
        Viewport viewport = DrawPipeline.getInstance().getViewport();

        drawArea.top = viewport.top + anchor.top * viewport.getHeight() + margin.top;
        drawArea.left = viewport.left + anchor.left * viewport.getWidth() + margin.left;
        drawArea.bottom = viewport.top + anchor.bottom * viewport.getHeight() + margin.bottom;
        drawArea.right = viewport.left + anchor.right * viewport.getWidth() + margin.right;
    }

    private boolean contains(float x, float y) {
        buildArea();
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
        if (currState == State.Released && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (contains(e.getX(), e.getY())) {
                currState = State.Pressed;
                pointerId = id;
            }
        } else if (currState == State.Pressed && pointerId == id && e.getAction() == MotionEvent.ACTION_UP) {
            if (contains(e.getX(), e.getY())) {
                onClick();
            }
            currState = State.Released;
            pointerId = null;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        GameObject btn = state.get(currState.ordinal());
        if (btn != null) {
            btn.onDraw(canvas);
        }
    }
}
