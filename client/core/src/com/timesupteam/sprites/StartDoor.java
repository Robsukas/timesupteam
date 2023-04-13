package com.timesupteam.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.timesupteam.screens.PlayScreen;

public class StartDoor extends InteractiveTileObject{
    public StartDoor(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
    }

    public void destroyDoor() {

    }
}
