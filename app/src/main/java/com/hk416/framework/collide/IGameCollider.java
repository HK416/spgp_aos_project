package com.hk416.framework.collide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk416.framework.transform.Vector;

public interface IGameCollider<T> {
    /**
     * 두 충돌체가 서로 충돌했는지 확인하는 함수입니다.
     *
     * @param other 상대 충돌체 입니다.
     * @param depth 상대 충돌체가 이 충돌체에 충돌한 깊이 정보입니다.
     * @return 충돌할 경우 `true`를 반환합니다. 이때 depth가 `null`이 아닐 경우 충돌 깊이도 반환합니다.
     */
    boolean intersects(@NonNull T other, @Nullable Vector depth);
}
