package com.timesupteam.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.screens.PlayScreen;
import com.timesupteam.tools.KeysManager;

public class Keys extends InteractiveTileObject {
    private final KeysManager keysManager;
    private final int id;

    public Keys(PlayScreen screen, Rectangle bounds, KeysManager keysManager, int id) {
        super(screen, bounds);
        this.keysManager = keysManager;
        this.id = id;
        fixture.setUserData(this);
    }

    /**
     * Get unique id of the key.
     */
    public int getId() {
        return id;
    }

    /**
     * On any hit (TODO: make sure that it's player, not guard colliding etc.), add the key to be destroyed list.
     */
    private void onHit() {
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
