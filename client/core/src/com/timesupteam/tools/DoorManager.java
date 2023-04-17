package com.timesupteam.tools;

import com.timesupteam.sprites.EndDoor;
import com.timesupteam.sprites.StartDoor;

import java.util.ArrayList;
import java.util.List;

public class DoorManager {

    private List<StartDoor> startDoorList = new ArrayList<>();
    private List<EndDoor> endDoorList = new ArrayList<>();

    public void addStartDoor(StartDoor door) {
        if (!startDoorList.contains(door)) {
            startDoorList.add(door);
        }
    }

    public void addEndDoor(EndDoor door) {
        if (!endDoorList.contains(door)) {
            endDoorList.add(door);
        }
    }

    public void deleteStartDoors() {
        for (StartDoor door : startDoorList) {

        }
    }
}
