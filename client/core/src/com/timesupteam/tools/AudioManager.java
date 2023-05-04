package com.timesupteam.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.timesupteam.TimesUpTeamGame;

public class AudioManager {
    public Sound openDoorSound;
    public Sound keyPickedSound;
    public Music playScreenMusic;
    public Music menuMusic;

    public Music gameOverMusic;

    public Sound clickSound;

    public Music violinMusic;

    public Music clockMusic;
    private float soundVolume = 1.0f;


    public AudioManager() {
        playScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/playScreenMusic.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menuMusic.mp3"));
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/gameOverMusic.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/clickSound.mp3"));
        violinMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/violinMusic.mp3"));
        clockMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/clockMusic.mp3"));
        keyPickedSound = Gdx.audio.newSound(Gdx.files.internal("audio/keyPickedSound.mp3"));
        openDoorSound = Gdx.audio.newSound(Gdx.files.internal("audio/openDoorSound.mp3"));
    }

    public void playMenuMusic() {
        if (TimesUpTeamGame.DEBUG.get("music")) {
            stopAllMusic();
            menuMusic.setLooping(true);
            menuMusic.play();
        }
    }

    public void playViolinMusic() {
        if (TimesUpTeamGame.DEBUG.get("music")) {
            violinMusic.setLooping(true);
            violinMusic.play();
        }
    }
    public void playClockMusic() {
        if (TimesUpTeamGame.DEBUG.get("music")) {
            clockMusic.setLooping(true);
            clockMusic.play();
        }
    }
    public void playGameOverMusic() {
        if (TimesUpTeamGame.DEBUG.get("music")) {
            stopAllMusic();
            gameOverMusic.setLooping(false);
            gameOverMusic.play();
        }
    }
    public void playClickSound() {
        clickSound.play(soundVolume);
    }

    public void playKeyPickedSound() {
        keyPickedSound.play(soundVolume);
    }

    public void playOpenDoorSound() {
        openDoorSound.play(soundVolume);
    }

    public void stopAllMusic() {
        menuMusic.stop();
        playScreenMusic.stop();
        gameOverMusic.stop();
        violinMusic.stop();
        clockMusic.stop();
    }

    public boolean isMenuMusicPlaying() {
        return menuMusic != null && menuMusic.isPlaying();
    }

    public void setAllMusicVolume(float volume) {
        menuMusic.setVolume(volume);
        playScreenMusic.setVolume(volume);
        gameOverMusic.setVolume(volume);
        violinMusic.setVolume(volume);
        clockMusic.setVolume(volume);
    }
    public void setAllSoundVolume(float volume) {
        soundVolume = volume;
    }

    public float getCurrentMusicVolume() {
        return menuMusic.getVolume();
    }

    public void setViolinMusic(float volume) {
        violinMusic.setVolume(volume);
    }

    public void dispose() {
        playScreenMusic.dispose();
        menuMusic.dispose();
        gameOverMusic.dispose();
        clickSound.dispose();
        violinMusic.dispose();
        clockMusic.dispose();
    }
}
