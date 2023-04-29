package com.timesupteam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timesupteam.screens.MenuScreen;
import com.timesupteam.tools.AudioManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.timesupteam.screens.PlayScreen;

import java.util.HashMap;
import java.util.Map;


public class TimesUpTeamGame extends Game {

    public AudioManager audioManager;

    // Enable to disable lighting effects, zoom out, enable visible collision boxes etc.
    public static Map<String, Boolean> DEBUG = new HashMap<>();
    static {
        DEBUG.put("zoom", false);
        DEBUG.put("lights", false);
        DEBUG.put("Box2DDebugLines", true);
        DEBUG.put("kill when timer finishes", false);
        DEBUG.put("music", false);
        DEBUG.put("open door", true);
    }

    // Whether game is running
    public static boolean isRunning = false;  // 2 players have joined
    public static boolean isTimeUp = false;  // timers is up

    // Virtual width and height (camera size = zoom-in level). The smaller, the more zoomed in
    public static int V_WIDTH = 512;
    public static int V_HEIGHT = 288;

    // PPM - pixels per meter
    public static final float PPM = 100;

    public SpriteBatch batch;

    @Override
    public void create() {
        audioManager = new AudioManager();
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        audioManager.dispose();
        // Exit application when game window closed. That way everything gets exited, like networking
        System.exit(0);
    }
}
