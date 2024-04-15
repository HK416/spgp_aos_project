package com.hk416.fallingdowntino;

import androidx.annotation.NonNull;

import java.util.Stack;

public final class SceneManager {
    public static final String TAG = SceneManager.class.getSimpleName();

    private static final SceneManager instance = new SceneManager();

    private static final int CMD_NONE = 0;
    public static final int CMD_PUSH = 1;
    public static final int CMD_POP = 2;
    public static final int CMD_CHANGE = 3;

    private Stack<IGameScene> sceneStack = new Stack<>();
    private IGameScene nextScene = null;
    private int commands = CMD_NONE;

    public static SceneManager getInstance() {
        return instance;
    }

    public void changeScene(@NonNull IGameScene scene) {
        nextScene = scene;
        commands = CMD_CHANGE;
    }

    public void pushScene(@NonNull IGameScene scene) {
        nextScene = scene;
        commands = CMD_PUSH;
    }

    public void popScene() {
        nextScene = null;
        commands = CMD_POP;
    }
}
