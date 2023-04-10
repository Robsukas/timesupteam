package com.rahamehed.server;

import com.esotericsoftware.kryonet.Server;

import java.util.Timer;
import java.util.TimerTask;


public class TimerLogic {

    private Server server;
//    private boolean isRunning = false;

    private int cycleLength = 250; // ms
    private int secondsPerLevel = 15;
    private int cyclesLeft;


    public TimerLogic(Server server) {
        this.server = server;
    }

    /**
     * Start level and its timer.
     * Update guards position while game is running.
     * Run until time is up, then send game end event.
     */
    public void start() {
//        // Ensure start is called only once
//        if (isRunning) return;
//        isRunning = true;

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

        server.sendToAllTCP(msg);
    }

    /**
     * Send game over event to all players (lost).
     */
    private void gameOver() {
        Network.GameOver msg = new Network.GameOver();

        server.sendToAllTCP(msg);
    }

    /**
     * Send guards' new positions to all players.
     */
    private void moveGuard() {
        Network.MoveGuard msg = new Network.MoveGuard();
        msg.guardId = 0;
        msg.x = (float) (Math.random() * 2 + 5f);
        msg.y = (float) (Math.random() * 2 + 5f);

        server.sendToAllTCP(msg);
    }
}
