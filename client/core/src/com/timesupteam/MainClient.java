package com.timesupteam;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class MainClient {

    private final Client client;
    // Ports must be the same on server
    private final int TCP_PORT = 3000;
    private final int UDP_PORT = 3001;
    private final String SERVER_IP = "localhost";

    public MainClient() throws IOException {
        client = new Client();
        // RegisterClasses.register(client);

        client.start();
        int connectionTimeout = 7000;
        client.connect(connectionTimeout, SERVER_IP, TCP_PORT, UDP_PORT);
    }
}
