package com.timesupteam.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class Guard extends Enemy {
    private float stateTime;
    private TextureRegion texture;

    float x, y;

    public Guard(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 16 / TimesUpTeamGame.PPM, 16 / TimesUpTeamGame.PPM);
        stateTime = 0;
    }

    public void update(float dt) {
        // Update statetime
        stateTime += dt;

        // Update body position (x, y)
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(5.7f, 7.1f);  // starting pos of enemy - THIS IS TEMPORARY

        bdef.type = BodyDef.BodyType.StaticBody;

        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / TimesUpTeamGame.PPM, 8 / TimesUpTeamGame.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }
}
