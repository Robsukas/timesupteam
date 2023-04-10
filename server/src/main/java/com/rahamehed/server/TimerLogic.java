package com.rahamehed.server;

import com.esotericsoftware.kryonet.Server;

import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private MainServer server;

    private int cycleLength = 250; // ms
    private int secondsPerLevel = 150;
    private int cyclesLeft;

    int[][] a = new int[10][10];

    public TimerLogic(MainServer server) {
        this.server = server;
    }

    private void readInMap() {

    }

    /**
     * Start level and its timer.
     * Update guards position while game is running.
     * Run until time is up, then send game end event.
     */
    public void start() {
        gameStart();
        cyclesLeft = (secondsPerLevel * 1000) / cycleLength;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (cyclesLeft <= 0) {
                    timer.cancel();
                    gameOver();
                }

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

        if (msg.x == 0 && msg.y == 0) {
            msg.x = 3.315605f;
            msg.y = 2.889148f;
        }

        // Calculate guard's new position
        // ...
        float goalX = server.players.entrySet().iterator().next().getValue().get(0);
        float goalY = server.players.entrySet().iterator().next().getValue().get(1);


        server.server.sendToAllTCP(msg);
    }
}
