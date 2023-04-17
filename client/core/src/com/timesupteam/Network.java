package com.timesupteam;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MovePlayer.class);
        kryo.register(KeyPicked.class);
        kryo.register(MoveGuard.class);
        kryo.register(GameStart.class);
        kryo.register(GameOver.class);
    }

    static public class MovePlayer {
        public int id;
        public float x, y;
    }

    static public class KeyPicked {
        public int id;
        public int keyId;
    }

    static public class MoveGuard {
        public int guardId;
        public float x, y;
    }

    static public class GameStart {
        public int time;  // seconds
    }

    static public class GameOver {
//        public int time;  // seconds
    }
}
