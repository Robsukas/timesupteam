package com.rahamehed.server;

import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TmxMapLoader {

    public boolean[][] readInMap(String mapName, String wallsLayerName) throws Exception {
        System.out.println("Reading in new map...");
        String path = getFilePathFromResources(mapName);
        System.out.println("Path: " + path);
        Map map = new TMXMapReader().readMap(path);
        System.out.println("Map width: " + map.getWidth());
        System.out.println("Map height: " + map.getHeight());
        System.out.println("Map layers count: " +map.getLayerCount());

        for (MapLayer layer : map.getLayers()) {
            System.out.println("- Layer: " + layer.getName());
        }

        // Find walls layer
        TileLayer wallsLayer = null;
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().equals(wallsLayerName)) {
                wallsLayer = (TileLayer) layer;
            }
        }

        if (wallsLayer == null) {
            throw new RuntimeException("Can't find walls layer '" + wallsLayerName + "'");
        }

        // Create and return a boolean map where true is only when there's a wall cell
        boolean[][] mapArray = new boolean[map.getWidth()][map.getHeight()];
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (wallsLayer.getTileAt(x, y) != null) {
                    mapArray[x][y] = true;
                }
            }
        }

        return mapArray;
    }

    /**
     * Get absolute path of given filename as a String.
     * The file should be in src/main/resources.
     *
     * @param filename filename
     * @return absolute path containing filename
     */
    private String getFilePathFromResources(String filename) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        if (url == null) {
            throw new RuntimeException("Can't find file '" + filename + "'. It should be in src/main/resources.");
        }
        try {
            return Path.of(url.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}


