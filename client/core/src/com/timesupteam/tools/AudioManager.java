package com.timesupteam.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public Music playScreenMusic;
    public Music menuMusic;

    public Music gameOverMusic;

    public Sound clickSound;


    public AudioManager() {
        playScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/playScreenMusic.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menuMusic.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/gameOverMusic.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/clickSound.mp3"));
    }

    public void playMenuMusic() {
        stopAllMusic();
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    public void playPlayScreenMusic() {
        stopAllMusic();
        playScreenMusic.setLooping(true);
        playScreenMusic.play();
    }
    public void playGameOverMusic() {
        stopAllMusic();
        gameOverMusic.setLooping(false);
        gameOverMusic.play();
    }
    public void playClickSound() {
        clickSound.play();
    }

    private void stopAllMusic() {
        menuMusic.stop();
        playScreenMusic.stop();
        gameOverMusic.stop();
    }

    public boolean isMenuMusicPlaying() {
        return menuMusic != null && menuMusic.isPlaying();
    }

    public void setAllMusicVolume(float volume) {
        menuMusic.setVolume(volume);
        playScreenMusic.setVolume(volume);
        gameOverMusic.setVolume(volume);
    }


    public void dispose() {
        playScreenMusic.dispose();
        menuMusic.dispose();
        gameOverMusic.dispose();
        clickSound.dispose();
    }
}
