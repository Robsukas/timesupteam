package com.timesupteam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.timesupteam.screens.PlayScreen;

import java.io.IOException;

public class TimesUpTeamGame extends Game {

    // Virtual width and height
    public static final int V_WIDTH = 512;
    public static final int V_HEIGHT = 288;

    // PPM - pixels per meter
    public static final float PPM = 100;

    public SpriteBatch batch;

    public MainClient client;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));

        // Try to create a client, add listeners, connect to the server
        client = new MainClient();
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
