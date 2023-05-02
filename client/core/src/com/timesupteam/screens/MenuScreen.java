package com.timesupteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.timesupteam.TimesUpTeamGame;

import java.sql.Time;

public class MenuScreen implements Screen {
    private TimesUpTeamGame game;
    private Stage stage;
    private Skin skin;
    private int BUTTON_WIDTH = 70;
    private int BUTTON_HEIGHT = 35;

    public MenuScreen(final TimesUpTeamGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(
                TimesUpTeamGame.V_WIDTH, TimesUpTeamGame.V_HEIGHT
        ));

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/uiskin.json")); // Make sure to have a UI skin JSON file in your assets folder

        // Create background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("skins/background22.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);


        // Create the play button
        TextButton playButton = new TextButton("Play", skin);
        playButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.setPosition(stage.getWidth() / 2 - playButton.getWidth() / 2, stage.getHeight() / 2 - playButton.getHeight() / 2 + 70);

        // Set up the play button click listener
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playClickSound();
                TimesUpTeamGame.isTimeUp = false;
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        // Add the play button to the stage
        stage.addActor(playButton);

        // Create the settings button
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(stage.getWidth() / 2 - settingsButton.getWidth() / 2, stage.getHeight() / 2 - settingsButton.getHeight() / 2);

        // Set up the settings button click listener
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the settings screen or perform other actions
                game.audioManager.playClickSound();
                game.setScreen(new SettingsScreen(game));
            }
        });

        // Add the settings button to the stage
        stage.addActor(settingsButton);

        // Create the exit button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(stage.getWidth() / 2 - exitButton.getWidth() / 2, stage.getHeight() / 2 - exitButton.getHeight() / 2 - 70);

        // Set up the exit button click listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playClickSound();
                Gdx.app.exit(); // Exit the game
            }
        });

        // Add the exit button to the stage
        stage.addActor(exitButton);
    }

    @Override
    public void show() {
        // Called when the screen becomes the current screen
        if (!game.audioManager.isMenuMusicPlaying()) {
            game.audioManager.playMenuMusic();
        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage actors
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}


