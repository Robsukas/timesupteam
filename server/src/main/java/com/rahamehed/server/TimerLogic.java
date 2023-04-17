package com.rahamehed.server;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private MainServer server;

    private int cycleLength = 250; // ms
    private int secondsPerLevel = 20;
    private int cyclesLeft;

    private boolean[][] map;  // holds walls pos

    public TimerLogic(MainServer server) {
        this.server = server;
    }


    /**
     * Start level and its timer.
     * Update guards position while game is running.
     * Run until time is up, then send game end event.
     */
    public void start() {
        // Both players have joined
        // Load map into memory
        TmxMapLoader mapLoader = new TmxMapLoader();
        try {
            map = mapLoader.readInMap("ai_test_map2.tmx", "top");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.deepToString(map));

        System.exit(0);

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

//        // If beginning, set guard's position to zero
//        if (msg.x == 0 && msg.y == 0) {
//            msg.x = 3.315605f;
//            msg.y = 2.889148f;
//        }

        // Calculate guard's new position
        // ...
        float goalX = server.players.entrySet().iterator().next().getValue().get(0);
        float goalY = server.players.entrySet().iterator().next().getValue().get(1);
        msg.x = goalX;
        msg.y = goalY;

        server.server.sendToAllTCP(msg);
    }
}
