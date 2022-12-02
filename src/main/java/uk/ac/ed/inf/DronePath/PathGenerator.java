package uk.ac.ed.inf.DronePath;

import java.util.*;
import java.util.List;

/**
 * {@link PathGenerator} helps search for the most optimal route to deliver orders.
 */
public class PathGenerator {
    private final double MOVE_DISTANCE = 0.00015;
    private final CompassDirection[] POSSIBLE_MOVES = {
            CompassDirection.NORTH, CompassDirection.NORTH_NORTH_EAST, CompassDirection.NORTH_EAST, CompassDirection.EAST_NORTH_EAST,
            CompassDirection.EAST, CompassDirection.EAST_SOUTH_EAST, CompassDirection.SOUTH_EAST, CompassDirection.SOUTH_SOUTH_EAST,
            CompassDirection.SOUTH, CompassDirection.SOUTH_SOUTH_WEST, CompassDirection.SOUTH_WEST, CompassDirection.WEST_SOUTH_WEST,
            CompassDirection.WEST, CompassDirection.WEST_NORTH_WEST, CompassDirection.NORTH_WEST, CompassDirection.NORTH_NORTH_WEST
    };

    public PathGenerator() {
    }

    /**
     * @param goal Last {@link PathNode} visited close to the {@link uk.ac.ed.inf.Restaurants.Restaurant}.
     * @return List of {@link PathNode} creating a route from Appleton Tower to the
     * {@link uk.ac.ed.inf.Restaurants.Restaurant}.
     */
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

    /**
     * The function uses, A* Algorithm, a graph traversal search algorithm, that was built based off
     * <a href="https://brilliant.org/wiki/dijkstras-short-path-finder/">Dijkstra's algorithm</a> to find the shortest
     * path from one node to the other. It orders nodes explored by using a combination of heuristics to optimise the
     * path travelled.
     *
     * @param noFlyZones  List of {@link NoFlyZone} that restricts the possible valid paths.
     * @param centralArea {@link CentralArea} defines a polygon bounding are for the drone.
     * @param source      {@link PathNode} starting node of the path.
     * @param goal        {@link PathNode} end goal node of the path.
     * @return The last visited {@link PathNode} that is close to the goal node.
     * @see <a href="http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html">Here</a> is an in depth
     * explaination of the theory behind the algorithm.
     */
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
        int intersectCentralAreaCount = 0;

        while (!nodeQueue.isEmpty()) {
            // the node is having the lowest f score value
            PathNode current = nodeQueue.poll();
            lngLatsQueue.remove(current.getValue());
            explored.add(current.getValue());

            // goal found
            if (current.getValue().closeTo(goal.getValue())) {
                result = current;
                break;
            }

            // check every child of current node
            for (CompassDirection d : POSSIBLE_MOVES) {
                PathNode next = new PathNode(current.getValue().nextPosition(d));

                // check if the next node and the path to next node is valid
                int tempCentralAreaCount = isPathValid(centralArea, intersectCentralAreaCount, current, next);
                if (!isPathValid(noFlyZones, current, next) || tempCentralAreaCount + intersectCentralAreaCount > 1) {
                    continue;
                }

                next.sethScore(goal.getValue());
                double tempGScore = current.getgScore() + MOVE_DISTANCE;
                double tempFScore = tempGScore + next.gethScore();

                // if the child node has been evaluated and the newer f score is higher, skip
                if (explored.contains(next.getValue()) && tempFScore >= next.getfScore()) {
                }

                // else if child node is not in queue or newer f score is lower
                else if (!lngLatsQueue.contains(next.getValue()) || tempFScore < next.getfScore()) {
                    intersectCentralAreaCount += tempCentralAreaCount;
                    next.setPrevious(current);
                    next.setgScore(tempGScore);
                    next.setfScore(tempFScore);

                    // if next coordinate previously visited, remove it from queue to avoid entering a loop
                    if (lngLatsQueue.contains(next.getValue())) {
                        nodeQueue.remove(next);
                        lngLatsQueue.remove(next.getValue());
                    }

                    nodeQueue.add(next);
                    lngLatsQueue.add(next.getValue());
                }
            }
        }
        return result;
    }

    // -- helper functions to help validate if paths and nodes are valid moves --

    /**
     * @param noFlyZones List of {@link NoFlyZone} that restricts the possible valid paths.
     * @param current    Details of the current {@link PathNode}
     * @param next       {@link PathNode} of the next node to be validated
     * @return Returns true if the path doesnt intersect through any {@link NoFlyZone}.
     */
    public boolean isPathValid(NoFlyZone[] noFlyZones, PathNode current, PathNode next) {
        for (int i = 0; i < noFlyZones.length; i++) {
            LngLat[] coordinates = noFlyZones[i].getCoordinates();
            for (int j = 0; j < coordinates.length - 1; j++) {
                if (current.getValue().isIntersecting(coordinates[j], coordinates[j + 1],
                        current.getValue(), next.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    // function that checks if the path leaves the central area, and if so, checks
    // if it re-enters the central area again

    /**
     * The function helps simplify checking if the possible path has left the {@link CentralArea}. And if so, whether
     * it enter the area again.
     *
     * @param centralArea               {@link CentralArea} defines a polygon bounding are for the drone.
     * @param intersectCentralAreaCount The current number of times the path has crossed the {@link CentralArea}.
     * @param current                   Details of the current {@link PathNode}
     * @param next                      {@link PathNode} of the next node to be validated
     * @return Returns the number of times the next node would cross the {@link CentralArea} boundary.
     */
    public int isPathValid(CentralArea centralArea, int intersectCentralAreaCount, PathNode current, PathNode next) {
        LngLat[] coordinates = centralArea.getCoordinates();
        int tempCount = 0;
        for (int i = 0; i < coordinates.length; i++) {
            if (i == coordinates.length - 1) {
                if (current.getValue().isIntersecting(coordinates[i], coordinates[0],
                        current.getValue(), next.getValue())) {
                    tempCount += 1;
                }
            } else if (current.getValue().isIntersecting(coordinates[i], coordinates[i + 1],
                    current.getValue(), next.getValue())) {
                tempCount += 1;
            }
        }
        return tempCount;
    }

    // function that checks if the next node is in a no fly zone
//    public boolean isNodeValid(NoFlyZone[] noFlyZones, PathNode next) {
//        for (int i = 0; i < noFlyZones.length; i++) {
//            LngLat[] coordinates = noFlyZones[i].getCoordinates();
//            if (next.getValue().inArea(coordinates)) {
//                return false;
//            }
//        }
//        return true;
//    }
}
