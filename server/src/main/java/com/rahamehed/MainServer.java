package com.rahamehed;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class MainServer {

    private final Server server;
    // Ports must be the same in clients
    private final int TCP_PORT = 3000;
    private final int UDP_PORT = 3001;

    public static void main(String[] args) throws IOException {
        new MainServer();
    }

    public MainServer() throws IOException {
        // Set logging level
        Log.set(Log.LEVEL_DEBUG);

        // Initialize server & register all classes that are to be sent over the network
        server = new Server();
        RegisterClasses.register(server);

        // Add listener to tell the server, what to do after something is sent over the network
        server.addListener(new Listener() {


            /**
             * New player connected.
             * "This method should not block for long periods as other network activity will not be processed until it returns."
             */
            public void connected(Connection c) {
                System.out.println();
                System.out.println("--- Player connected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println("--- UDP: " + c.getRemoteAddressUDP().toString());
                System.out.println("--- TCP: " + c.getRemoteAddressTCP().toString());
                System.out.println();
            }


            /**
             * A player disconnected from the game.
             */
            public void disconnected(Connection c) {
                System.out.println();
                System.out.println("--- Player disconnected.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println();
            }


            /**
             * Data received from player.
             * "This method should not block for long periods as other network activity will not be processed until it returns."
             */
            public void received(Connection c, Object object) {
                if (object == null) {
                    return;
                }

                System.out.println();
                System.out.println("--- Received data from player.");
                System.out.println("--- Connection ID: " + c.getID());
                System.out.println("--- Object: " + object);
                System.out.println();
            }
        });

        server.bind(TCP_PORT, UDP_PORT);
        server.start();
    }
}
