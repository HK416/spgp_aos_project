package com.hk416.fallingdowntino.object.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.object.Player;
import com.hk416.framework.object.UiTextObject;
import com.hk416.framework.transform.Anchor;

public class ScoreUi extends UiTextObject {
    private static final int MAX_DISTANCE = 9999;
    private final Player player;

    public ScoreUi(@NonNull Player player) {
        super(null, new Anchor(0.01f, 0.3f, 0.1f, 0.7f));
        this.player = player;
        setColor(Color.BLACK);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onUpdate(float elapsedTime) {
        super.onUpdate(elapsedTime);
        int distance = Math.min(MAX_DISTANCE, (int)(player.getDistance()));
        this.text = String.format("%04dm", distance);
    }
}
