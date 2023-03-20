package com.rahamehed.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class MainServer {

    private final Server server;
    // Ports must be the same in clients
    private final int TCP_PORT = 8080;
    private final int UDP_PORT = 8081;

    public static void main(String[] args) throws IOException {
        new MainServer();
    }

    public MainServer() throws IOException {
        // Set logging level
        Log.set(Log.LEVEL_INFO);

        // Initialize server & register all classes that are to be sent over the network
        server = new Server();
        Network.register(server);

        // Add listener to tell the server, what to do after something is sent over the network
        server.addListener(new Listener() {
            public void connected(Connection c) {
//                players.put(c.getID(), new Player());

                System.out.println();
                System.out.println("!-- Player connected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println("--- UDP: " + c.getRemoteAddressUDP().toString());
                System.out.println("--- TCP: " + c.getRemoteAddressTCP().toString());
                System.out.println();
            }

            public void disconnected(Connection c) {
//                players.remove(c.getID());

                System.out.println();
                System.out.println("!-- Player disconnected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println();
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

                    server.sendToAllExceptTCP(msg.id, msg);
                }
            }
        });

        // Start the server
        server.bind(TCP_PORT, UDP_PORT);
        server.start();
    }
}
