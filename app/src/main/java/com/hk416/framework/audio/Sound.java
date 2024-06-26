package com.hk416.framework.audio;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.hk416.fallingdowntino.GameView;

import java.util.HashMap;

public class Sound {
    protected static MediaPlayer mediaPlayer;
    protected static SoundPool soundPool;


    public static void playMusic(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(GameView.getRootContext(), resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    public static void stopMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer = null;
    }

    public static void pauseAllSounds() {
        pauseMusic();
        pauseEffects();
    }

    public static void pauseMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
        getSoundPool().autoPause();
    }

    public static void pauseEffects() {
        getSoundPool().autoPause();
    }

    public static void resumeAllSounds() {
        resumeMusic();
        resumeEffects();
    }

    public static void resumeMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
        getSoundPool().autoResume();
    }

    public static void resumeEffects() {
        getSoundPool().autoResume();
    }

    private static final HashMap<Integer, Integer> soundIdMap = new HashMap<>();
    public static void playEffect(int resId) {
        SoundPool pool = getSoundPool();
        int soundId;
        if (soundIdMap.containsKey(resId)) {
            soundId = soundIdMap.get(resId);
        } else {
            soundId = pool.load(GameView.getRootContext(), resId, 1);
            soundIdMap.put(resId, soundId);
        }

        pool.play(soundId, 1f, 1f, 0, 0, 1f);
    }

    public static void preloadEffect(int resId) {
        if (soundIdMap.containsKey(resId)) {
            return;
        }

        SoundPool pool = getSoundPool();
        int soundId = pool.load(GameView.getRootContext(), resId, 1);
        soundIdMap.put(resId, soundId);
    }

    private static SoundPool getSoundPool() {
        if (soundPool != null) return soundPool;

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(3)
                .build();
        return soundPool;
    }
}
