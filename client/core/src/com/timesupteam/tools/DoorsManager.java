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

    private PlayScreen screen;
    private World world;
    private MainClient client;

    private List<StartDoor> startDoors = new ArrayList<>();
    private List<StartDoor> startDoorsToBeDestroyed;

    private List<EndDoor> endDoors = new ArrayList<>();
    private List<EndDoor> endDoorsToBeDestroyed = new ArrayList<>();

    public DoorsManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.client = screen.getClient();
        this.screen = screen;
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
//        endDoorsToBeDestroyed = endDoors;
        endDoorsToBeDestroyed.add(endDoors.remove(0));
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
                // Play the open door sound when start door is opened.
                screen.game.audioManager.playOpenDoorSound();
            }
            startDoorsToBeDestroyed.clear();
        }

        if (endDoorsToBeDestroyed != null) {
            for (EndDoor door : endDoorsToBeDestroyed) {
                door.getCell().setTile(null);
                world.destroyBody(door.body);
                // Play the open door sound when end door is opened.
                screen.game.audioManager.playOpenDoorSound();
            }
            endDoorsToBeDestroyed.clear();

        }
    }
}
