package com.rahamehed.server.ai;

public class Node implements Comparable<Node>{
    private float x;
    private float y;
    private int index;
    public Node(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Set the index for this current node
    public void setIndex(int index) {
        this.index = index;
    }



}
