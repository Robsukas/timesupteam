package com.rahamehed.server;

import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class MapHandler {

    public Map map;
    public TileLayer wallsLayer;
    public int[][] mapArray;

    public void readInMap(String mapName, String wallsLayerName) throws Exception {
        System.out.println("Reading in new map...");
        String path = getFilePathFromResources(mapName);
        System.out.println("Path: " + path);
        map = new TMXMapReader().readMap(path);
        System.out.println("Map width: " + map.getWidth());
        System.out.println("Map height: " + map.getHeight());
        System.out.println("Map layers count: " + map.getLayerCount());

        for (MapLayer layer : map.getLayers()) {
            System.out.println("- Layer: " + layer.getName());
        }

        // Find walls layer with given name
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().equals(wallsLayerName)) {
                wallsLayer = (TileLayer) layer;
            }
        }
        if (wallsLayer == null) {
            throw new RuntimeException("Can't find walls layer '" + wallsLayerName + "'");
        }


        // Create a new int map where 1 is only when there's a wall cell (otherwise 0)
        // access: map[y][x]

        mapArray = new int[map.getHeight()][map.getWidth()];
        for (int y = 0; y < map.getHeight(); y++) {  // rows
            for (int x = 0; x < map.getWidth(); x++) {  // elements in rows
                if (wallsLayer.getTileAt(x, y) == null) continue;  // skip non-walls, 0 by default

                mapArray[y][x] = 1; // wall
            }
        }
    }

    public void printMap() {
        for (int y = 0; y < mapArray.length; y++) {  // rows
            for (int x = 0; x < mapArray[0].length; x++) {  // elements in rows
                System.out.printf(String.valueOf(mapArray[y][x]));
            }
            System.out.println();
        }
    }

    /**
     * Convert player client-side position (floats) to server-side position (ints) on tilemap.
     *
     * @param x client-side x
     * @param y client-side y
     * @return {x, y} - int array of size 2
     */
    public int[] playerPositionToInts(float x, float y) {
        int PPM = 100;
        int TILE_WIDTH = 16;
        int serverX = (int) (x * PPM / TILE_WIDTH);
        int serverY = (int) (y * PPM / TILE_WIDTH);

        serverY = map.getHeight() - 1 - serverY;  // flip player Y, since client puts origin in lower-left corner

        return new int[]{serverX, serverY};
    }

    /**
     * Convert guard server-side position (ints, in cell) to client-side position (floats for setTransform).
     *
     * @param x server-side x
     * @param y server-side y
     * @return {x, y} - float array of size 2
     */
    public float[] guardPositionToFloats(int x, int y) {
        int PPM = 100;
        int TILE_WIDTH = 16;

        float clientX = x * TILE_WIDTH / (float) PPM;
        clientX += TILE_WIDTH / 2f / PPM;

        float clientY = (y - map.getHeight() + 1) * TILE_WIDTH / (float) -PPM;
        clientY += TILE_WIDTH / 2f / PPM;

        return new float[]{clientX, clientY};
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


