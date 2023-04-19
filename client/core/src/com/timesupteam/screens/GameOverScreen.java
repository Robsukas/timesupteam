package com.timesupteam.screens;

import com.badlogic.gdx.Game;
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

/**
 * Screen to display when game is over (guards caught player/time is up).
 */
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private TimesUpTeamGame game;

    public GameOverScreen(final TimesUpTeamGame game, String text) {
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
        Label gameOverLabel = new Label(text, font);
        table.add(gameOverLabel).expandX();

        stage.addActor(table);
        stage.addActor(menuButton);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        game.audioManager.playGameOverMusic();


    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
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

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
