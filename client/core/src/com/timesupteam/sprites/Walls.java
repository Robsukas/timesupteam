package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class Walls extends InteractiveTileObject {

    public Walls(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onTopHit() {
        Gdx.app.log("Walls", "Collision on Top");
    }

    @Override
    public void onBottomHit() {
        Gdx.app.log("Walls", "Collision on Bottom");
    }

    @Override
    public void onLeftHit() {
        Gdx.app.log("Walls", "Collision on Left");
    }

    @Override
    public void onRightHit() {
        Gdx.app.log("Walls", "Collision on Right");
    }
}
