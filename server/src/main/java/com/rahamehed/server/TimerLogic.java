package com.rahamehed.server;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private final int cycleLength = 500; // ms
    private final int secondsPerLevel = 10;
    private MainServer server;
    private MapHandler mapHandler;
    private int cyclesLeft;

    private int player1LastX;
    private int player1LastY;

    // Set initial guard position (cells) here
    private int guardX = 53;
    private int guardY = 35;

    private List<int[]> currentGuardPath;
    private int pathCounter = 0;

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

                if (server.players.size() == 0) {
                    return;
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

        int player1X = server.players.entrySet().iterator().next().getValue()[0];
        int player1Y = server.players.entrySet().iterator().next().getValue()[1];

        // Get the closest player
        int h = Integer.MAX_VALUE;
        for (int[] p : server.players.values()) {
            int newH = AStarMazeSolver.getHeuristic(new int[]{guardY, guardX}, new int[]{p[1], p[0]});
            if (newH < h) {
                h = newH;
                player1X = p[0];
                player1Y = p[1];
            }
        }

        // Player has moved, recalculate path to them
        if (player1X != player1LastX || player1Y != player1LastY) {
            player1LastX = player1X;
            player1LastY = player1Y;

            pathCounter = 1;
            currentGuardPath = AStarMazeSolver.solveMaze(mapHandler.mapArray, new int[]{guardY, guardX}, new int[]{player1Y, player1X});
        }

        currentGuardPath.forEach(p -> System.out.println(p[0] + ", " + p[1]));
        if (pathCounter < currentGuardPath.size()) {
            int[] nextCell = currentGuardPath.get(pathCounter);

            guardY = nextCell[0];
            guardX = nextCell[1];

            pathCounter++;

            // Convert back to client-side positions
            float[] realNextCell = mapHandler.guardPositionToFloats(nextCell[1], nextCell[0]);

            msg.x = realNextCell[0];
            msg.y = realNextCell[1];
        }

        System.out.println("- player: " + player1X + ", " + player1Y);
        System.out.println("- moving guard to " + msg.x + ", " + msg.y);

        server.server.sendToAllTCP(msg);
    }
}
