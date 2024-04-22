package com.hk416.framework.scene;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.framework.object.GameObject;

import java.util.ArrayDeque;

public class GameScene {
    private static final String TAG = GameScene.class.getSimpleName();

    protected final ArrayDeque<GameObject> gameObjects = new ArrayDeque<>();
    private final ArrayDeque<GameObject> insertObjects = new ArrayDeque<>();
    private final ArrayDeque<GameObject> removeObjects = new ArrayDeque<>();

    protected void insertObject(@NonNull GameObject object) {
        insertObjects.push(object);
    }

    protected void removeObject(@NonNull GameObject object) {
        removeObjects.push(object);
    }


    public void onEnter() {
        /* empty */
    }

    public void onExit() {
        /* empty */
    }

    public void onPause() {
        /* empty */
    }

    public void onResume() {
        /* empty */
    }

    public void handleEvent(@NonNull MotionEvent e) {
        for (GameObject object : gameObjects) {
            object.onTouchEvent(e);
        }
    }

    public void onUpdate(float elapsedTimeSec, long frameRate) {
        for (GameObject object : insertObjects) {
            gameObjects.push(object);
        }

        for (GameObject object : gameObjects) {
            object.onUpdate(elapsedTimeSec);
        }

        for (GameObject object : removeObjects) {
            gameObjects.remove(object);
        }
    }

    public void onDraw(@NonNull Canvas canvas) {
        for (GameObject object : gameObjects) {
            object.onDraw(canvas);
        }
    }
}
