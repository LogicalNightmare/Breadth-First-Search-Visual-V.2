package com.bfs.v2;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PathFinder {
    private final LinkedHashMap<Point, Integer> masterList;
    private LinkedHashMap<Integer, ArrayList<Integer>> adjacencyList;
    private int[][] grid;

    public PathFinder(int[][] grid) {
        this.grid = grid;

        masterList = new LinkedHashMap<>();
        adjacencyList = new LinkedHashMap<>();

        buildMasterList();
    }

    public LinkedList<Point> getPath(Point start, Point end, ArrayList<Point> blockedTiles) {
        buildAdjacencyList(blockedTiles);
        Integer[] previous = bfs(start);
        LinkedList<Integer> path = reconstructPath(start, end, previous);
        adjacencyList = new LinkedHashMap<>();
        return convertPath(path);
    }

    private LinkedList<Point> convertPath(LinkedList<Integer> path) {
        LinkedList<Point> convertedPath = new LinkedList<>();
        for(Integer p : path) {
            for(Point node : masterList.keySet()) {
                if(masterList.get(node) == p) {
                    convertedPath.add(node);
                }
            }
        }
        return convertedPath;
    }

    private LinkedList<Integer> reconstructPath(Point start, Point end, Integer[] prev) {
        Integer startNode = masterList.get(start);
        Integer endNode = masterList.get(end);
        LinkedList<Integer> path = new LinkedList<>();

        for(Integer at = endNode; !at.equals(startNode); at = prev[at]) {
            path.add(at);
        }

        return path;
    }

    private Integer[] bfs(Point start) {
        Integer startNode = masterList.get(start);
        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[grid.length * grid[0].length];
        Integer[] previous = new Integer[grid.length * grid[0].length];

        queue.add(startNode);
        visited[startNode] = true;

        while(!queue.isEmpty()) {
            Integer node = queue.pollFirst();

            for(Integer n : adjacencyList.get(node)) {
                if(!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                    previous[n] = node;
                }
            }
        }

        return previous;
    }

    private void buildAdjacencyList(ArrayList<Point> blockedTiles) {
        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                if(blockedTiles.contains(new Point(x, y))) continue;
                Integer node = masterList.get(new Point(x, y));
                ArrayList<Integer> adjacentNodes = new ArrayList<>();

                //(r + 1, c)
                if(y + 1 < grid.length && !blockedTiles.contains(new Point(x, y + 1))) {
                    adjacentNodes.add(masterList.get(new Point(x, y + 1)));
                }

                //(r - 1, c)
                if(y - 1 >= 0 && !blockedTiles.contains(new Point(x, y - 1))) {
                    adjacentNodes.add(masterList.get(new Point(x, y - 1)));
                }

                //(r, c + 1)
                if(x + 1 < grid[y].length && !blockedTiles.contains(new Point(x + 1, y))) {
                    adjacentNodes.add(masterList.get(new Point(x + 1, y)));
                }

                //(r, c - 1)
                if(x - 1 >= 0 && !blockedTiles.contains(new Point(x - 1, y))) {
                    adjacentNodes.add(masterList.get(new Point(x - 1, y)));
                }

                adjacencyList.put(node, adjacentNodes);
            }
        }
    }

    private void buildMasterList() {
        int counter = 0;

        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                masterList.put(new Point(x, y), counter);
                counter++;
            }
        }
    }
}
