package com.timesupteam.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.timesupteam.screens.PlayScreen;
import com.timesupteam.sprites.EndDoor;
import com.timesupteam.sprites.Keys;
import com.timesupteam.sprites.StartDoor;
import com.timesupteam.sprites.Walls;

public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen, KeysManager keysManager, DoorsManager doorsManager) {
        TiledMap map = screen.getMap();

        // Create wall collisions
        TiledMapTileLayer wallsLayer = (TiledMapTileLayer) map.getLayers().get("top");
        for (int x = 0; x < wallsLayer.getWidth(); x++) {
            for (int y = 0; y < wallsLayer.getHeight(); y++) {
                if (wallsLayer.getCell(x, y) == null) continue;

                Rectangle rect = new Rectangle(
                        x * wallsLayer.getTileWidth(), y * wallsLayer.getTileHeight(),
                        wallsLayer.getTileWidth(), wallsLayer.getTileHeight());
                TiledMapTileLayer.Cell cell = wallsLayer.getCell(x, y);

                new Walls(screen, rect, cell);
            }
        }

        // Create key collisions
        TiledMapTileLayer keysLayer = (TiledMapTileLayer) map.getLayers().get("keys");
        for (int x = 0; x < keysLayer.getWidth(); x++) {
            for (int y = 0; y < keysLayer.getHeight(); y++) {
                if (keysLayer.getCell(x, y) == null) continue;

                Rectangle rect = new Rectangle(
                        x * keysLayer.getTileWidth(), y * keysLayer.getTileHeight(),
                        keysLayer.getTileWidth(), keysLayer.getTileHeight());
                TiledMapTileLayer.Cell cell = keysLayer.getCell(x, y);

                keysManager.addKey(new Keys(screen, rect, cell, keysManager, keysManager.getKeyIdAndIncrement()));
            }
        }

        // Create door collisions
        TiledMapTileLayer startDoorLayer = (TiledMapTileLayer) map.getLayers().get("startdoor");
        for (int x = 0; x < startDoorLayer.getWidth(); x++) {
            for (int y = 0; y < startDoorLayer.getHeight(); y++) {
                if (startDoorLayer.getCell(x, y) == null) continue;

                Rectangle rect = new Rectangle(
                        x * startDoorLayer.getTileWidth(), y * startDoorLayer.getTileHeight(),
                        startDoorLayer.getTileWidth(), startDoorLayer.getTileHeight());

                TiledMapTileLayer.Cell cell = startDoorLayer.getCell(x, y);

                doorsManager.addStartDoor(new StartDoor(screen, rect, cell));
            }
        }

        TiledMapTileLayer endDoorLayer = (TiledMapTileLayer) map.getLayers().get("enddoor");
        for (int x = 0; x < endDoorLayer.getWidth(); x++) {
            for (int y = 0; y < endDoorLayer.getHeight(); y++) {
                if (endDoorLayer.getCell(x, y) == null) continue;

                Rectangle rect = new Rectangle(
                        x * endDoorLayer.getTileWidth(), y * endDoorLayer.getTileHeight(),
                        endDoorLayer.getTileWidth(), endDoorLayer.getTileHeight());

                TiledMapTileLayer.Cell cell = endDoorLayer.getCell(x, y);

                doorsManager.addEndDoor(new EndDoor(screen, rect, cell));
            }
        }
    }
}
