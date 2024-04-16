package com.hk416.fallingdowntino;

import androidx.annotation.NonNull;

public final class Vector {
    private static final float EPSLION = 1.401298E-45f;

    public static final Vector X = new Vector(1.0f, 0.0f, 0.0f);
    public static final Vector Y = new Vector(0.0f, 1.0f, 0.0f);
    public static final Vector Z = new Vector(0.0f, 0.0f, 1.0f);

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vector() {
        /* empty */
    }

    public Vector(float val) {
        this.x = val; this.y = val; this.z = val;
    }

    public Vector(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vector prevAdd(@NonNull Vector lhs) {
        return new Vector(lhs.x + x, lhs.y + y, lhs.z + z);
    }

    public Vector postAdd(@NonNull Vector rhs) {
        return new Vector(x + rhs.x, y + rhs.y, z + rhs.z);
    }

    public void postAddAssign(@NonNull Vector rhs) {
        x += rhs.x; y += rhs.y; z += rhs.z;
    }

    public Vector prevSub(@NonNull Vector lhs) {
        return new Vector(lhs.x - x, lhs.y - y, lhs.z - z);
    }

    public Vector postSub(@NonNull Vector rhs) {
        return new Vector(x - rhs.x, y - rhs.y, z - rhs.z);
    }

    public void postSubAssign(@NonNull Vector rhs) {
        x -= rhs.x; y -= rhs.y; z -= rhs.z;
    }

    public Vector prevMul(float lhs) {
        return new Vector(lhs * x, lhs * y, lhs * z);
    }

    public Vector prevMul(@NonNull Vector lhs) {
        return new Vector(lhs.x * x, lhs.y * y, lhs.z * z);
    }

    public Vector postMul(float rhs) {
        return new Vector(x * rhs, y * rhs, z * rhs);
    }

    public Vector postMul(@NonNull Vector rhs) {
        return new Vector(x * rhs.x, y * rhs.y, z * rhs.z);
    }

    public void postMulAssign(float rhs) {
        x *= rhs; y *= rhs; z *= rhs;
    }

    public void postMulAssign(@NonNull Vector rhs) {
        x *= rhs.x; y *= rhs.y; z *= rhs.z;
    }

    public Vector prevDiv(float lhs) {
        return new Vector(lhs / x, lhs / y, lhs/ z);
    }

    public Vector prevDiv(@NonNull Vector lhs) {
        return new Vector(lhs.x / x, lhs.y / y, lhs.z / z);
    }

    public Vector postDiv(float rhs) {
        return new Vector(x / rhs, y / rhs, z / rhs);
    }

    public Vector postDiv(@NonNull Vector rhs) {
        return new Vector(x / rhs.x, y / rhs.y, z / rhs.z);
    }

    public void postDivAssign(float rhs) {
        x /= rhs; y /= rhs; z /= rhs;
    }

    public void postDivAssign(@NonNull Vector rhs) {
        x /= rhs.x; y /= rhs.y; z /= rhs.z;
    }

    public float dot(@NonNull Vector rhs) {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    public float lengthSquare() {
        return x * x + y * y + z * z;
    }

    public float length() {
        return (float)Math.sqrt(lengthSquare());
    }

    public Vector normalize() {
        return this.postDiv(length());
    }

    public boolean isNormalized() {
        return Math.abs(lengthSquare()) <= EPSLION;
    }
}
