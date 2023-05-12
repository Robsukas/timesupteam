package com.rahamehed.server;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private final int cycleLength = 500; // ms
    private int secondsPerLevel = 60;
    private MainServer server;
    private MapHandler mapHandler;
    private Timer timer;
    private int cyclesLeft;

    private int currentLevel = 1;

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
        reset();

        // Send game start event to all players
        gameStart();

        // Do stuff (move guard) every cycleLength milliseconds
        timer = new Timer();
        cyclesLeft = (secondsPerLevel * 1000) / cycleLength;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (cyclesLeft <= 0) {
                    // Time is up, send game over event to all players
                    timer.cancel();
                    gameOver();
                    return;
                }

                if (server.players.size() == 0) {
                    return;
                }

                // Check if players are in the end room
                if (playersInEndRoom()) {
                    if (currentLevel == 2) {  // todo: change to 3 when level 3 exists
                        gameWin();
                        timer.cancel();
                    } else {
                        currentLevel++;
                        if (currentLevel == 2) {
                            secondsPerLevel = 300;
                            guardX = 168;
                            guardY = 142;
                        } else if (currentLevel == 3) {
                            secondsPerLevel = 30;
                            guardX = 168;  // find out (look at server print)
                            guardY = 142;  // find out (look at server print)
                        }

                        // Reset time
                        cyclesLeft = (secondsPerLevel * 1000) / cycleLength;
                        levelUp();
                    }
                    return;
                }

                // Update guard's position and send it to all players
                moveGuard();

                cyclesLeft--;
            }
        }, 0, cycleLength);
    }


    /**
     * Check whether both players are in the end room.
     */
    private boolean playersInEndRoom() {
        boolean finished = true;
        int endRoomY = 14;
        for (int[] p : server.players.values()) {
            System.out.println(Arrays.toString(p));
            if (p[1] > endRoomY) {
                finished = false;
                break;
            }
        }
        return finished;
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

    private void gameWin() {
        Network.GameWin msg = new Network.GameWin();

        server.server.sendToAllTCP(msg);
    }

    private void levelUp() {
        Network.LevelUp msg = new Network.LevelUp();
        msg.time = secondsPerLevel;
        msg.x = 29.26049f;
        msg.y = 1.154136f;

        // Set one player's server-side coordinate temporarily to not trigger both players in end room
        server.players.entrySet().iterator().next().setValue(new int[]{15, 15});

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

//        currentGuardPath.forEach(p -> System.out.println(p[0] + ", " + p[1]));
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

//        if ((Math.abs(guardX - player1X) == 1 && guardY == player1Y) ||
//             Math.abs(guardY - player1Y) == 1 && guardX == player1X) {
//
//        }

        // If guard is on the same tile as player, end game
        if (guardX == player1X && guardY == player1Y) {
            gameOver();
        }

        System.out.println("- player: x=" + player1X + ", y=" + player1Y);
        System.out.println("- guard : x=" + guardX + ", y=" + guardY);
//        System.out.println("- moving guard to " + msg.x + ", " + msg.y);

        server.server.sendToAllTCP(msg);
    }

    public void reset() {
        if (timer != null) {
            timer.cancel();
        }
        guardX = 53;
        guardY = 35;
        currentLevel = 1;
        secondsPerLevel = 60;
    }
}
