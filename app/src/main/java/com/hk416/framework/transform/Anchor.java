package com.hk416.framework.transform;

public final class Anchor {
    public float top = 0.5f;
    public float left = 0.5f;
    public float bottom = 0.5f;
    public float right = 0.5f;

    public Anchor() {
        /* empty */
    }

    public Anchor(float top, float left, float bottom, float right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
