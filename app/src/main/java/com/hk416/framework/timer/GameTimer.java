package com.hk416.framework.timer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.BuildConfig;

public final class GameTimer {
    private static final int MAX_SAMPLES = 50;
    private static final float SECONDS_PER_NANO = 1.0f / 1e+9f;
    public static final float DEBUG_TEXT_SIZE = 64.0f;

    private static Paint debugPaint;

    private long previousTimePoint = 0;
    private long currentTimePoint = 0;

    private final float[] sampleTimes = new float[MAX_SAMPLES];
    private int numSampleTimes = 0;
    private float elapsedTimeSec = 0.0f;
    private float fpsElapsedTimeSec = 0.0f;
    private long framePerSeconds = 0;
    private long frameRate = 0;

    public GameTimer() {
        if (BuildConfig.DEBUG && debugPaint == null) {
            debugPaint = new Paint();
            debugPaint.setARGB(255, 51, 204, 51);
            debugPaint.setTextSize(DEBUG_TEXT_SIZE);
        }
    }

    private static float toSeconds(long nanoTimes) {
        return nanoTimes * SECONDS_PER_NANO;
    }

    public void reset(long frameTimeNanos) {
        previousTimePoint = frameTimeNanos;
        currentTimePoint = frameTimeNanos;
        numSampleTimes = 0;
        elapsedTimeSec = 0.0f;
        fpsElapsedTimeSec = 0.0f;
        framePerSeconds = 0;
        frameRate = 0;
    }

    public void tick(long frameTimeNanos) {
        // 초단위의 지난 시간을 계산한다.
        currentTimePoint = frameTimeNanos;
        long duration = currentTimePoint - previousTimePoint;
        float elapsedTimeSec = toSeconds(duration);

        previousTimePoint = currentTimePoint;

        // 지난 시간이 유효한 값일 경우 샘플에 추가한다.
        if (Math.abs(this.elapsedTimeSec - elapsedTimeSec) < 1.0f) {
            System.arraycopy(sampleTimes, 0, sampleTimes, 1, MAX_SAMPLES - 1);
            sampleTimes[0] = elapsedTimeSec;
            numSampleTimes = Math.min(numSampleTimes + 1, MAX_SAMPLES);
        }

        // FrameRate를 계산한다.
        framePerSeconds += 1;
        fpsElapsedTimeSec += elapsedTimeSec;
        if (fpsElapsedTimeSec > 1.0f) {
            frameRate = framePerSeconds;
            framePerSeconds = 0;
            fpsElapsedTimeSec -= 1.0f;
        }

        // 지난 시간을 계산한다.
        this.elapsedTimeSec = 0.0f;
        if (numSampleTimes > 0) {
            for (int i = 0; i < numSampleTimes; i++) {
                this.elapsedTimeSec += sampleTimes[i];
            }
            this.elapsedTimeSec /= (float)numSampleTimes;
        }
    }

    public float getElapsedTimeSec() {
        return elapsedTimeSec;
    }

    public long getFrameRate() {
        return frameRate;
    }

    @SuppressLint("DefaultLocale")
    public void drawDebugFps(@NonNull Canvas canvas, float x, float y) {
        if (BuildConfig.DEBUG && debugPaint != null) {
            canvas.drawText(
                    String.format("FPS: %d", getFrameRate()),
                    x, y,
                    debugPaint
            );
        }
    }
}
