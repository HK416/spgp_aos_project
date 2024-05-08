package com.hk416.framework.collide;

import androidx.annotation.NonNull;

public interface IGameCollider<T> {
    boolean isCollide(@NonNull T other);
}
