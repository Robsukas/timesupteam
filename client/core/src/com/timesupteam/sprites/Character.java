package com.timesupteam.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class Character extends Sprite {

    public enum State {RUNNINGUP, RUNNINGDOWN, RUNNINGLEFT, RUNNINGRIGHT, IDLEUP, IDLEDOWN, IDLELEFT, IDLERIGHT}

    public State currentState;
    public State previousState;
    public World world;
    public Body b2Body;
    private TextureRegion characterIdleDown;
    private TextureRegion characterIdleLeft;
    private TextureRegion characterIdleRight;
    private TextureRegion characterIdleUp;

    private Animation<TextureRegion> characterRunDown;
    private Animation<TextureRegion> characterRunUp;
    private Animation<TextureRegion> characterRunLeft;
    private Animation<TextureRegion> characterRunRight;
    private float stateTimer;

    private boolean isMainCharacter;
    public float lastX, lastY;

    public Character(World world, PlayScreen screen, boolean isMainCharacter) {
        super(screen.getAtlas().findRegion("000"));
        this.world = world;
        this.isMainCharacter = isMainCharacter;

        defineCharacter();

        currentState = State.IDLEDOWN;
        previousState = State.IDLEDOWN;
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Initialize character animation regions

        // DOWN
        frames.add(new TextureRegion(getTexture(), 19, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 37, 1, 16, 16));
        characterRunDown = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // LEFT
        frames.add(new TextureRegion(getTexture(), 73, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 91, 1, 16, 16));
        characterRunLeft = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // RIGHT
        frames.add(new TextureRegion(getTexture(), 181, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 199, 1, 16, 16));
        characterRunRight = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // UP
        frames.add(new TextureRegion(getTexture(), 127, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 145, 1, 16, 16));
        characterRunUp = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // Initialize character idle regions
        characterIdleDown = new TextureRegion(getTexture(), 1, 1, 16, 16);
        characterIdleUp = new TextureRegion(getTexture(), 109, 1, 16, 16);
        characterIdleLeft = new TextureRegion(getTexture(), 55, 1, 16, 16);
        characterIdleRight = new TextureRegion(getTexture(), 163, 1, 16, 16);

        // Create bounds and texture region for character
        setBounds(0, 0, 16 / TimesUpTeamGame.PPM, 16 / TimesUpTeamGame.PPM);
    }

    public void update(float dt) {
        // Set the position of the sprite on the b2body center
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);

        // Set the frame for the player
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNINGUP:
                region = characterRunUp.getKeyFrame(stateTimer, true);
                break;
            case RUNNINGDOWN:
                region = characterRunDown.getKeyFrame(stateTimer, true);
                break;
            case RUNNINGLEFT:
                region = characterRunLeft.getKeyFrame(stateTimer, true);
                break;
            case RUNNINGRIGHT:
                region = characterRunRight.getKeyFrame(stateTimer, true);
                break;
            case IDLEUP:
                region = characterIdleUp;
                break;
            case IDLELEFT:
                region = characterIdleLeft;
                break;
            case IDLERIGHT:
                region = characterIdleRight;
                break;
            case IDLEDOWN:
            default:
                region = characterIdleDown;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {

        if (isMainCharacter) {
            if (b2Body.getLinearVelocity().y > 0) {
                return State.RUNNINGUP;
            } else if (b2Body.getLinearVelocity().y < 0) {
                return State.RUNNINGDOWN;
            } else if (b2Body.getLinearVelocity().x > 0) {
                return State.RUNNINGRIGHT;
            } else if (b2Body.getLinearVelocity().x < 0) {
                return State.RUNNINGLEFT;
            } else {
                if (previousState == State.RUNNINGUP) {
                    return State.IDLEUP;
                } else if (previousState == State.RUNNINGLEFT) {
                    return State.IDLELEFT;
                } else if (previousState == State.RUNNINGRIGHT) {
                    return State.IDLERIGHT;
                } else if (previousState == State.RUNNINGDOWN) {
                    return State.IDLEDOWN;
                } else {
                    return previousState;
                }
            }
        }

        // A bit different logic for animating the other player
        if (b2Body.getPosition().x < lastX) {
            lastX = b2Body.getPosition().x;
            return State.RUNNINGLEFT;
        } else if (b2Body.getPosition().x > lastX) {
            lastX = b2Body.getPosition().x;
            return State.RUNNINGRIGHT;
        } else if (b2Body.getPosition().y > lastY) {
            lastY = b2Body.getPosition().y;
            return State.RUNNINGUP;
        } else if (b2Body.getPosition().y < lastY) {
            lastY = b2Body.getPosition().y;
            return State.RUNNINGDOWN;
        } else {
            if (previousState == State.RUNNINGUP) {
                return State.IDLEUP;
            } else if (previousState == State.RUNNINGLEFT) {
                return State.IDLELEFT;
            } else if (previousState == State.RUNNINGRIGHT) {
                return State.IDLERIGHT;
            } else if (previousState == State.RUNNINGDOWN) {
                return State.IDLEDOWN;
            } else {
                return previousState;
            }
        }
    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(5.68f, 5.87f);  // starting pos of characters

        lastX = bdef.position.x;
        lastY = bdef.position.y;

        bdef.type = BodyDef.BodyType.DynamicBody;

        b2Body = world.createBody(bdef);


        if (!isMainCharacter)
            // Don't use collision box for second player, since their position is updated directly
            // (avoid unnecessary calculations)
            return;
            // fdef.isSensor = true;  // it's another player, disable their collisions with the world

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(7 / TimesUpTeamGame.PPM, 8 / TimesUpTeamGame.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }
}
