package com.timesupteam.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.sprites.Keys;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;
import com.timesupteam.sprites.Walls;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map, KeysManager keysManager) {
    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        // Create fixtures for walls
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Walls(screen , rect);
        }

        // Create fixtures for keys, and add each key to KeysManager
        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            keysManager.addKey(new Keys(world, map, rect, keysManager, keysManager.getKeyIdAndIncrement()));
        }
    }
}
