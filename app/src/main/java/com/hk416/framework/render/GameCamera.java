package com.hk416.framework.render;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Transform;
import com.hk416.framework.transform.Vector;

public class GameCamera extends GameObject {
    private static final String TAG = GameCamera.class.getSimpleName();

    protected Projection projection = null;
    protected Transform cameraTransform = Transform.IDENTITY;
    protected Transform inverseCameraTransform = Transform.IDENTITY;

    public GameCamera() {
        /* empty */
    }

    public void setProjection(@NonNull Projection projection) {
        this.projection = projection;
    }

    public void generateCameraTransform() {
        Vector position = getPosition();
        Vector right = getRightVector();
        Vector up = getUpVector();

        cameraTransform.xAxis.x = transform.xAxis.x;
        cameraTransform.xAxis.y = transform.yAxis.x;
        cameraTransform.xAxis.z = 0.0f;

        cameraTransform.yAxis.x = transform.xAxis.y;
        cameraTransform.yAxis.y = transform.yAxis.y;
        cameraTransform.yAxis.z = 0.0f;

        cameraTransform.zAxis.x = -position.dot(right);
        cameraTransform.zAxis.y = -position.dot(up);
        cameraTransform.zAxis.z = 1.0f;

        inverseCameraTransform = cameraTransform.inverse();
    }

    public Vector toProjectionCoord(@NonNull Vector worldPoint) {
        if (projection == null) {
            Log.w(TAG, "::toProjectionCoord >> 카메라에 projection이 설정되지 않았습니다.");
            return null;
        }

        Vector cameraPoint = cameraTransform.transform(worldPoint);
        return projection.toProjectionCoord(cameraPoint);
    }

    public Vector toWorldCoord(@NonNull Vector projectionPoint) {
        if (projection == null) {
            Log.w(TAG, "::toWorldCoord >> 카메라에 projection이 설정되지 않았습니다.");
            return null;
        }

        Vector cameraPoint = projection.toCameraCoord(projectionPoint);
        return inverseCameraTransform.transform(cameraPoint);
    }
}
