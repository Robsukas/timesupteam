package com.rahamehed;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class RegisterClasses {


    /**
     * Register classes that are sent over the network.
     * This class should be equal in server and client.
     */
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(PlayerPosition.class);
//        kryo.register(Player.class);
//        kryo.register(Player[].class);
//        kryo.register(Character.class);
        System.out.println("here");
    }
}
