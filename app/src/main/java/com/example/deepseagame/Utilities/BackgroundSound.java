package com.example.deepseagame.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundSound {

    private static Context context;
    private static volatile BackgroundSound instance;
    private MediaPlayer mediaPlayer;
    private int RES_ID;

    private BackgroundSound(Context context) {
        this.context = context;
    }

    public static BackgroundSound getInstance() {
        return instance;
    }

    public static BackgroundSound init(Context context){
        if (instance == null){
            synchronized (BackgroundSound.class){
                if (instance == null){
                    instance = new BackgroundSound(context.getApplicationContext());
                }
            }
        }
        return getInstance();
    }

    public void setResourceId(int RES_ID) {
        this.RES_ID = RES_ID;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        if (mediaPlayer != null) {
            release();
        }
        mediaPlayer = MediaPlayer.create(context, RES_ID);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.4f, 0.4f);
    }

    public void playMusic() {
        if (mediaPlayer == null  ||  mediaPlayer.isPlaying()) {
            initMediaPlayer();
        }

        try {
            mediaPlayer.start();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer == null  ||  !mediaPlayer.isPlaying()) {
            return;
        }

        try {
            mediaPlayer.pause();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        if (mediaPlayer == null  ||  !mediaPlayer.isPlaying()) {
            return;
        }

        try {
            mediaPlayer.stop();
            release();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void release() {
        if (mediaPlayer == null) {
            return;
        }

        try {
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isMusicPlaying() {
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.isPlaying();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


}
