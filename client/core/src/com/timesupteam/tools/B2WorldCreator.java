package com.timesupteam.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.sprites.Keys;
import com.timesupteam.sprites.Walls;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map, KeysManager keysManager) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create fixtures for walls
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Walls(world, map, rect);
        }

        // Create fixtures for keys
        int id = 0;  // each key has a unique id, so we can send KeyPicked event to the server with key id
        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            keysManager.addKey(new Keys(world, map, rect, keysManager, id++));
        }
    }
}
