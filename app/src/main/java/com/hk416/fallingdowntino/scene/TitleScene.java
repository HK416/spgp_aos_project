package com.hk416.fallingdowntino.scene;

import com.hk416.framework.scene.GameScene;

public class TitleScene extends GameScene {
    enum Tags { Background, Button, Text };

    private static final String TAG = TitleScene.class.getSimpleName();

    public TitleScene() {
        super(Tags.values().length);
    }
}
