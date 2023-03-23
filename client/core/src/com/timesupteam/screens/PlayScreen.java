package com.timesupteam.screens;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.timesupteam.MainClient;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.sprites.Character;
import com.timesupteam.tools.B2WorldCreator;
import com.timesupteam.tools.WorldContactListener;

public class PlayScreen implements Screen {

    // Main variables
    private TimesUpTeamGame game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    // Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    public Character player;

    // Lighting variables
//    private HackLightEngine lightEngine;
//    private HackLight fogLight, libgdxLight;

    private RayHandler rayHandler;
    private ConeLight testLight2;

    // Multiplayer variables
    public MainClient client;
    public Character player2;
    private float player2X, player2Y;

    public PlayScreen(TimesUpTeamGame game) {
        // Initialize texture
        atlas = new TextureAtlas("Character.pack");

        // Initialize game
        this.game = game;

        // Initialize gameCam
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TimesUpTeamGame.V_WIDTH / TimesUpTeamGame.PPM, TimesUpTeamGame.V_HEIGHT / TimesUpTeamGame.PPM, gameCam);

        maploader = new TmxMapLoader();
        map = maploader.load("level_1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / TimesUpTeamGame.PPM);

        // Set gamecam position to center
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        // Create main character in to the world
        player = new Character(world, this, true);

        // Try to create a client, add listeners, connect to the server
        client = new MainClient(this);

        world.setContactListener(new WorldContactListener());

        // Lighting
        rayHandler = new RayHandler(world);
//        rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());
//        rayHandler.setCombinedMatrix(gameCam.combined);
        RayHandler.useDiffuseLight(true);


        PointLight testLight = new PointLight(rayHandler, 100, Color.WHITE, 30 / TimesUpTeamGame.PPM, player.b2Body.getPosition().x, player.b2Body.getPosition().y);
        testLight.attachToBody(player.b2Body);
        testLight.setXray(true);

        testLight2 = new ConeLight(rayHandler, 100, Color.WHITE, 70 / TimesUpTeamGame.PPM, 0, 0, 0, 60);
        testLight2.attachToBody(player.b2Body, 0, 0, 0);

        testLight2.setSoftnessLength(0f);
        testLight2.setXray(true);
//        testLight2.setXray(true);
//        rayHandler.setAmbientLight(0.5f);
//        rayHandler.setBlurNum(3);

//        PointLight pl = new PointLight(rayHandler, 128, new Color(0.2f, 1, 1, 1f), 10, -5, 2);
//        PointLight pl2 = new PointLight(rayHandler, 128, new Color(1, 0, 1, 1f), 10, 5, 2);
//
//        pl.attachToBody(player.b2Body);

//        player.b2Body =
//        PointLight light = new PointLight(rayHandler, 120, Color.WHITE, 7, 10, 10);
//        light.setSoftnessLength(0f);
//        light.setXray(false);
//        light.attachToBody(player.b2Body);

//        rayHandler.setShadows(true);
//        pl.setStaticLight(false);
//        pl.setSoft(true);


    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput() {
        boolean moveUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean moveDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // Do nothing if player hasn't moved
        if (!moveRight && !moveUp && !moveLeft && !moveDown) {
            return;
        }



        if (moveUp) {
            player.b2Body.setTransform(player.b2Body.getPosition(), (float) Math.toRadians(90.0f));
        } else if (moveDown) {
            player.b2Body.setTransform(player.b2Body.getPosition(), (float) Math.toRadians(270.0f));
        } else if (moveLeft) {
            player.b2Body.setTransform(player.b2Body.getPosition(), (float) Math.toRadians(180.0f));
        } else if (moveRight) {
            player.b2Body.setTransform(player.b2Body.getPosition(), (float) Math.toRadians(0.0f));
        }

        // Otherwise, calculate & apply force to player body
        float moveX = 0f;
        float moveY = 0f;

        if (moveRight) moveX = 1f;
        if (moveLeft) moveX = -1f;
        if (moveUp) moveY = 1f;
        if (moveDown) moveY = -1f;

        Vector2 moveDir = new Vector2(moveX, moveY).nor();  // normalized => diagonal movement is not faster
        float moveSpeed = 0.75f;
        moveDir.x *= moveSpeed;
        moveDir.y *= moveSpeed;

        player.b2Body.applyLinearImpulse(moveDir, player.b2Body.getWorldCenter(), true);
    }

    public void update(float dt) {
        // Stop character(s)'s movement, then handle user input (for possible new movement)
        player.b2Body.setLinearVelocity(Vector2.Zero);

        if (player2 != null)
            player2.b2Body.setLinearVelocity(Vector2.Zero);

        // Apply main character's movement
        handleInput();

        // Apply second player's movement
        if (player2 != null) {
            player2.lastX = player2.b2Body.getPosition().x;
            player2.lastY = player2.b2Body.getPosition().y;
            player2.b2Body.setTransform(new Vector2(player2X, player2Y), 0f);
        }

        // Update player(s)'s sprite location
        player.update(dt);

        if (player2 != null)
            player2.update(dt);

        // Put game-cam on main character
        float currentPositionX = player.b2Body.getPosition().x;
        float currentPositionY = player.b2Body.getPosition().y;
        gameCam.position.x = currentPositionX;
        gameCam.position.y = currentPositionY;

        // Update our game-cam with correct coordinates after changes
        gameCam.update();

        // Tell our renderer to draw only what our camera can see in our game world
        renderer.setView(gameCam);

        // SERVER: if player position has changed since last update, send the new position to server for broadcasting
        if (currentPositionX != player.lastX || currentPositionY != player.lastY) {
            client.sendPosition(currentPositionX, currentPositionY);

            player.lastX = currentPositionX;
            player.lastY = currentPositionY;
        }
//
//        // Lighting
//        rayHandler.update();

        // Take a time step (actually simulate movement, collision detection, etc.)
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        // Render our game map
        renderer.render();

        // DEBUG BOX LINES: render our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        // Give sprite a game batch to draw itself on
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        // Draw players, with our main character on top
        if (player2 != null) {
            player2.draw(game.batch);
        }
        player.draw(game.batch);

        game.batch.end();

        // Lighting
        rayHandler.setCombinedMatrix(gameCam);
//        rayHandler.render();
        rayHandler.updateAndRender();  // this stretches???
    }

    @Override
    public void resize(int width, int height) {
//        if (width == 0 || height == 0)
//            return;

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
        rayHandler.dispose();
    }

    /**
     * Create a second Character to our world with given coordinates.
     * Called from MainClient when we get a MovePlayer event
     * about another player from the server.
     *
     * @param x x
     * @param y y
     */
    public void createSecondPlayer(float x, float y) {
        player2 = new Character(world, this, false);
        moveSecondPlayer(x, y);
    }

    /**
     * Move second player directly to given coordinates.
     * Set last x and y for animation purposes (done in Character).
     *
     * @param x x
     * @param y y
     */
    public void moveSecondPlayer(float x, float y) {
        player2X = x;
        player2Y = y;
    }
}
