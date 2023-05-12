package com.timesupteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.timesupteam.TimesUpTeamGame;

public class WinGameScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private TimesUpTeamGame game;
    public WinGameScreen(final TimesUpTeamGame game, String text) {
        this.game = game;
        viewport = new FitViewport(TimesUpTeamGame.V_WIDTH, TimesUpTeamGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.setPosition(10, 10);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.playClickSound();
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Label.LabelStyle font = new Label.LabelStyle((new BitmapFont()), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);  // table will take up the entire stage
        Label gameWinLabel = new Label(text, font);
        table.add(gameWinLabel).expandX();

        stage.addActor(table);
        stage.addActor(menuButton);
    }
    @Override
    public void show() {
        game.audioManager.playWinGameMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);  // black color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // clears the screen

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
    public void dispose() {stage.dispose();
    }
}
