package com.timesupteam.sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.timesupteam.screens.PlayScreen;

public class EndDoor extends InteractiveTileObject{
    public EndDoor(PlayScreen screen, Rectangle bounds, TiledMapTileLayer.Cell cell) {
        super(screen, bounds, cell);
        fixture.setUserData(this);
    }
}
