package uk.ac.ed.inf.DronePath;

import java.util.*;
import java.util.List;

public class FlightPath {
    private final double MOVE_DISTANCE = 0.00015;
    private final CompassDirection[] POSSIBLE_MOVES = {
            CompassDirection.NORTH, CompassDirection.NORTH_NORTH_EAST, CompassDirection.NORTH_EAST, CompassDirection.EAST_NORTH_EAST,
            CompassDirection.EAST, CompassDirection.EAST_SOUTH_EAST, CompassDirection.SOUTH_EAST, CompassDirection.SOUTH_SOUTH_EAST,
            CompassDirection.SOUTH, CompassDirection.SOUTH_SOUTH_WEST, CompassDirection.SOUTH_WEST, CompassDirection.WEST_SOUTH_WEST,
            CompassDirection.WEST, CompassDirection.WEST_NORTH_WEST, CompassDirection.NORTH_WEST, CompassDirection.NORTH_NORTH_WEST
    };

    public FlightPath() {
    }

    public List<PathNode> getFlightPath(PathNode goal) {
        List<PathNode> path = new ArrayList<PathNode>();
        PathNode node = goal;
        while (node != null) {
            path.add(node);
            node = node.getPrevious();
        }
        Collections.reverse(path);
        return path;
    }

    public PathNode AStarSearch(NoFlyZone[] noFlyZones, CentralArea centralArea, PathNode source, PathNode goal) {
        Set<LngLat> explored = new HashSet<LngLat>();
//        HashMap<LngLat, PathNode> explored = new HashMap<LngLat, PathNode>();
        PathNode result = null;

        Set<LngLat> lngLatsQueue = new HashSet<LngLat>();
        PriorityQueue<PathNode> nodeQueue = new PriorityQueue<PathNode>(new Comparator<PathNode>() {
            @Override
            public int compare(PathNode node1, PathNode node2) {
                if (node1.getfScore() > node2.getfScore()) {
                    return 1;
                } else if (node1.getfScore() < node2.getfScore()) {
                    return -1;
                } else return 0;
            }
        });

        // cost from start
        source.setgScore(0);
        nodeQueue.add(source);
        lngLatsQueue.add(source.getValue());
        boolean found = false; // replace with the isvalid function
        int intersectCentralAreaCount = 0;

        while (!nodeQueue.isEmpty() && !found) {
            // the node is having the lowest f score value
            PathNode current = nodeQueue.poll();
            lngLatsQueue.remove(current.getValue());
            explored.add(current.getValue());
//            System.out.println(current);
//            System.out.println(current.getValue());

            // goal found
//            if (isGoal(current, goal)) {
            if (current.getValue().closeTo(goal.getValue())) {
                found = true;
//                System.out.println("YAYYYYYYYYYYYYYYY goal reached :D");
                result = current;
                break;
            }
//            System.out.println("is the goal reached? NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");


            // check every child of current node
            for (CompassDirection d : POSSIBLE_MOVES) {
                PathNode next = new PathNode(current.getValue().nextPosition(d));
//                System.out.println(d);
//                System.out.println(next);
//                System.out.println(next.getValue());

                // check if the next node and the path to next node is valid
                int tempCentralAreaCount = isPathValid(centralArea, intersectCentralAreaCount, current, next);
                if (!isNodeValid(noFlyZones, next) || !isPathValid(noFlyZones, current, next) ||
                        tempCentralAreaCount + intersectCentralAreaCount > 1){
                    continue;
                }

                next.sethScore(goal.getValue());
                double tempGScore = current.getgScore() + MOVE_DISTANCE;
                double tempFScore = tempGScore + next.gethScore();

//                System.out.println(queue);
                // if the child node has been evaluated and the newer f score is higher, skip
                if (explored.contains(next.getValue()) && tempFScore >= next.getfScore()) {
                    continue;
                }

                // else if child node is not in queue or newer f score is lower
                else if (!lngLatsQueue.contains(next.getValue()) || tempFScore < next.getfScore()) {
                    intersectCentralAreaCount += tempCentralAreaCount;
                    next.setPrevious(current);
                    next.setgScore(tempGScore);
                    next.setfScore(tempFScore);

                    if (lngLatsQueue.contains(next.getValue())) {
                        nodeQueue.remove(next);
                        lngLatsQueue.remove(next.getValue());
                    }

                    nodeQueue.add(next);
                    lngLatsQueue.add(next.getValue());
//                    System.out.println(next.getfScore());
//                    System.out.println(queue);
                }
            }
        }
        return result;
    }

    // helper functions to help validate if paths and nodes are valid moves
    // function that checks if the path intersects through no fly zones
    public boolean isPathValid(NoFlyZone[] noFlyZones, PathNode current, PathNode next) {
        for (int i = 0; i < noFlyZones.length; i++) {
            LngLat[] coordinates = noFlyZones[i].getCoordinates();
            for (int j = 0; j < coordinates.length-1; j++) {
                if (current.getValue().isIntersecting(coordinates[j], coordinates[j+1],
                        current.getValue(), next.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    // function that checks if the path leaves the central area, and if so, checks
    // if it re-enters the central area again
    public int isPathValid(CentralArea centralArea, int intersectCentralAreaCount, PathNode current, PathNode next) {
        LngLat[] coordinates = centralArea.getCoordinates();
        int tempCount = 0;
        for (int i = 0; i < coordinates.length; i++) {
            if (i == coordinates.length-1) {
                if (current.getValue().isIntersecting(coordinates[i], coordinates[0],
                        current.getValue(), next.getValue())) {
                    tempCount += 1;
                }
            } else if (current.getValue().isIntersecting(coordinates[i], coordinates[i+1],
            current.getValue(), next.getValue())) {
                tempCount += 1;
            }
        }
        return tempCount;
    }

    // function that checks if the next node is in a no fly zone
    public boolean isNodeValid(NoFlyZone[] noFlyZones, PathNode next) {
        for (int i = 0; i < noFlyZones.length; i++) {
            LngLat[] coordinates = noFlyZones[i].getCoordinates();
            if (next.getValue().inArea(coordinates)) {
                return false;
            }
        }
        return true;
    }
}
