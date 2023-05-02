package com.timesupteam.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class Guard extends Enemy {

    float x, y;
    private float stateTimer;
    private Animation<TextureRegion> guardIdle;

    public Guard(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 16 / TimesUpTeamGame.PPM, 16 / TimesUpTeamGame.PPM);

        // Guard idle animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 1, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 19, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 37, 1, 16, 16));
        frames.add(new TextureRegion(getTexture(), 55, 1, 16, 16));
        guardIdle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        // Set the position of the sprite on the b2body center
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);

        // Set the frame for the guard
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = guardIdle.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return region;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(9.049289f, 3.764999f);  // starting pos of enemy - THIS IS TEMPORARY

        bdef.type = BodyDef.BodyType.StaticBody;

        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / TimesUpTeamGame.PPM, 8 / TimesUpTeamGame.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }
}
