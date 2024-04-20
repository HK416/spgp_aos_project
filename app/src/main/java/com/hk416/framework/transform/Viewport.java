package com.hk416.framework.transform;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.hk416.framework.transform.Vector;

/**
 *  게임이 화면에 투영되는 영역을 나타내는 클래스 입니다.
 *  왼쪽 상단에서 오른쪽과 아래쪽으로 증가하는 좌표계를 사용합니다.
 *
 * @author HK416(powerspirit127@gmail.com)
 * @version 1.0
 */
public final class Viewport {
    public float top = 0.0f;
    public float left = 0.0f;
    public float bottom = 0.0f;
    public float right = 0.0f;

    public float getWidth() {
        return right - left;
    }

    public float getHeight() {
        return bottom - top;
    }

    /**
     * 투영 좌표계상의 한 점을 화면 좌표계상의 한 점의 위치로 변환하는 메소드입니다.
     *
     * @param projectionPoint -1.0f ~ 1.0f 사이의 값을 가지는 투영 좌표계상의 한 점의 위치
     * @return 화면 좌표계 상의 한 점의 위치
     */
    public PointF toScreenCoord(@NonNull Vector projectionPoint) {
        // 주어진 투영 좌표계(-1.0f ~ 1.0f)를 정규화(0.0f ~ 1.0f) 한다.
        float x = (0.5f * projectionPoint.x) + 0.5f;
        float y = 1.0f - (0.5f * projectionPoint.y) + 0.5f;
        return new PointF(
                left + x * getWidth(),
                top + y * getHeight()
        );
    }

    /**
     * 화면 좌표계상의 한 점을 투영 좌표계상의 한 점의 위치로 변환하는 메소드입니다.
     *
     * @param screenPoint 화면 좌표계상의 한 점의 위치
     * @return 투영 좌표계 상의 한 점의 위치
     */
    public Vector toProjectionCoord(@NonNull PointF screenPoint) {
        float x = 2.0f * ((screenPoint.x - left) / getWidth() - 0.5f);
        float y = 2.0f * ((1.0f - (screenPoint.y - top) / getHeight() - 0.5f));
        return new Vector(x, y, 0.0f);
    }
}
