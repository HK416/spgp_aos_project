package com.hk416.framework.transform;

import androidx.annotation.NonNull;

public final class Transform {
    public static final Transform IDENTITY = new Transform();

    public Vector xAxis = Vector.X;
    public Vector yAxis = Vector.Y;
    public Vector zAxis = Vector.Z;

    public Transform() {
        /* empty */
    }

    public Transform(float angleRadian) {
        float sin = (float)Math.sin(angleRadian);
        float cos = (float)Math.cos(angleRadian);
        this.xAxis = new Vector(cos, -sin, 0.0f);
        this.yAxis = new Vector(sin, cos, 0.0f);
        this.zAxis = Vector.Z;
    }

    public Transform(@NonNull Vector xAxis, @NonNull Vector yAxis, @NonNull Vector zAxis) {
        this.xAxis = xAxis; this.yAxis = yAxis; this.zAxis = zAxis;
    }

    public Transform prevAdd(@NonNull Transform lhs) {
        return new Transform(
                lhs.xAxis.postAdd(xAxis),
                lhs.yAxis.postAdd(yAxis),
                lhs.zAxis.postAdd(zAxis)
        );
    }

    public Transform postAdd(@NonNull Transform rhs) {
        return new Transform(
                xAxis.postAdd(rhs.xAxis),
                yAxis.postAdd(rhs.yAxis),
                zAxis.postAdd(rhs.zAxis)
        );
    }

    public void postAddAssign(@NonNull Transform rhs) {
        xAxis.postAddAssign(rhs.xAxis);
        yAxis.postAddAssign(rhs.yAxis);
        zAxis.postAddAssign(rhs.zAxis);
    }

    public Transform neg() {
        return new Transform(
                xAxis.neg(),
                yAxis.neg(),
                zAxis.neg()
        );
    }

    public Transform prevSub(@NonNull Transform lhs) {
        return new Transform(
                lhs.xAxis.postSub(xAxis),
                lhs.yAxis.postSub(yAxis),
                lhs.zAxis.postSub(zAxis)
        );
    }

    public Transform postSub(@NonNull Transform rhs) {
        return new Transform(
                xAxis.postSub(rhs.xAxis),
                yAxis.postSub(rhs.yAxis),
                zAxis.postSub(rhs.zAxis)
        );
    }

    public void postSubAssign(@NonNull Transform rhs) {
        xAxis.postSubAssign(rhs.xAxis);
        yAxis.postSubAssign(rhs.yAxis);
        zAxis.postSubAssign(rhs.zAxis);
    }

    public Transform prevMul(float lhs) {
        return new Transform(
                xAxis.prevMul(lhs),
                yAxis.prevMul(lhs),
                zAxis.prevMul(lhs)
        );
    }

    public Transform prevMul(@NonNull Transform lhs) {
        return new Transform(
                new Vector(
                        lhs.xAxis.x * xAxis.x + lhs.xAxis.y * yAxis.x + lhs.xAxis.z * zAxis.x,
                        lhs.xAxis.x * xAxis.y + lhs.xAxis.y * yAxis.y + lhs.xAxis.z * zAxis.y,
                        lhs.xAxis.x * xAxis.z + lhs.xAxis.y * yAxis.z + lhs.xAxis.z * zAxis.z
                ),
                new Vector(
                        lhs.yAxis.x * xAxis.x + lhs.yAxis.y * yAxis.x + lhs.yAxis.z * zAxis.x,
                        lhs.yAxis.x * xAxis.y + lhs.yAxis.y * yAxis.y + lhs.yAxis.z * zAxis.y,
                        lhs.yAxis.x * xAxis.z + lhs.yAxis.y * yAxis.z + lhs.yAxis.z * zAxis.z
                ),
                new Vector(
                        lhs.zAxis.x * xAxis.x + lhs.zAxis.y * yAxis.x + lhs.zAxis.z * zAxis.x,
                        lhs.zAxis.x * xAxis.y + lhs.zAxis.y * yAxis.y + lhs.zAxis.z * zAxis.y,
                        lhs.zAxis.x * xAxis.z + lhs.zAxis.y * yAxis.z + lhs.zAxis.z * zAxis.z
                )
        );
    }

    public Transform postMul(float rhs) {
        return new Transform(
                xAxis.postMul(rhs),
                yAxis.postMul(rhs),
                zAxis.postMul(rhs)
        );
    }

