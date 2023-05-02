package com.rahamehed.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainServer {

    public final Server server;
    // Ports must be the same in clients
    private final int TCP_PORT = 8080;
    private final int UDP_PORT = 8081;

    public Map<Integer, int[]> players = new HashMap<>();  // <player ID, <x, y>>
    private TimerLogic timer;
    private MapHandler mapHandler;

    public MainServer() throws IOException {
        // Set logging level
        Log.set(Log.LEVEL_INFO);

        // Initialize server & register all classes that are to be sent over the network
        server = new Server();
        Network.register(server);

        // Initialize timer, read in level 1 map
        mapHandler = new MapHandler();
        timer = new TimerLogic(this, mapHandler);
        try {
            mapHandler.readInMap("level_1.tmx", "top");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Add listener to tell the server, what to do after something is sent over the network
        server.addListener(new Listener() {
            public void connected(Connection c) {
                // Don't allow more than 2 players
                if (players.size() == 2) {
                    System.out.println("Already 2 players connected, disconnecting new connection.");
                    c.close();
                    return;
                }

                System.out.println();
                System.out.println("!-- Player connected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println("--- UDP: " + c.getRemoteAddressUDP().toString());
                System.out.println("--- TCP: " + c.getRemoteAddressTCP().toString());
                System.out.println();

                // Add new joined player to players map
                players.put(c.getID(), mapHandler.playerPositionToInts(10f, 2.83f));  // ! hard-coded, see client/.../Character.java

                // If both players have joined, start the timer loop
                if (players.size() == 2) {
                    timer.start();
                }
            }

            public void disconnected(Connection c) {
                System.out.println();
                System.out.println("!-- Player disconnected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println();

                // Remove disconnected player from players map
                players.remove(c.getID());

                if (players.size() == 1) {
                    // One player disconnected, disconnect the other one too
                    Arrays.stream(server.getConnections()).iterator().next().close();
//                    players.keySet().iterator().next()
                }

                if (players.size() == 0) {
                    // Game is over, reset game state
                    timer.reset();

                    // Or one of two players left ... what then
                }
            }

            public void received(Connection c, Object object) {
                if (object == null || object instanceof FrameworkMessage.KeepAlive) {
                    return;
                }
//                System.out.println();
//                System.out.println("!-- Received data from player.");
//                System.out.println("--- Connection ID: " + c.getID());
//                System.out.println("--- Object: " + object.getClass().getSimpleName());

                if (object instanceof Network.MovePlayer) {
                    Network.MovePlayer msg = (Network.MovePlayer) object;

//                    System.out.println();
//                    System.out.println("--- Received MovePlayer event from a player; sending that to all other players.");
//                    System.out.printf("--- (id: %d, x: %f, y: %f)\n", msg.id, msg.x, msg.y);
//                    System.out.println();

                    // Update player's location (server-side representation) in players map
//                    players.replace(msg.id, mapHandler.playerPositionToInts(msg.x, msg.y));
                    players.replace(msg.id, mapHandler.playerPositionToInts(msg.x, msg.y));

                    // Forward the MovePlayer message to other player(s)
                    server.sendToAllExceptTCP(msg.id, msg);
                }

                if (object instanceof Network.KeyPicked) {
                    Network.KeyPicked msg = (Network.KeyPicked) object;

                    System.out.println();
                    System.out.println("--- Received KeyPicked event from a player; sending that to all other players.");
                    System.out.printf("--- (id: %d, key id: %d)\n", msg.id, msg.keyId);
                    System.out.println();

                    server.sendToAllExceptTCP(msg.id, msg);
                }
            }
        });

        // Start the server
        server.bind(TCP_PORT, UDP_PORT);
        server.start();
    }

    public static void main(String[] args) throws IOException {
        new MainServer();
    }
}
