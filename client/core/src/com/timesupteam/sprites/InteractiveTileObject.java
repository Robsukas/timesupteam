package com.timesupteam.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.timesupteam.TimesUpTeamGame;
import com.timesupteam.screens.PlayScreen;

public class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / TimesUpTeamGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / TimesUpTeamGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / TimesUpTeamGame.PPM, bounds.getHeight() / 2 / TimesUpTeamGame.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        fixture = body.createFixture(fdef);
    }


    public void onTopHit() {}

    public void onBottomHit() {}

    public void onLeftHit() {}

    public void onRightHit() {}

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * TimesUpTeamGame.PPM / 16),
                (int)(body.getPosition().y * TimesUpTeamGame.PPM / 16));
    }

}