    public Transform postMul(@NonNull Transform rhs) {
        return new Transform(
                new Vector(
                        xAxis.x * rhs.xAxis.x + xAxis.y * rhs.yAxis.x + xAxis.z * rhs.zAxis.x,
                        xAxis.x * rhs.xAxis.y + xAxis.y * rhs.yAxis.y + xAxis.z * rhs.zAxis.y,
                        xAxis.x * rhs.xAxis.z + xAxis.y * rhs.yAxis.z + xAxis.z * rhs.zAxis.z
                ),
                new Vector(
                        yAxis.x * rhs.xAxis.x + yAxis.y * rhs.yAxis.x + yAxis.z * rhs.zAxis.x,
                        yAxis.x * rhs.xAxis.y + yAxis.y * rhs.yAxis.y + yAxis.z * rhs.zAxis.y,
                        yAxis.x * rhs.xAxis.z + yAxis.y * rhs.yAxis.z + yAxis.z * rhs.zAxis.z
                ),
                new Vector(
                        zAxis.x * rhs.xAxis.x + zAxis.y * rhs.yAxis.x + zAxis.z * rhs.zAxis.x,
                        zAxis.x * rhs.xAxis.y + zAxis.y * rhs.yAxis.y + zAxis.z * rhs.zAxis.y,
                        zAxis.x * rhs.xAxis.z + zAxis.y * rhs.yAxis.z + zAxis.z * rhs.zAxis.z
                )
        );
    }

    public void postMulAssign(float rhs) {
        xAxis.postMulAssign(rhs);
        yAxis.postMulAssign(rhs);
        zAxis.postMulAssign(rhs);
    }

    public void postMulAssign(@NonNull Transform rhs) {
        xAxis = new Vector(
                xAxis.x * rhs.xAxis.x + xAxis.y * rhs.yAxis.x + xAxis.z * rhs.zAxis.x,
                xAxis.x * rhs.xAxis.y + xAxis.y * rhs.yAxis.y + xAxis.z * rhs.zAxis.y,
                xAxis.x * rhs.xAxis.z + xAxis.y * rhs.yAxis.z + xAxis.z * rhs.zAxis.z
        );
        yAxis = new Vector(
                yAxis.x * rhs.xAxis.x + yAxis.y * rhs.yAxis.x + yAxis.z * rhs.zAxis.x,
                yAxis.x * rhs.xAxis.y + yAxis.y * rhs.yAxis.y + yAxis.z * rhs.zAxis.y,
                yAxis.x * rhs.xAxis.z + yAxis.y * rhs.yAxis.z + yAxis.z * rhs.zAxis.z
        );
        zAxis = new Vector(
                zAxis.x * rhs.xAxis.x + zAxis.y * rhs.yAxis.x + zAxis.z * rhs.zAxis.x,
                zAxis.x * rhs.xAxis.y + zAxis.y * rhs.yAxis.y + zAxis.z * rhs.zAxis.y,
                zAxis.x * rhs.xAxis.z + zAxis.y * rhs.yAxis.z + zAxis.z * rhs.zAxis.z
        );
    }

    public Transform prevDiv(float lhs) {
        return new Transform(
                xAxis.prevDiv(lhs),
                yAxis.prevDiv(lhs),
                zAxis.prevDiv(lhs)
        );
    }

    public Transform postDiv(float rhs) {
        return new Transform(
                xAxis.postDiv(rhs),
                yAxis.postDiv(rhs),
                zAxis.postDiv(rhs)
        );
    }

    public void postDivAssign(float rhs) {
        xAxis.postDivAssign(rhs);
        yAxis.postDivAssign(rhs);
        zAxis.postDivAssign(rhs);
    }

    public Transform transpose() {
        return new Transform(
                new Vector(xAxis.x, yAxis.x, zAxis.x),
                new Vector(xAxis.y, yAxis.y, zAxis.y),
                new Vector(xAxis.z, yAxis.z, zAxis.z)
        );
    }

    public Transform inverse() {
        Vector v0 = yAxis.cross(zAxis);
        Vector v1 = zAxis.cross(xAxis);
        Vector v2 = xAxis.cross(yAxis);
        float det = zAxis.dot(v2);
        Vector invDet = new Vector(1.0f / det);
        return new Transform(
                v0.postMul(invDet),
                v1.postMul(invDet),
                v2.postMul(invDet)
        ).transpose();
    }

    public Vector transform(@NonNull Vector vector) {
        return new Vector(
                xAxis.x * vector.x + yAxis.x * vector.y + zAxis.x * vector.z,
                xAxis.y * vector.x + yAxis.y * vector.y + zAxis.y * vector.z,
                xAxis.z * vector.x + yAxis.z * vector.y + zAxis.z * vector.z
        );
    }
}
