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

    public boolean isOpened = false;

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
        System.out.println("added door");
        System.out.println(endDoors.size());
    }

    /**
     * Destroys the start door (puts in queue).
     */
    public void openStartDoor() {
        startDoorsToBeDestroyed = startDoors;
    }


    /**
     * Destroys the end door (puts in queue).
     */
    public void openEndDoor() {
//        endDoorsToBeDestroyed = endDoors;
        System.out.println(endDoors.size());

        EndDoor door1 = null;
        EndDoor door2 = null;
        EndDoor door3 = null;
        EndDoor door4 = null;

        door1 = endDoors.get(0);
        door2 = endDoors.get(1);
        door3 = endDoors.get(2);
        door4 = endDoors.get(3);


        endDoorsToBeDestroyed.add(door1);
        endDoorsToBeDestroyed.add(door2);
        endDoorsToBeDestroyed.add(door3);
        endDoorsToBeDestroyed.add(door4);

        endDoors.remove(door1);
        endDoors.remove(door2);
        endDoors.remove(door3);
        endDoors.remove(door4);
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
