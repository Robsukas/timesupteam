package com.timesupteam.sprites;

import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

import java.sql.Time;

public class Guard extends Enemy {

    float x, y;

    public Guard(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 16 / TimesUpTeamGame.PPM, 16 / TimesUpTeamGame.PPM);
    }

    public void update(float dt) {
        // Set the position of the sprite on the b2body center
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
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

        b2Body.createFixture(fdef).setUserData(this);
        fdef.isSensor = true;
    }

    public void onHit() {
        // Guard has hit player
//        TimesUpTeamGame.isTimeUp = true;
    }
}
