package com.timesupteam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timesupteam.screens.PlayScreen;


public class TimesUpTeamGame extends Game {

    // Virtual width and height (camera size = zoom-in level)
    public static final int V_WIDTH = 512;
    public static final int V_HEIGHT = 288;

    // PPM - pixels per meter
    public static final float PPM = 100;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        // Exit application when game window closed. That way everything gets exited, like networking
        System.exit(0);
    }
}
