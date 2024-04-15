package com.hk416.fallingdowntino;

import android.graphics.Canvas;
import android.util.Log;

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

    public void onPause() {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면을 정지시킵니다.
        }
    }

    public void onResume() {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면을 재개합니다.
        }
    }

    public boolean onUpdate(float elapsedTimeSec, long frameRate) {
        // 다음 장면을 설정하는 명령어를 수행합니다.
        processCommands();

        // 현재 장면을 갱신합니다.
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면을 갱신합니다.
            return true;
        }
        return false;
    }

    public void onDraw(@NonNull Canvas canvas) {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면을 그립니다.
        }
    }

    private void processCommands() {
        switch (commands) {
            case CMD_NONE: /* empty */
                break;
            case CMD_PUSH:
                processPushScene();
                break;
            case CMD_POP:
                processPopScene();
                break;
            case CMD_CHANGE:
                processChangeScene();
                break;
            default:
                Log.w(TAG, "::processCommands >> 유효하지 않은 명령어 입니다.");
                break;
        }
        cmdReset();
    }

    private IGameScene getCurrentScene() {
        return sceneStack.isEmpty() ? null : sceneStack.peek();
    }

    private void processChangeScene() {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면에서 빠져나옵니다.
            sceneStack.pop();
        }

        // TODO: 다음 장면에 진입합니다.
        sceneStack.push(nextScene);
    }

    private void processPushScene() {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면을 정지시킵니다.
        }

        // TODO: 다음 장면에 진입합니다.
        sceneStack.push(nextScene);
    }

    private void processPopScene() {
        IGameScene currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 현재 장면에서 빠져나옵니다.
            sceneStack.pop();
        }

        currentScene = getCurrentScene();
        if (currentScene != null) {
            // TODO: 이전 장면을 재개합니다.
        }
    }

    public void cmdReset() {
        nextScene = null;
        commands = CMD_NONE;
    }

    public void cmdChangeScene(@NonNull IGameScene scene) {
        nextScene = scene;
        commands = CMD_CHANGE;
    }

    public void cmdPushScene(@NonNull IGameScene scene) {
        nextScene = scene;
        commands = CMD_PUSH;
    }

    public void cmdPopScene() {
        nextScene = null;
        commands = CMD_POP;
    }
}
