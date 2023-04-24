package com.rahamehed.server;

import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private MainServer server;
    private MapHandler mapHandler;

    private final int cycleLength = 250; // ms
    private final int secondsPerLevel = 300;
    private int cyclesLeft;


    private int player1LastX;
    private int player1LastY;

    private int guardX;
    private int guardY;

    public TimerLogic(MainServer server, MapHandler mapHandler) {
        this.server = server;
        this.mapHandler = mapHandler;
    }

    /**
     * Start level and its timer.
     * Update guards position while game is running.
     * Run until time is up, then send game end event.
     */
    public void start() {
        // Both players have joined

        // Send game start event to all players
        gameStart();

        // Do stuff (move guard) every cycleLength milliseconds
        Timer timer = new Timer();
        cyclesLeft = (secondsPerLevel * 1000) / cycleLength;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (cyclesLeft <= 0) {
                    // Time is up, send game over event to all players
                    timer.cancel();
                    gameOver();
                }

                // Update guard's position and send it to all players
                moveGuard();

                cyclesLeft--;
            }
        }, 0, cycleLength);
    }

    /**
     * Send game start event to all players.
     */
    private void gameStart() {
        Network.GameStart msg = new Network.GameStart();
        msg.time = secondsPerLevel;

        server.server.sendToAllTCP(msg);
    }

    /**
     * Send game over event to all players (lost).
     */
    private void gameOver() {
        Network.GameOver msg = new Network.GameOver();

        server.server.sendToAllTCP(msg);
    }

    /**
     * Send guards' new positions to all players.
     */
    private void moveGuard() {
        Network.MoveGuard msg = new Network.MoveGuard();
        msg.guardId = 0;

//        int player1Id = server.players.entrySet().iterator().next().getKey();
        int player1X = server.players.entrySet().iterator().next().getValue()[0];
        int player1Y = server.players.entrySet().iterator().next().getValue()[1];

        // Remove player's old position from map
        if (player1LastX != 0) {
            mapHandler.setValueInMap(player1LastX, player1LastY, 0);
        }
        player1LastX = player1X;
        player1LastY = player1Y;

        // Set new player's position to the map
        mapHandler.setValueInMap(player1X, player1Y, 2);

        // DEBUG: print map
        System.out.println("\n\n-----------\n\n");
        mapHandler.printMap();
        System.out.println("\n\n-----------\n\n");


        // Calculate guard's new position
        // ...

        server.server.sendToAllTCP(msg);
    }
}
