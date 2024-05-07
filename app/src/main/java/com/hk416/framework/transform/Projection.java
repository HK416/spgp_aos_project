package com.hk416.framework.transform;

import androidx.annotation.NonNull;

/**
 * 카메라 좌표계에서 투영 좌표계에 투영되는 영역을 나타내는 클래스입니다.
 * 왼쪽 하단에서 오른쪽과 위쪽으로 증가하는 좌표계를 사용합니다.
 *
 * @author HK416(powerspirit127@gmail.com)
 * @version 1.0
 */
public final class Projection {
    public static final float DEF_LEFT = -4.5f;
    public static final float DEF_RIGHT = 4.5f;
    public static final float DEF_TOP = 8.0f;
    public static final float DEF_BOTTOM = -8.0f;
    public static final float DEF_NEAR = 0.0f;
    public static final float DEF_FAR = 100.0f;

    public float top = DEF_TOP;
    public float left = DEF_LEFT;
    public float bottom = DEF_BOTTOM;
    public float right = DEF_RIGHT;
    public float zNear = DEF_NEAR;
    public float zFar = DEF_FAR;

    public Projection() {
        /* empty */
    }

    public Projection(float top, float left, float bottom, float right, float zNear, float zFar) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public float getWidth() {
        return right - left;
    }

    public float getHeight() {
        return top - bottom;
    }

    public float getDepth() {
        return zFar - zNear;
    }

    /**
     * 카메라 좌표계상의 한 점을 투영 좌표계상의 한 점의 위치로 변환하는 메소드입니다.
     *
     * @param cameraPoint 카메라 좌표계상의 한 점의 위치
     * @return 투영 좌표계상의 한 점의 위치
     */
    public Vector toProjectionCoord(@NonNull Vector cameraPoint) {
        float x = 2.0f * ((cameraPoint.x - left) / getWidth() - 0.5f); // -1.0f ~ 1.0f
        float y = 2.0f * ((cameraPoint.y - bottom) / getHeight() - 0.5f); // -1.0f ~ 1.0f
        float z = (cameraPoint.z - zNear) / getDepth(); // 0.0f ~ 1.0f
        return new Vector(x, y, z);
    }

    /**
     * 투영 좌표계상의 한 점을 카메라 좌표계상의 한 점의 위치로 변환하는 메소드입니다.
     *
     * @param projectionPoint -1.0f ~ 1.0f 사이의 값을 가지는 투영 좌표계상의 한 점의 위치
     * @return 카메라 좌표계 상의 한 점의 위치
     */
    public Vector toCameraCoord(@NonNull Vector projectionPoint) {
        float x = left + ((projectionPoint.x * 0.5f) + 0.5f) * getWidth();
        float y = bottom + ((projectionPoint.y * 0.5f) + 0.5f) * getHeight();
        float z = zNear + projectionPoint.z * getDepth();
        return new Vector(x, y, z);
    }
}
