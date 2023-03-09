package com.timesupteam.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.TimesUpTeamGame;

public class Character extends Sprite {
    public World world;
    public Body b2Body;

    private static final float playerDensity = 1.0f;

    public Character(World world) {
        this.world = world;
        defineCharacter();
    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(5.68f, 5.87f);
        bdef.type = BodyDef.BodyType.DynamicBody;
        //bdef.fixedRotation = true;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(3 / TimesUpTeamGame.PPM, 3 / TimesUpTeamGame.PPM);
        fdef.shape = shape;
        //fdef.density = playerDensity;
        b2Body.createFixture(fdef);
    }
}
