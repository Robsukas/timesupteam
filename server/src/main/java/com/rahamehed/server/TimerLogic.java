package com.rahamehed.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.Timer;
import java.util.TimerTask;

//import javax.swing.Timer;

public class TimerLogic {


//    static class Task extends TimerTask {
//        public void run() {
//            System.out.println("Time's up!");
////            toolkit.beep();
////            System.exit(0);
////            server.sendToAllTCP();
//
//        }
//    }

    public static void main(Server server) {
        Timer timer = new Timer();
        long delay = 250; // ms

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Network.MoveGuard msg = new Network.MoveGuard();
                msg.guardId = 0;
                msg.x = (float) (Math.random() * 2 + 5f);
                msg.y = (float) (Math.random() * 2 + 5f);

                server.sendToAllTCP(msg);
            }
        }, 0, delay);

    }

//    public static int main() {
//        Timer timer = new Timer();
//        long delay = 5000; // ms
//        timer.schedule(new Task(), 0, delay);
//    }
}
