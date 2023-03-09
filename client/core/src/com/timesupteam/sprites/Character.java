package com.timesupteam.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class Character extends Sprite {
    public World world;
    public Body b2Body;
    private TextureRegion characterIdle;

    public Character(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("tile000"));
        this.world = world;
        defineCharacter();

        // Initialize character texture region
        characterIdle = new TextureRegion(getTexture(), 1, 1, 16, 16);

        // Create bounds and texture region for character
        setBounds(0, 0, 16 / TimesUpTeamGame.PPM, 16 / TimesUpTeamGame.PPM);
        setRegion(characterIdle);
    }

    public void update(float dt) {
        // Set the position of the sprite on the b2body center
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
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
