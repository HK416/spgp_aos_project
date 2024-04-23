package com.hk416.framework.texture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class BitmapPool {
    private static final String TAG = BitmapPool.class.getSimpleName();
    private static BitmapPool instance = null;

    private final Map<Integer, Bitmap> bitmaps = new HashMap<>();
    private final BitmapFactory.Options opts;
    private final Resources res;

    public BitmapPool(@NonNull Resources res) {
        this.res = res;
        this.opts = new BitmapFactory.Options();
        this.opts.inScaled = false;
    }

    public static void init(@NonNull Resources res) {
        if (instance == null) {
            instance = new BitmapPool(res);
        }
    }

    @Nullable
    public static BitmapPool getInstance() {
        return instance;
    }

    @NonNull
    public Bitmap get(int resId) {
        Bitmap bitmap = bitmaps.get(resId);
        if (bitmap != null) {
            return bitmap;
        } else {
            bitmap = BitmapFactory.decodeResource(res, resId, opts);
            Log.d(TAG, "::get >> 비트맵 이미지 로드됨 (ID:" + resId
                    + ", (w:" + bitmap.getWidth()
                    + ", h:" + bitmap.getHeight()
                    + "))"
            );

            bitmaps.put(resId, bitmap);
            return bitmap;
        }
    }

    public void clear() {
        bitmaps.clear();
    }
}
