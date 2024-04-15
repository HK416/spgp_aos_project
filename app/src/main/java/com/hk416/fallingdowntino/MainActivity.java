package com.hk416.fallingdowntino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GameView gameView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "::onCreate >> MainActivity 생성.");
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.startScheduling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopScheduling();
    }
}