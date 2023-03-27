package com.rahamehed.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MovePlayer.class);
        kryo.register(KeyPicked.class);
    }

    static public class MovePlayer {
        public int id;
        public float x, y;
    }

    static public class KeyPicked {
        public int id;
        public int keyId;
    }
}
