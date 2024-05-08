package com.hk416.framework.collide;

import androidx.annotation.NonNull;

public final class BoundingBox implements IGameCollider<BoundingBox> {
    public float centerX = 0.0f;
    public float centerY = 0.0f;
    public float sizeX = 0.0f;
    public float sizeY = 0.0f;

    public BoundingBox() {
        /* empty */
    }

    public BoundingBox(float x, float y, float sx, float sy) {
        centerX = x;
        centerY = y;
        sizeX = sx;
        sizeY = sy;
    }

    @Override
    public boolean isCollide(@NonNull BoundingBox other) {
        float leftSideA = centerX - 0.5f * sizeX;
        float rightSideA = centerX + 0.5f * sizeX;
        float bottomSideA = centerY - 0.5f * sizeY;
        float topSideA = centerY + 0.5f * sizeY;

        float leftSideB = other.centerX - 0.5f * other.sizeX;
        float rightSideB = other.centerX + 0.5f * other.sizeX;
        float bottomSideB = other.centerY - 0.5f * other.sizeY;
        float topSideB = other.centerY - 0.5f * other.sizeY;

        if (rightSideA < leftSideB || leftSideA > rightSideB) {
            return false;
        }

        if (topSideA < bottomSideB || bottomSideA > topSideB) {
            return false;
        }

        return true;
    }
}
