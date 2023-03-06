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

    private float lastPositionX, lastPositionY;


    public PlayScreen(TimesUpTeamGame game) {
        this.game = game;

        // Initialize gameCam
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TimesUpTeamGame.V_WIDTH / TimesUpTeamGame.PPM, TimesUpTeamGame.V_HEIGHT / TimesUpTeamGame.PPM, gameCam);

        maploader = new TmxMapLoader();
        map = maploader.load("final_test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / TimesUpTeamGame.PPM);

        // Set gamecam position to center
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(Vector2.Zero, true);
        b2dr = new Box2DDebugRenderer();
        player = new Character(world);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create fixtures for walls
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / TimesUpTeamGame.PPM, (rect.getY() + rect.getHeight() / 2) / TimesUpTeamGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / TimesUpTeamGame.PPM, rect.getHeight() / 2 / TimesUpTeamGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


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

    public void update() {
        // Stop player movement, then handle user input (for possible new movement)
        player.b2Body.setLinearVelocity(Vector2.Zero);
        handleInput();

        // Take a time step (actually simulate movement, collision detection, etc.)
        world.step(1 / 60f, 6, 2);

        // Put game-cam on character
        float currentPositionX = player.b2Body.getPosition().x;
        float currentPositionY = player.b2Body.getPosition().y;
        gameCam.position.x = currentPositionX;
        gameCam.position.y = currentPositionY;

        // Update our game-cam with correct coordinates after changes
        gameCam.update();

        // Tell our renderer to draw only what our camera can see in our game world
        renderer.setView(gameCam);

        // SERVER: if player position has changed since last update, send the new position to server for broadcasting
        if (currentPositionX != lastPositionX || currentPositionY != lastPositionY) {
            game.client.sendPosition(player.b2Body.getPosition().x, player.b2Body.getPosition().y);

            lastPositionX = currentPositionX;
            lastPositionY = currentPositionY;
        }
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render our game map
        renderer.render();

        // Render our Box2DDebugLines
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

    }
}
