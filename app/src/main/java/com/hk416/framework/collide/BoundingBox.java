package com.hk416.framework.collide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.transform.Vector;

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
    public boolean intersects(@NonNull BoundingBox other, @Nullable Vector depth) {
        float leftSideA = centerX - 0.5f * sizeX;
        float rightSideA = centerX + 0.5f * sizeX;
        float bottomSideA = centerY - 0.5f * sizeY;
        float topSideA = centerY + 0.5f * sizeY;

        float leftSideB = other.centerX - 0.5f * other.sizeX;
        float rightSideB = other.centerX + 0.5f * other.sizeX;
        float bottomSideB = other.centerY - 0.5f * other.sizeY;
        float topSideB = other.centerY + 0.5f * other.sizeY;

        if (leftSideA <= rightSideB) { // other가 +x 방향으로
            if (bottomSideA <= topSideB) { // other가 +y 방향으로
                if (depth != null) {
                    depth.x = rightSideB - leftSideA;
                    depth.y = topSideB - bottomSideA;
                    depth.z = 0.0f;
                }
                return true;
            } else if (topSideA >= bottomSideB) { // other가 -y 방향으로
                if (depth != null) {
                    depth.x = rightSideB - leftSideA;
                    depth.y = bottomSideB - topSideA;
                    depth.z = 0.0f;
                }
                return true;
            }
        } else if  (rightSideA >= leftSideB) { // other가 -x 방향으로
            if (bottomSideA <= topSideB) { // other가 +y 방향으로
                if (depth != null) {
                    depth.x = leftSideB - rightSideA;
                    depth.y = topSideB - bottomSideA;
                    depth.z = 0.0f;
                }
                return true;
            } else if (topSideA >= bottomSideB) { // other가 -y 방향으로
                if (depth != null) {
                    depth.x = leftSideB - rightSideA;
                    depth.y = bottomSideB - topSideA;
                    depth.z = 0.0f;
                }
                return true;
            }
        }

        return false;
    }
}
