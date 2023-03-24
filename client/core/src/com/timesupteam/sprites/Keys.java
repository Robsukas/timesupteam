package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Keys extends InteractiveTileObject {
    public Keys(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onTopHit() {
        Gdx.app.log("Keys", "Collision on Top");
        getCell().setTile(null);
    }

    @Override
    public void onBottomHit() {
        Gdx.app.log("Keys", "Collision on Bottom");
        getCell().setTile(null);
    }

    @Override
    public void onLeftHit() {
        Gdx.app.log("Keys", "Collision on Left");
        getCell().setTile(null);
    }

    @Override
    public void onRightHit() {
        Gdx.app.log("Keys", "Collision on Right");
        getCell().setTile(null);
    }
}
