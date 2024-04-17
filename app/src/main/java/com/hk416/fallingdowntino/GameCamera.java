package com.hk416.fallingdowntino;

import androidx.annotation.NonNull;

public class GameCamera extends GameObject {
    protected Transform cameraTransform = Transform.IDENTITY;
    protected Transform inverseCameraTransform = Transform.IDENTITY;

    public GameCamera() {
        /* empty */
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

    public Vector toCameraCoord(@NonNull Vector worldPoint) {
        return cameraTransform.transform(worldPoint);
    }

    public Vector toWorldCoord(@NonNull Vector cameraPoint) {
        return inverseCameraTransform.transform(cameraPoint);
    }
}
