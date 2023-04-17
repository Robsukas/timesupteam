package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.timesupteam.screens.PlayScreen;

public class StartDoor extends InteractiveTileObject{
    public StartDoor(PlayScreen screen, Rectangle bounds, TiledMapTileLayer.Cell cell) {
        super(screen, bounds, cell);
        fixture.setUserData(this);
    }


    /**
     * On any hit (TODO: make sure that it's player, not guard colliding etc.),
     */
    private void onHit() {
//        this.getCell("StartDoor").setTile(null);
    }

    @Override
    public void onTopHit() {
        Gdx.app.log("StartDoor", "Collision on Top");
        onHit();
    }

    @Override
    public void onBottomHit() {
        Gdx.app.log("StartDoor", "Collision on Bottom");
        onHit();
    }

    @Override
    public void onLeftHit() {
        Gdx.app.log("StartDoor", "Collision on Left");
        onHit();
    }

    @Override
    public void onRightHit() {
        Gdx.app.log("StartDoor", "Collision on Right");
        onHit();
    }
}
