package com.timesupteam.screens;

import box2dLight.ConeLight;
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
import com.timesupteam.scenes.HUD;
import com.timesupteam.sprites.Character;
import com.timesupteam.sprites.Guard;
import com.timesupteam.sprites.Keys;
import com.timesupteam.tools.B2WorldCreator;
import com.timesupteam.tools.KeysManager;
import com.timesupteam.tools.WorldContactListener;

public class PlayScreen implements Screen {

    // Main variables
    private TimesUpTeamGame game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private HUD hud;

    // Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Sprites
    public Character player;
    public Guard guard;
    private float guardX, guardY;

    private KeysManager keysManager;

    // Lighting variables
    private RayHandler rayHandler;
    private ConeLight flashlight;
    private PointLight circularLight;

    // Multiplayer variables
    public MainClient client;
    public Character player2;
    private float player2X, player2Y;
    private ConeLight flashlight2;
    private PointLight circularLight2;


    public PlayScreen(TimesUpTeamGame game) {
        // Initialize texture
        atlas = new TextureAtlas("Character.pack");

        // Initialize game
        this.game = game;

        // Try to create a client, add listeners, connect to the server
        client = new MainClient(this);

        // Initialize gameCam
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TimesUpTeamGame.V_WIDTH / TimesUpTeamGame.PPM, TimesUpTeamGame.V_HEIGHT / TimesUpTeamGame.PPM, gameCam);

        // Initialize the HUD for visual key count, timer
        hud = new HUD(game.batch);

        // Load and render the map
        maploader = new TmxMapLoader();
//        map = maploader.load("level_1.tmx");
        map = maploader.load("ai_test_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / TimesUpTeamGame.PPM);

        // Set gamecam position to center
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Initialize world, debug lines renderer
        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();

        // Create walls, keys
        keysManager = new KeysManager(this);
        new B2WorldCreator(this, keysManager);

        // Create main character in to the world
        player = new Character(this, true);

        world.setContactListener(new WorldContactListener());

        // Initialize lighting
        rayHandler = new RayHandler(world);
        rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());

        RayHandler.useDiffuseLight(true);
        rayHandler.setBlurNum(2);

        // Lights for main character
        circularLight = new PointLight(rayHandler, 100, Color.WHITE, 30 / TimesUpTeamGame.PPM, 0, 0);
        circularLight.attachToBody(player.b2Body);
        circularLight.setXray(true);

        flashlight = new ConeLight(rayHandler, 100, Color.WHITE, 70 / TimesUpTeamGame.PPM, 0, 0, 0, 60);
        flashlight.attachToBody(player.b2Body, 0, 0, 0);
        flashlight.setXray(true);
        flashlight.setSoft(false);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public World getWorld() {
        return world;
    }
    public TiledMap getMap() {
        return map;
    }

    public MainClient getClient() {
        return client;
    }

    public KeysManager getKeysManager() {
        return keysManager;
    }

    @Override
    public void show() {
        game.audioManager.playPlayScreenMusic();

    }

    public void handleInput() {
        boolean moveUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean moveDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // Do nothing if player hasn't pressed any keys
        if (!moveRight && !moveUp && !moveLeft && !moveDown) {
            return;
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
            player2.b2Body.setTransform(new Vector2(player2X, player2Y), player2.b2Body.getAngle());
        }

        // Update player(s)'s (and guards') sprite location
        if (guard != null)
            guard.b2Body.setTransform(new Vector2(guardX, guardY), 0);
//            guard.update(dt);

        player.update(dt);

        if (player2 != null)
            player2.update(dt);

        // Put game-cam on main character
        float currentPositionX = player.b2Body.getPosition().x;
        float currentPositionY = player.b2Body.getPosition().y;
        gameCam.position.x = currentPositionX;
        gameCam.position.y = currentPositionY;

        // Update HUD timer, if game is running (both players have joined)
        if (TimesUpTeamGame.isRunning)
            hud.update(dt);

        // Update our game-cam with correct coordinates after changes
        gameCam.update();

        // Tell our renderer to draw only what our camera can see in our game world
        renderer.setView(gameCam);

        // SERVER: if player position has changed since last update, send the new position to server for broadcasting
        if (currentPositionX != player.lastX || currentPositionY != player.lastY) {
            player.lastX = currentPositionX;
            player.lastY = currentPositionY;

            client.sendPosition(currentPositionX, currentPositionY);
        }


        // Keys - destroy picked up key objects from world & increase key count
        keysManager.update();

        // Take a time step (actually simulate movement, collision detection, etc.)
        // World is locked during step, can't transform/destroy/... bodies at the same time
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render our game map
        renderer.render();

        // DEBUG BOX LINES: render our Box2DDebugLines
        if (TimesUpTeamGame.DEBUG.get("Box2DDebugLines"))
            b2dr.render(world, gameCam.combined);

        // Give sprite a game batch to draw itself on
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        // Draw players, with our main character on top
        if (guard != null) {
//            guard.draw(game.batch); // must have texture!
            ;
        }

        if (player2 != null) {
            player2.draw(game.batch);
        }
        player.draw(game.batch);
        //guard.draw(game.batch);
        game.batch.end();

        // Lighting
        if (TimesUpTeamGame.DEBUG.get("lights")) {
            rayHandler.setCombinedMatrix(gameCam);
            rayHandler.updateAndRender();
        }

        // Draw the HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Game over logic
        if (TimesUpTeamGame.DEBUG.get("kill when timer finishes")) {
            if (TimesUpTeamGame.isTimeUp) {  // replace with real logic
                game.setScreen(new GameOverScreen(game, "Y'all dead."));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width == 0 || height == 0)
            return;

        gamePort.update(width, height);
        rayHandler.useCustomViewport(gamePort.getScreenX(), gamePort.getScreenY(), gamePort.getScreenWidth(), gamePort.getScreenHeight());
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
        hud.dispose();
    }

    public HUD getHud() {
        return hud;
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
        player2 = new Character(this, false);

        moveSecondPlayer(x, y);

        // Create lights around 2nd player
        circularLight2 = new PointLight(rayHandler, 100, Color.WHITE, 30 / TimesUpTeamGame.PPM, 0, 0);
        circularLight2.attachToBody(player2.b2Body);
        circularLight2.setXray(true);

        flashlight2 = new ConeLight(rayHandler, 100, Color.WHITE, 70 / TimesUpTeamGame.PPM, 0, 0, 0, 60);
        flashlight2.attachToBody(player2.b2Body, 0, 0, 0);
        flashlight2.setXray(true);
        flashlight2.setSoft(false);
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

    public void createGuard(float x, float y) {
        guard = new Guard(this, x, y);

        moveGuard(x, y);
    }

    public void moveGuard(float x, float y) {
        guardX = x;
        guardY = y;
    }
}
