package uk.ac.ed.inf;

import java.awt.*;
import java.util.*;
import java.util.List;

public class FlightPath {
    private NoFlyZone noFlyZone;
    private LngLat dropOffPoint;
    private final double MOVE_DISTANCE = 0.00015;
    private final CompassDirection[] POSSIBLE_MOVES = {
            CompassDirection.NORTH, CompassDirection.NORTH_NORTH_EAST, CompassDirection.NORTH_EAST, CompassDirection.EAST_NORTH_EAST,
            CompassDirection.EAST, CompassDirection.EAST_SOUTH_EAST, CompassDirection.SOUTH_EAST, CompassDirection.SOUTH_SOUTH_EAST,
            CompassDirection.SOUTH, CompassDirection.SOUTH_SOUTH_WEST, CompassDirection.SOUTH_WEST, CompassDirection.WEST_SOUTH_WEST,
            CompassDirection.WEST, CompassDirection.WEST_NORTH_WEST, CompassDirection.NORTH_WEST, CompassDirection.NORTH_NORTH_WEST
    };
//    private final Point APPLETON_TOWER = Point.fromLngLat(−3.186874, 55.944494);

    public FlightPath(){}

    public static List<PathNode> getPath(PathNode goal) {
        List<PathNode> path = new ArrayList<PathNode>();

//        for (PathNode cood = goal; cood != null; cood = cood.getParent()){
//            System.out.println(cood);
//            System.out.println(cood.getValue());
//            System.out.println(cood.getParent().getValue());
//            path.add(cood);
//        }
        PathNode node = goal;
        while (node != null) {
            System.out.println(node);
            System.out.println(node.getValue());
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public PathNode AStarSearch(RestClient server, PathNode source, PathNode goal) {
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

        while (!nodeQueue.isEmpty() && !found) {
            // the node is having the lowest f score value
            PathNode current = nodeQueue.poll();
            lngLatsQueue.remove(current.getValue());
            explored.add(current.getValue());
            System.out.println(current);
            System.out.println(current.getValue());

            // goal found
            if (current.getValue().closeTo(goal.getValue())) {
                found = true;
                System.out.println("YAYYYYYYYYYYYYYYY goal reached :D");
                result = current;
                break;
            }
            System.out.println("is the goal reached? NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

            //get all possible moves for current node
//            CompassDirection[] possibleMoves = current.getPossibleMoves(POSSIBLE_MOVES, server);


            // check every child of current node
            for (CompassDirection d : POSSIBLE_MOVES) {
                PathNode next = new PathNode(current.getValue().nextPosition(d));
//                System.out.println(d);
//                System.out.println(next);
//                System.out.println(next.getValue());
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
}
