package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.TimesUpTeamGame;

public class Walls extends InteractiveTileObject {

    public Walls(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
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
