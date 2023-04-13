package com.rahamehed.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainServer {

    public final Server server;
    // Ports must be the same in clients
    private final int TCP_PORT = 8080;
    private final int UDP_PORT = 8081;

    public Map<Integer, List<Float>> players = new HashMap<>();  // <player ID, <x, y>>
    private TimerLogic timer;

    public static void main(String[] args) throws IOException {
        new MainServer();
    }

    public MainServer() throws IOException {
        // Set logging level
        Log.set(Log.LEVEL_INFO);

        // Initialize server & register all classes that are to be sent over the network
        server = new Server();
        Network.register(server);

        // Initialize timer
        timer = new TimerLogic(this);

        // Add listener to tell the server, what to do after something is sent over the network
        server.addListener(new Listener() {
            public void connected(Connection c) {
                System.out.println();
                System.out.println("!-- Player connected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println("--- UDP: " + c.getRemoteAddressUDP().toString());
                System.out.println("--- TCP: " + c.getRemoteAddressTCP().toString());
                System.out.println();

                // Add new joined player to players map with coordinates (0, 0)
                players.put(c.getID(), List.of(0f, 0f));

                // If both players have joined, start the timer loop
                if (players.size() >= 2) {
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

                    System.out.println();
                    System.out.println("--- Received MovePlayer event from a player; sending that to all other players.");
                    System.out.printf("--- (id: %d, x: %f, y: %f)\n", msg.id, msg.x, msg.y);
                    System.out.println();

                    // Update player's location (server-side representation) in players map
                    players.replace(msg.id, List.of(msg.x, msg.y));

                    // Forward the MovePlayer message to other player(s)
                    server.sendToAllExceptTCP(msg.id, msg);
                }
            }
        });

        // Start the server
        server.bind(TCP_PORT, UDP_PORT);
        server.start();
    }
}
