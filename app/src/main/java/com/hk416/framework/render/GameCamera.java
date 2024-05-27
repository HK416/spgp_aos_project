package com.hk416.framework.render;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.object.GameObject;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Transform;
import com.hk416.framework.transform.Vector;

public class GameCamera extends GameObject {
    private static final String TAG = GameCamera.class.getSimpleName();

    protected Projection projection = null;
    protected Transform cameraTransform = new Transform();
    protected Transform inverseCameraTransform = new Transform();

    public GameCamera() {
        /* empty */
    }

    public GameCamera(float x, float y) {
        super(x, y);
    }

    public void setProjection(@NonNull Projection projection) {
        this.projection = projection;
    }

    @Nullable
    public  Projection getProjection() {
        return projection;
    }

    public void generateCameraTransform() {
        Vector position = getWorldPosition();
        Vector right = getWorldRightVector();
        Vector up = getWorldUpVector();

        cameraTransform.xAxis.x = worldTransform.xAxis.x;
        cameraTransform.xAxis.y = worldTransform.yAxis.x;
        cameraTransform.xAxis.z = 0.0f;

        cameraTransform.yAxis.x = worldTransform.xAxis.y;
        cameraTransform.yAxis.y = worldTransform.yAxis.y;
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
