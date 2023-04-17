package com.timesupteam.tools;

import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.MainClient;
import com.timesupteam.scenes.HUD;
import com.timesupteam.screens.PlayScreen;
import com.timesupteam.sprites.EndDoor;
import com.timesupteam.sprites.StartDoor;

import java.util.ArrayList;
import java.util.List;

public class DoorsManager {

    private World world;
    private MainClient client;

    private List<StartDoor> startDoors = new ArrayList<>();
    private List<StartDoor> startDoorsToBeDestroyed;

    private List<EndDoor> endDoors = new ArrayList<>();
    private List<EndDoor> endDoorsToBeDestroyed;

    public DoorsManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.client = screen.getClient();
    }

    public void addStartDoor(StartDoor door) {
        startDoors.add(door);
    }

    public void addEndDoor(EndDoor door) {
        endDoors.add(door);
    }

    /**
     * Destroys the start door (puts in queue).
     */
    public void openStartDoor() {
        startDoorsToBeDestroyed = startDoors;
    }


    /**
     * Destroys the start door (puts in queue).
     */
    public void openEndDoor() {
        endDoorsToBeDestroyed = endDoors;
    }

    /**
     * Destroy all doors that need to be destroyed.
     * Called in PlayScreen.update().
     */
    public void update() {
        if (startDoorsToBeDestroyed != null) {
            for (StartDoor door : startDoorsToBeDestroyed) {
                door.getCell().setTile(null);
                world.destroyBody(door.body);
            }
            startDoorsToBeDestroyed.clear();
        }

        if (endDoorsToBeDestroyed != null) {
            for (EndDoor door : endDoorsToBeDestroyed) {
                door.getCell().setTile(null);
                world.destroyBody(door.body);
            }
            endDoorsToBeDestroyed.clear();
        }
    }
}
