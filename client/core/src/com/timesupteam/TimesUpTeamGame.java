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

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

		// Try to connect to the server
		try {
			new MainClient();
		} catch (IOException e) {
			System.err.println("--- IOException; seems like ports may be already in use. " +
					"Did you try to close game instances?");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void render () {
		super.render();
	}
}
