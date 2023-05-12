package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.timesupteam.screens.PlayScreen;

public class Walls extends InteractiveTileObject {

    public Walls(PlayScreen screen, Rectangle bounds, TiledMapTileLayer.Cell cell) {
        super(screen, bounds, cell);
        fixture.setUserData(this);
    }

    @Override
    public void onTopHit() {
//        Gdx.app.log("Walls", "Collision on Top");
    }

    @Override
    public void onBottomHit() {
//        Gdx.app.log("Walls", "Collision on Bottom");
    }

    @Override
    public void onLeftHit() {
//        Gdx.app.log("Walls", "Collision on Left");
    }

    @Override
    public void onRightHit() {
//        Gdx.app.log("Walls", "Collision on Right");
    }
}
