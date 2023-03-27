package com.timesupteam.tools;

import com.badlogic.gdx.physics.box2d.World;
import com.timesupteam.MainClient;
import com.timesupteam.scenes.HUD;
import com.timesupteam.screens.PlayScreen;
import com.timesupteam.sprites.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the destruction of keys.
 */
public class KeysManager {

    private World world;
    private HUD hud;
    private MainClient client;

    private List<Keys> keys = new ArrayList<>();
    private List<Keys> keysToBeDestroyed = new ArrayList<>();
    private static int keyId = 0;  // each key has a unique id, so we can send KeyPicked event to the server with key id

    public KeysManager(PlayScreen screen) {
        this.world = screen.getWorld();
        this.hud = screen.getHud();
        this.client = screen.getClient();
    }

    public int getKeyIdAndIncrement() {
        return keyId++;
    }

    public void addKey(Keys key) {
        if (!keys.contains(key)) {
            keys.add(key);
        }
    }

    /**
     * Add a Keys object to be destroyed in the next update cycle, and send KeyPicked event to the server.
     * Shall be called when this player picked the key.
     *
     * @param key Keys object to be destroyed
     */
    public void addKeyToBeDestroyed(Keys key) {
        if (!keysToBeDestroyed.contains(key)) {
            keysToBeDestroyed.add(key);

            client.sendKeyPicked(key.getId());
        }
    }

    /**
     * Add a Keys object to be destroyed in the next update cycle.
     * Shall be called when receiving KeyPicked event from the server (other player got the key).
     *
     * @param id id of the Keys object to be destroyed
     */
    public void addKeyToBeDestroyed(int id) {
        for (Keys key : keys) {
            if (key.getId() == id) {
                if (!keysToBeDestroyed.contains(key)) {
                    keysToBeDestroyed.add(key);
                }
                return;
            }
        }
    }

    /**
     * Destroy all keys in to be destroyed list & clear the list. Update keys count.
     * Shall be called from PlayScreen.update()
     */
    public void update() {
        for (Keys key : keysToBeDestroyed) {
            key.getCell().setTile(null);
            world.destroyBody(key.body);
            hud.addKeyCount(1);
        }
        keysToBeDestroyed.clear();
    }
}
