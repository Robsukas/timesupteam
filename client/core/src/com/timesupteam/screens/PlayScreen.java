package com.timesupteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.sprites.Character;
import com.timesupteam.tools.B2WorldCreator;

public class PlayScreen implements Screen {

    // Main variables
    private TimesUpTeamGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    // Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Character player;


    public PlayScreen(TimesUpTeamGame game) {
        this.game = game;

        // Initialize gameCam
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TimesUpTeamGame.V_WIDTH / TimesUpTeamGame.PPM, TimesUpTeamGame.V_HEIGHT / TimesUpTeamGame.PPM, gameCam);

        maploader = new TmxMapLoader();
        map = maploader.load("test_level_1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / TimesUpTeamGame.PPM);

        // Set gamecam position to center
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        player = new Character(world);

    }

    @Override
    public void show() {

    }

    public void handleInput() {
        float speed = 2.0f;

        boolean moveUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean moveDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (moveUp) {
            player.b2Body.applyLinearImpulse(new Vector2(0, 0.3f), player.b2Body.getWorldCenter(), true);

        }
        if (moveLeft) {
            player.b2Body.applyLinearImpulse(new Vector2(-0.3f, 0), player.b2Body.getWorldCenter(), true);
        }
        if (moveDown) {
            player.b2Body.applyLinearImpulse(new Vector2(0, -0.3f), player.b2Body.getWorldCenter(), true);
        }
        if (moveRight) {
            player.b2Body.applyLinearImpulse(new Vector2(0.3f, 0), player.b2Body.getWorldCenter(), true);
        }
        System.out.println("X = " + player.b2Body.getPosition().x * 100);
        System.out.println("Y = " + player.b2Body.getPosition().y * 100);
    }

    public void update() {
        //handle user input first
        handleInput();
        player.b2Body.setLinearDamping(20);

        world.step(1 / 60f, 6, 2);

        // Put gamecam on character
        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.position.y = player.b2Body.getPosition().y;

        //update our gamecam with correct coordinates after changes
        gameCam.update();

        //tell our renderer to draw only what our camera can see in our game world
        renderer.setView(gameCam);

    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //render our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
