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
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;

    private TiledMapTileLayer.Cell cell;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds, TiledMapTileLayer.Cell cell) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        this.cell = cell;

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

    public TiledMapTileLayer.Cell getCell(/*String layerName*/){
//        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
//        return layer.getCell((int)(body.getPosition().x * TimesUpTeamGame.PPM / layer.getTileWidth()),
//                (int)(body.getPosition().y * TimesUpTeamGame.PPM / layer.getTileHeight()));
        return cell;
    }

}
