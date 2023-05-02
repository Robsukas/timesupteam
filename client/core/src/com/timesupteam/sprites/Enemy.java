package com.timesupteam.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.timesupteam.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    private Animation<TextureRegion> guardIdle;
    public Body b2Body;
    public Enemy(PlayScreen screen, float x, float y) {
        super(screen.getEnemyAtlas().findRegion("000"));
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();

}
