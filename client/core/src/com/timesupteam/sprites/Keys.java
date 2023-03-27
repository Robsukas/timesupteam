package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.tools.KeysManager;

public class Keys extends InteractiveTileObject {
    private final KeysManager keysManager;
    private final int id;

    public Keys(World world, TiledMap map, Rectangle bounds, KeysManager keysManager, int id) {
        super(world, map, bounds);
        this.keysManager = keysManager;
        this.id = id;
        fixture.setUserData(this);
    }

    public int getId() {
        return id;
    }

    /**
     * On any hit.
     */
    private void onHit() {
        getCell().setTile(null);
        keysManager.addKeyToBeDestroyed(this);
    }

    @Override
    public void onTopHit() {
        Gdx.app.log("Keys", "Collision on Top");
        onHit();
    }

    @Override
    public void onBottomHit() {
        Gdx.app.log("Keys", "Collision on Bottom");
        onHit();
    }

    @Override
    public void onLeftHit() {
        Gdx.app.log("Keys", "Collision on Left");
        onHit();
    }

    @Override
    public void onRightHit() {
        Gdx.app.log("Keys", "Collision on Right");
        onHit();
    }
}
