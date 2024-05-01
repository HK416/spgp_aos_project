package com.hk416.framework.scene;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.hk416.framework.object.GameObject;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameScene {
    private static final String TAG = GameScene.class.getSimpleName();

    protected final ArrayList<ArrayDeque<GameObject>> gameObjects;
    private final ArrayList<ArrayDeque<GameObject>> insertObjects;
    private final ArrayList<ArrayDeque<GameObject>> removeObjects;
    private final int numTags;

    public GameScene(int numTags) {
        gameObjects = new ArrayList<>();
        insertObjects = new ArrayList<>();
        removeObjects = new ArrayList<>();
        for (int i = 0; i < numTags; i++) {
            gameObjects.add(new ArrayDeque<>());
            insertObjects.add(new ArrayDeque<>());
            removeObjects.add(new ArrayDeque<>());
        }
        this.numTags = numTags;
    }

    protected <E extends Enum<E>>void insertObject(E tag, @NonNull GameObject object) {
        ArrayDeque<GameObject> queue = insertObjects.get(tag.ordinal());
        queue.push(object);
    }

    protected <E extends Enum<E>>void removeObject(E tag, @NonNull GameObject object) {
        ArrayDeque<GameObject> queue = removeObjects.get(tag.ordinal());
        queue.push(object);
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
        for (ArrayDeque<GameObject> objects : gameObjects) {
            for (GameObject object : objects) {
                object.onTouchEvent(e);
            }
        }
    }

    public void onUpdate(float elapsedTimeSec, long frameRate) {
        for (int i = 0; i < numTags; i++) {
            ArrayDeque<GameObject> objects = gameObjects.get(i);
            ArrayDeque<GameObject> queue = insertObjects.get(i);
            while (!queue.isEmpty()) {
                objects.push(queue.pop());
            }
        }

        for (ArrayDeque<GameObject> objects : gameObjects) {
            for (GameObject object : objects) {
                object.onUpdate(elapsedTimeSec);
            }
        }

        for (int i = 0; i < numTags; i++) {
            ArrayDeque<GameObject> objects = gameObjects.get(i);
            ArrayDeque<GameObject> queue = removeObjects.get(i);
            while (!queue.isEmpty()) {
                objects.remove(queue.pop());
            }
        }
    }

    public void onDraw(@NonNull Canvas canvas) {
        for (ArrayDeque<GameObject> objects : gameObjects) {
            for (GameObject object : objects) {
                object.onDraw(canvas);
            }
        }
    }
}
