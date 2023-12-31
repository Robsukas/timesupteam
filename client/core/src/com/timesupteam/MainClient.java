package com.timesupteam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.timesupteam.screens.PlayScreen;

import java.io.IOException;

public class MainClient {

    private final Client client;

    private final String SERVER_IP = "193.40.156.59";// "193.40.156.59"; // "localhost";  //
    private final int TCP_PORT = 8080;  // must be the same on server
    private final int UDP_PORT = 8081;  // must be the same on server
    private final PlayScreen screen;

    public MainClient(final PlayScreen screen) {
        this.screen = screen;
        // Set logging level
        Log.set(Log.LEVEL_INFO);

        // Initialize client & register all classes that are to be sent over the network
        client = new Client();
        client.start();
        Network.register(client);

        // ThreadedListener runs the listener methods on a different thread, so non-blocking
        client.addListener(new Listener.ThreadedListener(new Listener() {
            public void connected(Connection connection) {
                System.out.println("!-- Connected to the server.");
                System.out.println("--- Our ID: " + connection.getID());
                System.out.println();
            }

            public void disconnected(Connection connection) {
                System.out.println("!-- Disconnected from the server.");
                System.out.println("--- Our ID: " + connection.getID());
                System.out.println();

                // When connection lost, game is over
                TimesUpTeamGame.isRunning = false;
                TimesUpTeamGame.isTimeUp = true;
            }

            public void received(Connection connection, Object object) {
                if (object == null) {
                    return;
                }

                if (object instanceof Network.GameStart) {
                    // Another player has joined, start game timer and open start door
                    System.out.println("game start!");
                    Network.GameStart msg = (Network.GameStart) object;
                    screen.getHud().setWorldTimer(msg.time);
                    screen.getDoorsManager().openStartDoor();
                    TimesUpTeamGame.isTimeUp = false;
                    TimesUpTeamGame.isRunning = true;
                    TimesUpTeamGame.isWin = false;
                }

                else if (object instanceof Network.GameOver) {
                    System.out.println("Game over");
                    client.stop(); // disconnect player from server
                    try {
                        client.dispose();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                else if (object instanceof Network.GameWin) {
                    System.out.println("game win!");
                    TimesUpTeamGame.isWin = true;
                    client.stop();
                }

                else if (object instanceof Network.LevelUp) {
                    System.out.println("level up!");
                    final Network.LevelUp msg = (Network.LevelUp) object;

                    screen.getHud().setWorldTimer(msg.time);
                    screen.getHud().keyCount = 0;
                    screen.getHud().keyCountLabel.setText(String.format("%01d/3", 0));
                    screen.getHud().nextLevel();
                    screen.getKeysManager().keysPickedUp = 0;
                    screen.getDoorsManager().isOpened = false;
                    screen.game.audioManager.playLevelUpSound();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            screen.player.b2Body.setTransform(
                                    new Vector2(msg.x, msg.y),
                                    screen.player.b2Body.getAngle());
                        }
                    });
                }

                else if (object instanceof Network.MovePlayer) {
                    // Another player has moved
                    final Network.MovePlayer msg = (Network.MovePlayer) object;

                    System.out.println();
                    System.out.println("--- Received MovePlayer event from server.");
                    System.out.printf("--- (id: %d, x: %f, y: %f)\n", msg.id, msg.x, msg.y);

                    if (screen.player2 == null) {
                        System.out.println("- player2 is null, creating second player...");
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                screen.createSecondPlayer(msg.x, msg.y);
                            }
                        });

                        System.out.println("- Other player doesn't see us yet, send them MovePlayer with our current position...");
                        sendPosition(screen.player.b2Body.getPosition().x, screen.player.b2Body.getPosition().y);
                    } else {
                        System.out.println("- player2 exists, moving them...");
                        screen.moveSecondPlayer(msg.x, msg.y);
                        // ...
                    }

                    System.out.println();
                }

                else if (object instanceof Network.MoveGuard) {
                    final Network.MoveGuard msg = (Network.MoveGuard) object;

                    if (screen.guard == null) {
                        System.out.println("- guard is null, creating guard");
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                screen.createGuard(msg.x, msg.y);
                            }
                        });
                    } else {
                        System.out.printf("- moving guard (%f, %f)\n", msg.x, msg.y);
                        screen.moveGuard(msg.x, msg.y);
                    }

                    System.out.println();
                }

                else if (object instanceof Network.KeyPicked) {
                    Network.KeyPicked msg = (Network.KeyPicked) object;

                    System.out.println();
                    System.out.println("--- Received KeyPicked event from server.");
                    System.out.printf("--- (id: %d, key id: %d)\n", msg.id, msg.keyId);

                    screen.getKeysManager().addKeyToBeDestroyed(msg.keyId);
                }
            }
        }));

        // Try to connect to server
        try {
            int connectionTimeout = 7000;
            client.connect(connectionTimeout, SERVER_IP, TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            // During development, we might not need a server for client testing, so commented right now
            // System.exit(1);
            e.printStackTrace();
            System.err.println("! Can't connect to server. Is a server instance running?");
        }
    }


    /**
     * Send player's position (with their ID) to the server, so the server can forward it to other players.
     */
    public void sendPosition(float x, float y) {
        Network.MovePlayer msg = new Network.MovePlayer();

        msg.id = client.getID();
        msg.x = x;
        msg.y = y;

        System.out.println();
        System.out.printf("--- Sending my new position to the server... (id: %d, x: %f, y: %f)\n", msg.id, msg.x, msg.y);
        System.out.println();

        client.sendTCP(msg);
    }

    /**
     * Send key picked up event to the server, so the server can forward it to other players.
     */
    public void sendKeyPicked(int keyId) {
        Network.KeyPicked msg = new Network.KeyPicked();

        msg.id = client.getID();
        msg.keyId = keyId;

        System.out.printf("--- Sending key picked event to the server... (id: %d, key id: %d)\n", msg.id, msg.keyId);

        client.sendTCP(msg);
    }
}
