package com.timesupteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.timesupteam.TimesUpTeamGame;

public class SettingsScreen implements Screen {

    private TimesUpTeamGame game;
    private Stage stage;
    private Skin skin;

    public SettingsScreen(final TimesUpTeamGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(
                TimesUpTeamGame.V_WIDTH, TimesUpTeamGame.V_HEIGHT
        ));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Label titleLabel = new Label("Settings", skin);
        titleLabel.setPosition(stage.getWidth() / 2 - titleLabel.getWidth() / 2, stage.getHeight() * 0.8f);

        Label volumeLabel = new Label("Sound Volume", skin);
        volumeLabel.setPosition(stage.getWidth() / 2 - volumeLabel.getWidth() / 2, stage.getHeight() * 0.6f);

        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        volumeSlider.setPosition(stage.getWidth() / 2 - volumeSlider.getWidth() / 2, stage.getHeight() * 0.5f);

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = ((Slider) actor).getValue();
                game.audioManager.setAllMusicVolume(volume);
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(stage.getWidth() * 0.1f, stage.getHeight() * 0.1f);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(titleLabel);
        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}

