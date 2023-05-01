package com.rahamehed.server;

import java.util.*;

public class AStarMazeSolver {
    /**
     * Get the shortest path from given startPoint to endPoint in maze.
     *
     * @param maze       2D int array where 1 is wall, 0 is passable path
     * @param startPoint {x, y}
     * @param endPoint   {x, y}
     * @return list of {x, y} coordinate pairs from start to end point (both inclusive)
     */
    public static List<int[]> solveMaze(int[][] maze, int[] startPoint, int[] endPoint) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // possible move directions (NSWE)
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.g + node.h));

        Node startNode = new Node(startPoint[0], startPoint[1], null, 0, getHeuristic(startPoint, endPoint));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            visited.add(currentNode.x + "," + currentNode.y);

            if (currentNode.x == endPoint[0] && currentNode.y == endPoint[1]) {
                return buildPath(currentNode);
            }

            for (int[] direction : directions) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                if (isValidPosition(maze, newX, newY) && !visited.contains(newX + "," + newY)) {
                    Node neighborNode = new Node(newX, newY, currentNode, currentNode.g + 1, getHeuristic(new int[]{newX, newY}, endPoint));
                    openList.add(neighborNode);
                }
            }
        }

        return new ArrayList<>();
    }

    public static int getHeuristic(int[] point, int[] endPoint) {
        return Math.abs(point[0] - endPoint[0]) + Math.abs(point[1] - endPoint[1]);
    }

    private static boolean isValidPosition(int[][] maze, int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != 1;
    }

    private static List<int[]> buildPath(Node node) {
        List<int[]> path = new ArrayList<>();
        while (node != null) {
            path.add(0, new int[]{node.x, node.y});
            node = node.parent;
        }
        return path;
    }

    /**
     * For testing the algo.
     */
    public static void main(String[] args) {
//        int[][] maze = {
//                {0, 1, 0, 0, 0},
//                {0, 1, 0, 1, 0},
//                {0, 0, 0, 1, 0},
//                {0, 1, 0, 1, 0},
//                {0, 1, 0, 1, 0}
//        };

        MapHandler mapHandler = new MapHandler();
        try {
            mapHandler.readInMap("level_1.tmx", "top");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int[][] maze = mapHandler.mapArray;

        mapHandler.printMap();
//        int[] guardPos = mapHandler.playerPositionToInts(9.049289f, 3.764999f);
        int[] guardPos = mapHandler.playerPositionToInts(8.574544f, 4.500601f);
        System.out.println(Arrays.toString(guardPos));
        int temp = guardPos[0];
        guardPos[0] = guardPos[1];
        guardPos[1] = temp;

        int[] playerPos = mapHandler.playerPositionToInts(10f, 2.83f);
        temp = playerPos[0];
        playerPos[0] = playerPos[1];
        playerPos[1] = temp;


        System.out.println("Starting pos: " + Arrays.toString(guardPos));
        System.out.println("End pos     : " + Arrays.toString(playerPos));
        System.out.println();
//
//        int[] startPoint = {0, 0};
//        int[] endPoint = {4, 4};

        long start = System.currentTimeMillis();
        List<int[]> path = AStarMazeSolver.solveMaze(maze, guardPos, playerPos);
        long time = System.currentTimeMillis() - start;

        for (int[] point : path) {
            mapHandler.mapArray[point[0]][point[1]] = 4;
            System.out.println("Path: " + Arrays.toString(point));
        }

        mapHandler.printMap();
        System.out.println();
        System.out.println("Time (ms): " + time);
    }

    private static class Node {
        int x, y;
        Node parent;
        int g, h;

        Node(int x, int y, Node parent, int g, int h) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }
    }
}