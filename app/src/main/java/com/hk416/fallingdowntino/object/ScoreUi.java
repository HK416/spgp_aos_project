package com.hk416.fallingdowntino.object;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.hk416.framework.object.UiTextObject;

public class ScoreUi extends UiTextObject {
    private final Player player;

    public ScoreUi(@NonNull Player player) {
        super(null);
        this.player = player;
        setAnchor(0.0f, 0.35f, 0.1f, 0.65f);
        setColor(Color.BLACK);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onUpdate(float elapsedTime) {
        super.onUpdate(elapsedTime);
        float distance = player.getDistance();
        this.text = String.format("%05dm", (int)distance);
    }
}
